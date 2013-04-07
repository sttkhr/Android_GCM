package localhost.test_gcm;

import static localhost.test_gcm.CommonUtilities.SENDER_ID;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;

/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {	
    public GCMIntentService() {
        super(SENDER_ID);
    }

    @Override
    protected void onRegistered(Context context, String registrationId) {
        Log.i(CommonUtilities.TAG_GCM, "Device registered: regId = " + registrationId);
    }

    @Override
    protected void onUnregistered(Context context, String registrationId) {
        Log.i(CommonUtilities.TAG_GCM, "Device unregistered: regId = " + registrationId);
    }

    @Override
    protected void onMessage(Context context, Intent intent) {
        Log.i(CommonUtilities.TAG_GCM, "Received message");       
    }
    
    @Override
    protected void onDeletedMessages(Context context, int total) {
        Log.i(CommonUtilities.TAG_GCM, "Received deleted messages");
    }

    @Override
    public void onError(Context context, String errorId) {
        Log.i(CommonUtilities.TAG_GCM, "Received error: " + errorId);
    }

    @Override
    protected boolean onRecoverableError(Context context, String errorId) {
        Log.i(CommonUtilities.TAG_GCM, "Received recoverable error: " + errorId);
		return false;
    } 
}
