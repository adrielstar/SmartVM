package ga.adriwalter.smartvending;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseUser;

import ga.adriwalter.smartvending.model.Account;
import ga.adriwalter.smartvending.utils.Credential;
import ga.adriwalter.smartvending.utils.ParseConstants;

public class SmartVendingApplication extends Application {
    public static final String APPLICATION_ID = Credential.APPLICATION_ID; //Parse APPLICATION_ID
    public static final String CLIENT_KEY = Credential.CLIENT_KEY;  //Parse APPLICATION_ID

    @Override
    public void onCreate() {
        super.onCreate();

        // Register your parse models.
        ParseObject.registerSubclass(Account.class);

        // Enable Local Datastore.
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, APPLICATION_ID, CLIENT_KEY);
    }

    public static void updateParseInstallation(ParseUser parseUser) {
        ParseInstallation installation = ParseInstallation.getCurrentInstallation();
        installation.put(ParseConstants.KEY_USER_ID, parseUser.getObjectId());
        installation.saveInBackground();
    }
}