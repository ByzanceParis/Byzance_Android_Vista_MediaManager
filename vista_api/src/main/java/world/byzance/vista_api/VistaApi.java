package world.byzance.vista_api;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by maelle on 09/07/2019.
 */

public class VistaApi {
    public static String BaseURL = "";
    OkHttpClient Httpclient;
    MediaManager mediaManager;
    private String TAG = "VistaAPI";
    private UpdateDate updateDate;
    private Context context;

    public VistaApi (String url, Context c){
        BaseURL = url;
        context = c;
        Httpclient = new OkHttpClient.Builder() .connectTimeout(90, TimeUnit.SECONDS).readTimeout(90, TimeUnit.SECONDS) .writeTimeout(90, TimeUnit.SECONDS).build();
        updateDate = new UpdateDate(context);
        mediaManager = new MediaManager(context);
        LocalBroadcastManager.getInstance(context).registerReceiver(updateStatusReceiver,
                new IntentFilter("updateStatus"));
    }
    interface OnRequestComplete {
        void OnRequestComplete(JSONArray response);
        void OnRequestError();
    }

    public void setFolder(String folderPath){
        mediaManager.setFolderPath(folderPath);
    }
    //check the last update date and launch update if needed
    public void update(){
        getModels("/Updates", new OnRequestComplete() {
            public void OnRequestComplete(JSONArray response) {
                Log.d(TAG,response.toString());
                try {
                    JSONObject dateJson = response.getJSONObject(0);
                    String dateString = (String) dateJson.get("Last_Update");
                    if(!updateDate.isUpToDate(dateString)){
                        updateDate.setDate(dateString);
                        updateMedias();
                    }else{
                        Log.d(TAG, "already up to date");
                        Intent i = new Intent("world.byzance.VistaApi.complete");
                        context.sendBroadcast(i);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void OnRequestError() {
                Log.e(TAG,"Connection Failed");
                Intent intent = new Intent("updateStatus");
                intent.putExtra("status", "Connection Failed");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }


    //get all media and check if they need an update
    private void updateMedias(){
        Log.d(TAG,"Update Content");
        getModels("/Media", new OnRequestComplete() {
            public void OnRequestComplete(JSONArray response) {
                Log.d(TAG,response.toString());
                mediaManager.update(response);
            }
            @Override
            public void OnRequestError() {
                Log.e(TAG,"An error occured");
                Intent intent = new Intent("updateStatus");
                intent.putExtra("status", "Connection Failed");
                LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
            }
        });
    }
    private BroadcastReceiver updateStatusReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            String message = intent.getStringExtra("status");
            Log.d(TAG, "update status : " + message);
            if(message.equals("Complete")){
                updateDate.saveTheDate();
            }
            Toast toast = Toast.makeText(context, "Update status : " + message, Toast.LENGTH_LONG);
            toast.show();

            Intent i = new Intent("world.byzance.VistaApi.complete");
            context.sendBroadcast(i);
        }
    };
    //Http request to api to get a specific model
    private void getModels(String model, final OnRequestComplete listener){
        Request request = new Request.Builder()
                .url(BaseURL + model)
                .get()
                .build();
        Httpclient.newCall(request).enqueue(new Callback() {
                                                @Override
                                                public void onFailure(Call call, IOException e) {
                                                    listener.OnRequestError();
                                                    e.printStackTrace();
                                                }

                                                @Override
                                                public void onResponse(Call call, final Response response) throws IOException {
                                                    if (!response.isSuccessful()) {
                                                        throw new IOException("Unexpected code " + response);

                                                    } else {
                                                        String jsonData = response.body().string();
                                                        try {
                                                            JSONArray jsonarray = new JSONArray(jsonData);
                                                            listener.OnRequestComplete(jsonarray);

                                                        } catch (JSONException e) {
                                                            listener.OnRequestError();
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            });
    }
}
