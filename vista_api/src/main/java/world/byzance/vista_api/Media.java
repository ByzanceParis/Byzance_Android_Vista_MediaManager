package world.byzance.vista_api;

import android.nfc.tech.NfcA;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by maelle on 11/07/2019.
 */

public class Media {
    String Name;
    String updateDate;
    JSONObject media;

    public Media(JSONObject json) {
        try {
            Name = (String) json.get("Name");
            updateDate = (String) json.get("updated_at");
            media = (JSONObject) json.get("Media");
            Log.d("Media",media.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void DownloadMedia(String folderPath){
        Log.d("DownloadMedia", Name + " start ...");
        try {
            String url  = (String) media.get("url");
            String ext  = (String) media.get("ext");
            Log.d("DownloadMedia",folderPath + Name + ext);


            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder().url(VistaApi.BaseURL + url).build();
            Response response = client.newCall(request).execute();
            if (!response.isSuccessful()) {
                throw new IOException("Failed to download file: " + response);
            }

            FileOutputStream fos = new FileOutputStream(folderPath + Name + ext);
            fos.write(response.body().bytes());
            fos.close();
            Log.d("DownloadMedia",Name + " ....complete");

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public String getMediaName(){
        return Name;
    }

    //delete file of current media.
    public void deleteMedia(String folderPath) {
        try {
            String ext = (String) media.get("ext");
            String fileName = folderPath + Name + ext;

            File myFile = new File(fileName);
            if(myFile.exists())
                myFile.delete();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }
}
