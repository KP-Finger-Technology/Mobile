package fingertech.mobileclientgky;

import android.content.ContentValues;
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
import java.util.ArrayList;


/**
 * Created by Andarias Silvanus on 15/06/03.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    private static String my_package = "fingertech.mobileclientgky";
    // The Android's default system path of your application database.
    private static String DB_PATH;
    private static String DB_NAME = "gky_pluit.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    private ArrayList<String> pasalAlkitab;
    private ArrayList<Integer> jumlahPasal;
    private int jumlah_kitab = 66;

    /**
     * Constructor
     * Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        pasalAlkitab = new ArrayList<String>();
        jumlahPasal = new ArrayList<Integer>();

        DB_PATH = context.getDatabasePath(DB_NAME).getAbsolutePath();
        try {
            createDataBase();
        } catch (IOException e) {
            Log.e("DataBaseHelper", e.getMessage(), e);
        }
    }

    /**
     * Creates a empty database on the system and rewrites it with your own database.
     * */
    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();

        if(dbExist){
            // Do nothing - database already exist
        }
        else{
            // By calling this method and empty database will be created into the default system path
            // of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            try {
                copyDataBase();
            }
            catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;

        try {
            String myPath = DB_PATH;
            File file = new File(myPath);
            if (file.exists() && !file.isDirectory())
                checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
        }
        catch(SQLiteException e) {
            // Database does't exist yet.
        }

        boolean res = false;
        if(checkDB != null) {
            checkDB.close();
            res = true;
        }
        return res;
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the
     * system folder, from where it can be accessed and handled.
     * This is done by transfering bytestream.
     * */
    private void copyDataBase() throws IOException {
        SQLiteDatabase.openOrCreateDatabase(DB_PATH,null);
        // Open your local db as the input stream
        InputStream myInput = myContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        // String outFileName = DB_PATH + DB_NAME;
        String outFileName = DB_PATH;

        // Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        // Transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        // Close the streams
        myInput.close();
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }

    public void openDataBase() throws SQLException {
        // Open the database
        String myPath = DB_PATH;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READONLY);
    }

    public void closeDataBase() {
        myDataBase.close();
    }

    public boolean isTableExists(String tableName) {
        if (tableName == null || myDataBase == null || !myDataBase.isOpen()) {
            return false;
        }
        Cursor cursor = myDataBase.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = ? AND name = ?", new String[] {"table", tableName});
        if (!cursor.moveToFirst()) {
            return false;
        }
        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    @Override
    public synchronized void close() {
        if(myDataBase != null)
            myDataBase.close();
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {}

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}

    // Add your public helper methods to access and get content from the database.
    // You could return cursors by doing "return myDataBase.query(....)" so it'd be easy
    // to you to create adapters for your views.

    // Untuk Alkitab
    public void getDaftarKitab() {
        if (DB_PATH != null) {
            String tableName = "BOOKS";
            boolean check = isTableExists(tableName);
            if (check) {
                Cursor cursor = myDataBase.rawQuery("SELECT * FROM BOOKS ORDER BY keyid ASC", null);
                int colKitab = cursor.getColumnIndex("isi");
                int colPasal = cursor.getColumnIndex("pasal");

                // Check if our result was valid
                int i = 0;
                if (cursor != null && cursor.moveToFirst()) {
                    // Loop through all Results
                    do {
                        pasalAlkitab.add(cursor.getString(colKitab));
                        jumlahPasal.add(cursor.getInt(colPasal));
                        i++;
                    } while (cursor.moveToNext());
                    cursor.close();
                }
            }
        }
    }

    public void searchKitab(String _kitab) {
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM BOOKS_1 WHERE isi_books LIKE \"%"+_kitab+"%\" ORDER BY keyid ASC", null);
        int colKitab = cursor.getColumnIndex("isi_books");
        int colPasal = cursor.getColumnIndex("pasal");

        pasalAlkitab = new ArrayList<String>();
        jumlahPasal = new ArrayList<Integer>();

        // Check if our result was valid
        if (cursor != null && cursor.moveToFirst()) {
            // Loop through all results
            do {
                String judul = cursor.getString(colKitab);
                int pasal = cursor.getInt(colPasal);
                pasalAlkitab.add(judul);
                jumlahPasal.add(pasal);
            } while(cursor.moveToNext());
            cursor.close();
        }
    }

    public ArrayList<String> getPasal(String kitab, int pasal) {
        ArrayList<String> res = new ArrayList<String>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM BIBLE JOIN BOOKS_1 WHERE isi_books=\""+kitab+"\" AND BIBLE.pasal="+pasal+" AND BOOKS_1.keyid=BIBLE.kitab", null);
        int colIsi = cursor.getColumnIndex("isi");
        int colJudul = cursor.getColumnIndex("judul");

        // Check if our result was valid
        if (cursor != null && cursor.moveToFirst()) {
            do {
                res.add(cursor.getString(colIsi));
            }while(cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public int getJumlahAyat(String kitab, int pasal) {
        int jumAyat = 0;
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM BIBLE JOIN BOOKS_1 WHERE isi_books=\"" + kitab + "\" AND BIBLE.pasal=" + pasal + " AND BOOKS_1.keyid=BIBLE.kitab", null);

        // Check if our result was valid
        if (cursor != null && cursor.moveToFirst()) {
            // Loop through all results
            do {
                jumAyat++;
            } while(cursor.moveToNext());
            cursor.close();
        }
        return jumAyat;
    }

    public ArrayList<String> getPasalAlkitab() {
        return pasalAlkitab;
    }

    public ArrayList<Integer> getJumlahPasal() {
        return jumlahPasal;
    }

    // Untuk KPPK
    public ArrayList<String> getKPPK() {
        ArrayList<String> res = new ArrayList<String>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM KPPK", null);
        int colJudul = cursor.getColumnIndex("judul");
        int colIsi = cursor.getColumnIndex("isi");

        // Check if our result was valid
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String judul = cursor.getString(colJudul);
                String isi = cursor.getString(colIsi);
                res.add(judul);
                res.add(isi);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public ArrayList<String> searchKPPK(String _KPPK) {
        ArrayList<String> res = new ArrayList<String>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM KPPK WHERE judul LIKE \"%" + _KPPK + "%\" OR isi LIKE \"%" + _KPPK + "%\"", null);
        int colJudul = cursor.getColumnIndex("judul");
        int colIsi = cursor.getColumnIndex("isi");

        // Check if our result was valid
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String judul = cursor.getString(colJudul);
                String isi = cursor.getString(colIsi);
                res.add(judul);
                res.add(isi);
            }while(cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public boolean createTableKPPK() {
        boolean isSuccess = false;
        if (isTableExists("KPPK"))
            return isSuccess;
        else {
            myDataBase=this.getWritableDatabase() ;
            String query = "CREATE TABLE KPPK ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, judul TEXT, isi TEXT)";
            myDataBase.execSQL(query);

            isSuccess = true;
            return isSuccess;
        }
    }

    public boolean deleteTableKPPK () {
        boolean isSuccess = false;
        if (!isTableExists("KPPK"))
            return isSuccess;
        else {
            myDataBase=this.getWritableDatabase() ;
            String query = "DROP TABLE KPPK";
            myDataBase.execSQL(query);

            isSuccess = true;
            return isSuccess;
        }
    }

    public void insertDataKPPK(ArrayList<String> container) {
        ContentValues cv = new ContentValues(2);
        int len = container.size();
        myDataBase=this.getWritableDatabase() ;
        for (int i = 0; i < len; i = i + 2) {
            String judul = container.get(i);
            String isi = container.get(i + 1);
            cv.put("judul", judul);
            cv.put("isi", isi);
            myDataBase.insert("KPPK", null, cv);
        }
    }

    // Untuk Lirik Lagu Rohani
    public ArrayList<String> getLirikLaguRohani() {
        ArrayList<String> res = new ArrayList<String>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM LirikLaguRohani", null);
        int colJudul = cursor.getColumnIndex("judul");
        int colIsi = cursor.getColumnIndex("isi");

        // Check if our result was valid
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String judul = cursor.getString(colJudul);
                String isi = cursor.getString(colIsi);
                res.add(judul);
                res.add(isi);
            } while(cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public ArrayList<String> searchLirik(String _lirik) {
        ArrayList<String> res = new ArrayList<String>();
        Cursor cursor = myDataBase.rawQuery("SELECT * FROM LirikLaguRohani WHERE judul LIKE \"%" + _lirik + "%\" OR isi LIKE \"%" + _lirik + "%\"", null);
        int colJudul = cursor.getColumnIndex("judul");
        int colIsi = cursor.getColumnIndex("isi");

        // Check if our result was valid
        if (cursor != null && cursor.moveToFirst()) {
            do {
                String judul = cursor.getString(colJudul);
                String isi = cursor.getString(colIsi);
                res.add(judul);
                res.add(isi);
            } while(cursor.moveToNext());
            cursor.close();
        }
        return res;
    }

    public boolean createTableLirikLaguRohani() {
        boolean isSuccess = false;
        if (isTableExists("LirikLaguRohani"))
            return isSuccess;
        else {
            myDataBase = this.getWritableDatabase() ;
            String query = "CREATE TABLE LirikLaguRohani ( id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, judul TEXT, isi TEXT)";
            myDataBase.execSQL(query);

            isSuccess = true;
            return isSuccess;
        }
    }

    public boolean deleteTableLirikLaguRohani() {
        boolean isSuccess = false;
        if (!isTableExists("LirikLaguRohani"))
            return isSuccess;
        else {
            myDataBase = this.getWritableDatabase() ;
            String query = "DROP TABLE LirikLaguRohani";
            myDataBase.execSQL(query);

            isSuccess = true;
            return isSuccess;
        }
    }

    public void insertDataLirikLaguRohani(ArrayList<String> container) {
        ContentValues cv = new ContentValues(2);
        int len = container.size();
        myDataBase = this.getWritableDatabase() ;
        for (int i = 0; i < len; i = i + 2) {
            String judul = container.get(i);
            String isi = container.get(i+1);
            cv.put("judul", judul);
            cv.put("isi", isi);
            myDataBase.insert("LirikLaguRohani", null, cv);
        }
    }
}
