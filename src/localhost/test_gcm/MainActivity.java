package localhost.test_gcm;

import localhost.test_gcm.CommonUtilities;
import com.google.android.gcm.GCMRegistrar;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.app.Activity;

public class MainActivity extends Activity implements OnClickListener{

	Button onButton;
	Button offButton;
	String regId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);	
		setContentView(R.layout.activity_main);
		
		onButton  = (Button)findViewById(R.id.btn_register);
		onButton.setOnClickListener(this);
		
		offButton  = (Button)findViewById(R.id.btn_unregister);
		offButton.setOnClickListener(this);
		
        /* Push Notification Service */
        try{
	        GCMRegistrar.checkDevice(this);
	        GCMRegistrar.checkManifest(this);
        }
        catch(Exception e ){
        	 Log.d(CommonUtilities.TAG_GCM, "Exception :" + e);
        }		
	}
	
	@Override
	public void onClick(View v) {
		regId = GCMRegistrar.getRegistrationId(getBaseContext());
		if (v == onButton){
			if (regId.equals("")) {
				GCMRegistrar.register(getBaseContext(), CommonUtilities.SENDER_ID);
			}else{
				Log.d(CommonUtilities.TAG_GCM, "Already registered.");
			}			
		}
		else if (v == offButton){
			if (regId.equals("")) {
				Log.d(CommonUtilities.TAG_GCM, "Already unregistered.");
			}else{
				GCMRegistrar.unregister(getBaseContext());
			}		
		}
	}		
}
