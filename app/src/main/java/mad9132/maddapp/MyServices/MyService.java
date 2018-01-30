package mad9132.maddapp.MyServices;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.gson.Gson;

import java.io.IOException;

import mad9132.maddapp.model.CoursePOJO;
import mad9132.maddapp.utils.HttpHelper;
import mad9132.maddapp.utils.RequestPackage;

import static mad9132.maddapp.MainActivity.TAG_DEBUG;

/**
 * Class MyService.
 * <p>
 * Fetch the data at URI.
 * Return an array of Building[] as a broadcast message.
 *
 * @author David Gasner
 */
public class MyService extends IntentService {

    public static final String MY_SERVICE_MESSAGE = "myServiceMessage";
    public static final String MY_SERVICE_PAYLOAD = "myServicePayload";
    public static final String MY_SERVICE_RESPONSE = "myServiceResponse";
    public static final String MY_SERVICE_EXCEPTION = "myServiceException";
    public static final String REQUEST_PACKAGE = "requestPackage";

    public MyService() {
        super("MyService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        RequestPackage requestPackage = (RequestPackage) intent.getParcelableExtra(REQUEST_PACKAGE);
        Log.d(TAG_DEBUG, "MyService: httpHelper Started");
        String response;
        try {
            response = HttpHelper.downloadUrl(requestPackage);
            Log.d(TAG_DEBUG, "MyService: httpHelper end");
        } catch (IOException e) {
            e.printStackTrace();
            Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
            messageIntent.putExtra(MY_SERVICE_EXCEPTION, e.getMessage());
            LocalBroadcastManager manager =
                    LocalBroadcastManager.getInstance(getApplicationContext());
            manager.sendBroadcast(messageIntent);
            return;
        }

        Intent messageIntent = new Intent(MY_SERVICE_MESSAGE);
        Gson gson = new Gson();
        switch (requestPackage.getMethod()) {
            case GET:
                CoursePOJO[] buildingsArray = gson.fromJson(response, CoursePOJO[].class);
                messageIntent.putExtra(MY_SERVICE_PAYLOAD, buildingsArray);
                break;
            case POST:
            case PUT:
            case DELETE:
                CoursePOJO building = gson.fromJson(response, CoursePOJO.class);
                messageIntent.putExtra(MY_SERVICE_RESPONSE, building);
                break;
        }
        LocalBroadcastManager manager =
                LocalBroadcastManager.getInstance(getApplicationContext());
        manager.sendBroadcast(messageIntent);
    }
}
