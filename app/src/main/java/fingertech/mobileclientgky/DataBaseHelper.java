package fingertech.mobileclientgky;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by Andarias Silvanus on 15/06/03.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static String my_package = "fingertech.mobileclientgky";
    //The Android's default system path of your application database.
//    private static String DB_PATH = "/data/data/"+my_package+"/databases/";
    private static String DB_PATH;
    private static String DB_NAME = "gky_pluit.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private String[] pasalAlkitab;
    private int[] jumlahPasal;
    private int jumlah_kitab = 66;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        pasalAlkitab = new String[jumlah_kitab];
        jumlahPasal = new int[jumlah_kitab];
        DB_PATH = context.getDatabasePath(DB_NAME).getAbsolutePath();
        Log.d("path absolut database",DB_PATH);
        try {
            Log.d("persiapan buat database dari konstruktor DB","...");
            createDataBase();
        } catch (IOException e) {
            Log.d("gagal buat database dari konstruktor DB!","...");
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist){
            //do nothing - database already exist
            Log.d("database already exist, not create","");
        }
        else{
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                Log.d("persiapan copy database","...");
                copyDataBase();
            }
            catch (IOException e) {
                Log.d("error copy database!","...");
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase(){
        SQLiteDatabase checkDB = null;

        try{
//            String myPath = DB_PATH + DB_NAME;
            String myPath = DB_PATH;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch(SQLiteException e){
            //database does't exist yet.
            Log.d("database tidak exist!","...");
        }

        boolean res = false;
        if(checkDB != null){
            checkDB.close();
            res = true;
        }
//        boolean res = checkDB != null ? true : false;
        Log.d("nilai boolean res",Boolean.toString(res));
        return res;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException{
        Log.d("masuk copyDatabase!","...");
        SQLiteDatabase.openOrCreateDatabase(DB_PATH,null);
        //Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        if (myInput != null)
            Log.d("get Asset berhasil","...");
        else
            Log.d("get Asset gagal","...");

        // Path to the just created empty db
//        String outFileName = DB_PATH + DB_NAME;
        String outFileName = DB_PATH;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        if (myOutput != null)
            Log.d("output Stream berhasil!","link filename: "+outFileName);
        else
            Log.d("output Stream gagal!","link filename: "+outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer))>0){
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myInput.close();
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        //Open the database
//        String myPath = DB_PATH + DB_NAME;
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void closeDataBase() {
        myDataBase.close();
    }

    private boolean isTableExists(SQLiteDatabase db, String tableName) {
        if (tableName == null || db == null || !db.isOpen()) {
            return false;
        }
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst()) {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public void getDaftarKitab() {
        if (DB_PATH != null) {
            String tableName = "BOOKS";
            boolean check = isTableExists(this.myDataBase, tableName);
            if (check) {
                Cursor cursor = myDataBase.rawQuery("SELECT * FROM BOOKS ORDER BY keyid ASC", null);
                int colKitab = cursor.getColumnIndex("isi");
                int colPasal = cursor.getColumnIndex("pasal");

                // Check if our result was valid
                cursor.moveToFirst();
                int i = 0;
                if (cursor != null) {
                    // Loop through all Results
                    do {
                        pasalAlkitab[i] = cursor.getString(colKitab);
                        jumlahPasal[i] = cursor.getInt(colPasal);
                        i++;
                    } while (cursor.moveToNext());
                }
            }
        }
    }

    public String[] getPasalAlkitab () {
        return pasalAlkitab;
    }

    public int[] getJumlahPasal() {
        return jumlahPasal;
    }

    private String resultQueryPasal;

    public void getPasal(String kitab, int pasal) {
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM BIBLE JOIN BOOKS_1 WHERE isi_books=\""+kitab+"\" AND BIBLE.pasal="+pasal+" AND BOOKS_1.keyid=BIBLE.kitab", null);
        int colIsi = cursor.getColumnIndex("isi");

        // Check if our result was valid
        cursor.moveToFirst();
        int ayat = 1;
        resultQueryPasal = "";
        if (cursor != null) {
            // Loop through all Results
            do {
                resultQueryPasal = resultQueryPasal + Integer.toString(ayat) + ". " + cursor.getString(colIsi) + "\n";
                ayat++;
            }while(cursor.moveToNext());
        }
    }

    public String getResultQueryPasal() {
        return resultQueryPasal;
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        try {
            Log.d("akan create database","...");
            createDataBase();
        } catch (IOException e) {
            Log.d("gagal create database","...");
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.
}
