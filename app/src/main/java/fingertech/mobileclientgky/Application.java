package fingertech.mobileclientgky;

import android.util.Log;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParsePush;
import com.parse.PushService;
import com.parse.SaveCallback;

/**
 * Created by William Stefan Hartono
 */
public class Application extends android.app.Application {

    public Application() {}

    @Override
    public void onCreate() {
        Parse.initialize(this, "iXHjKu5UpQ4tGedFm8teV7i6jFqAI5T0ttDF5p1V", "HZappdT4wp3vrZKA25ZHWUmm8Q7ZZqSs9w1CryKQ");
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // Specify an Activity to handle all pushes by default.
        PushService.setDefaultPushCallback(this, Home.class);

        // Untuk subscribe, channel default adalah "", yaitu untuk "broadcast"
        ParsePush.subscribeInBackground("", new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.d("com.parse.push", "successfully subscribed to the broadcast channel.");
                } else {
                    Log.e("com.parse.push", "failed to subscribe for push", e);
                }
            }
        });
    }
}
