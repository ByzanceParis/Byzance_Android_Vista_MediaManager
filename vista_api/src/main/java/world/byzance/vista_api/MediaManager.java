package world.byzance.vista_api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by maelle on 12/07/2019.
 */

public class MediaManager {
    private String TAG = "MediaManager";
    private Context context;
    ArrayList<Media> mediaList  = new ArrayList<Media>();
    SharedPreferences settings;
    String prefID = "MediaJson";
    String folderPath = "/sdcard/Movies/VistaFolder/";

    public MediaManager(Context c) {
        context = c;
        settings = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
        mediaList = getCurrentContent();
        File folder = new File("/sdcard/Movies/VistaFolder/");
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
    public void setFolderPath(String newfolderPath){

        folderPath = newfolderPath;
        File folder = new File(newfolderPath);
        if (!folder.exists()) {
            folder.mkdirs();
        }
    }
    //get Json of current media List saved and populate mediaList
    public ArrayList<Media> getCurrentContent() {
        ArrayList<Media> mediaList  = new ArrayList<Media>();

        String  MediaString = settings.getString(prefID, "[]");
        try {
            JSONArray jsonarray = new JSONArray(MediaString);
            for (int i=0; i < jsonarray.length(); i++) {
                mediaList.add(new Media(jsonarray.getJSONObject(i)));
            }
            return mediaList;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return mediaList;
    }

    //Save new Media List
    private void saveNewMediaList(JSONArray jsonArray){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(prefID, jsonArray.toString());
        editor.apply();
    }

    //check all media, add new one, udpate the one that needs it and clear the others.
    public void update(JSONArray jsonArray) throws JSONException {
        ArrayList<Media> newMediaList  = new ArrayList<Media>();
        for (int i=0; i < jsonArray.length(); i++) {
            newMediaList.add(new Media(jsonArray.getJSONObject(i)));
        }
        for (Media newm : newMediaList) {
            if(isNewMedia(newm)){
                newm.DownloadMedia(folderPath);
            }
        }
        cleanOldMedia();
        mediaList = newMediaList;
        saveNewMediaList(jsonArray);
        Log.d(TAG,"update complete");
        Intent intent = new Intent("updateStatus");
        intent.putExtra("status", "complete");
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    //Test if a media is a new media or need an upate
    private boolean isNewMedia(Media newMedia){
        for (Media m : mediaList) {
            if (newMedia.Name.equals(m.Name)){
                boolean response = !newMedia.updateDate.equals(m.updateDate);
                mediaList.remove(m);
                return response;
            }
        }
        return true;
    }
    private void cleanOldMedia() {
        for (Media m : mediaList) {
            m.deleteMedia(folderPath);
        }
    }
}
