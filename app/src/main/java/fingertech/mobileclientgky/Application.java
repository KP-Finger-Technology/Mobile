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
        Parse.initialize(this, "BdqtV1LzsPow0SVhwN38wqUY0pwZEySkJCgdG9VZ", "s6elMhoO6vuBhStfXNq3d5yC94vV1QnFFjAfvjoD");
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
