package world.byzance.vista_api;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by maelle on 11/07/2019.
 */

public class UpdateDate {
    Context context;
    SharedPreferences settings;
    String prefID = "UpdateDate";

    public UpdateDate(Context c) {
        context = c;
        settings = context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE);
    }

    public boolean isUpToDate(String date){
        String current_date = settings.getString(prefID,"");
        if(current_date.equals(date)){
            return true;
        }
        return false;
    }

    public void saveTheDate(String date){
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(prefID, date);
        editor.apply();
    }
}
