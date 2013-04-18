package localhost.test_gcm;

import localhost.test_gcm.CommonUtilities;
import com.google.android.gcm.GCMRegistrar;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;
import static localhost.test_gcm.CommonUtilities.TAG_GCM;

public class MainActivity extends Activity implements OnClickListener{

	Button onButton;
	Button offButton;
	Button sendButton;
	String regId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_main);
		
		onButton  = (Button)findViewById(R.id.btn_register);
		onButton.setOnClickListener(this);
		
		offButton  = (Button)findViewById(R.id.btn_unregister);
		offButton.setOnClickListener(this);
		
		sendButton  = (Button)findViewById(R.id.btn_msg_send);
		sendButton.setOnClickListener(this);		
		
        /* Push Notification Service */
        try{
	        GCMRegistrar.checkDevice(this);
	        GCMRegistrar.checkManifest(this);
        }
        catch(Exception e ){
        	 Log.d(TAG_GCM, "Exception :" + e);
        }		
	}
	
	@Override
	public void onClick(View v) {
		regId = GCMRegistrar.getRegistrationId(getBaseContext());
		if (v == onButton){
			if (regId.equals("")) {
				GCMRegistrar.register(getBaseContext(), CommonUtilities.SENDER_ID);
			}else{
				Log.d(TAG_GCM, "Already registered.");
			}			
		}
		else if (v == offButton){
			if (regId.equals("")) {
				Log.d(TAG_GCM, "Already unregistered.");
			}else{
				GCMRegistrar.unregister(getBaseContext());
			}		
		}
		else if (v == sendButton){
			SendAsyncTask task = new SendAsyncTask(regId , "test");
			task.execute();
		}
	}		
	
	public class SendAsyncTask extends AsyncTask<String, String, Long> {
		private String mRegId;
		private String mMsg;		
		
	    public SendAsyncTask(String regId , String msg) {
	        this.mRegId = regId;
	        this.mMsg = msg;
	    }
	    
	    /**
	     * バックグランドで行う処理
	     */
	    @Override
	    protected Long doInBackground(String... params) {
	    	ServerUtilities.sendMessage(mRegId,mMsg);
	        return null;
	    }
	}	
}
