package localhost.test_gcm;

import static localhost.test_gcm.CommonUtilities.SENDER_ID;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationCompat.Builder;

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
    	Bundle extras = intent.getExtras();
    	String message = extras.getString("message");
        Log.i(CommonUtilities.TAG_GCM, "Received message:" + message);       
        generateNotification(context, message);
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
    
    /**
     * Issues a notification to inform the user that server has sent a message.
     */
    @SuppressLint("NewApi")
	private static void generateNotification(Context context, String message) {
    	Intent notificationIntent = new Intent(context, MainActivity.class);
    	PendingIntent pendingIntent = 
    			PendingIntent.getActivity(context, 0, notificationIntent, 0);
    	
    	//Notification.Builder builder = new Notification.Builder(context);
    	Builder builder = new NotificationCompat.Builder(context);
    	
    	builder.setTicker("通知テスト受信");
    	builder.setContentTitle("GCM通知テスト");
       	builder.setContentText(message);
       	builder.setContentInfo("info");
       	builder.setWhen(System.currentTimeMillis());
       	builder.setContentIntent(pendingIntent);
       	builder.setSmallIcon(android.R.drawable.ic_menu_info_details);
       	Notification notification = builder.build(); 
       	
        NotificationManager notificationManager = (NotificationManager)
                context.getSystemService(Context.NOTIFICATION_SERVICE); 
        notification.flags = Notification.FLAG_AUTO_CANCEL;
        notificationManager.notify(1, notification);
        Log.i(CommonUtilities.TAG_GCM, "generateNotification:" + message);  
    }   
}
