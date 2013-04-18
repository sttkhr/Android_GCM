package localhost.test_gcm;

import android.util.Log;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import static localhost.test_gcm.CommonUtilities.TAG_GCM;
import static localhost.test_gcm.CommonUtilities.API_KEY;
import static localhost.test_gcm.CommonUtilities.SEND_URL;

public final class ServerUtilities {

    private static final int MAX_ATTEMPTS = 5;
    private static final int BACKOFF_MILLI_SECONDS = 2000;
    private static final Random random = new Random();	
	

	public static boolean sendMessage(String regId , String msg)
	{
        Map<String, String> params  = new HashMap<String, String>(); 
        Map<String, String> headers = new HashMap<String, String>();      
        params.put("registration_id", regId);	
        params.put("collapse_key", "1");
        params.put("data.message", msg);
        headers.put("Authorization", "key=" + API_KEY);
		
        long backoff = BACKOFF_MILLI_SECONDS + random.nextInt(1000);
        for (int i = 1; i <= MAX_ATTEMPTS; i++) {
            Log.d(TAG_GCM, "Attempt #" + i + " to register");
            try {
                post(SEND_URL, params , headers);
                return true;
            } catch (IOException e) {
                Log.e(TAG_GCM, "Failed to register on attempt " + i, e);
                if (i == MAX_ATTEMPTS) {
                    break;
                }
                try {
                    Log.d(TAG_GCM, "Sleeping for " + backoff + " ms before retry");
                    Thread.sleep(backoff);
                } catch (InterruptedException e1) {
                    // Activity finished before we complete - exit.
                    Log.d(TAG_GCM, "Thread interrupted: abort remaining retries!");
                    Thread.currentThread().interrupt();
                    return false;
                }
                // increase backoff exponentially
                backoff *= 2;
            }
        }
        return false;
	}    

    /**
     * Issue a POST request to the server.
     *
     * @param endpoint POST address.
     * @param params request parameters.
     *
     * @throws IOException propagated from POST.
     */
	static void post(String endpoint, Map<String, String> params , Map<String, String> headers)
            throws IOException {
        URL url;
        try {
            url = new URL(endpoint);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException("invalid url: " + endpoint);
        }
        StringBuilder bodyBuilder = new StringBuilder();
        Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
        // constructs the POST body using the parameters
        while (iterator.hasNext()) {
            Entry<String, String> param = iterator.next();
            bodyBuilder.append(param.getKey()).append('=')
                    .append(param.getValue());
            if (iterator.hasNext()) {
                bodyBuilder.append('&');
            }
        }
        String body = bodyBuilder.toString();
        Log.v(TAG_GCM, "Posting '" + body + "' to " + url);
        byte[] bytes = body.getBytes();
        HttpURLConnection conn = null;
        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setUseCaches(false);
            conn.setFixedLengthStreamingMode(bytes.length);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded;charset=UTF-8");
            Iterator<Entry<String, String>> iteratorHeaders = headers.entrySet().iterator();
            while (iteratorHeaders.hasNext()) {
                Entry<String, String> header = iteratorHeaders.next();
                conn.setRequestProperty(header.getKey() , header.getValue());
            }           
            
            // post the request
            OutputStream out = conn.getOutputStream();
            out.write(bytes);
            out.close();
            // handle the response
            int status = conn.getResponseCode();
            if (status != 200) {
            	Log.e(TAG_GCM, "Posting status is not  200.");
              throw new IOException("Post failed with error code " + status);
            }
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }
}
