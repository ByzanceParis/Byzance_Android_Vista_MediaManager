package world.byzance.vista_test;

import android.util.Log;

import world.byzance.vista_api.VistaApi;

import static world.byzance.vista_test.MyApplication.getAppContext;

/**
 * Created by maelle on 09/07/2019.
 */

public class VistaManager {
    private static final VistaManager ourInstance = new VistaManager();

    public static VistaManager getInstance() {
        return ourInstance;
    }

    private VistaApi vistaApi;

    private VistaManager() {
        vistaApi = new VistaApi("http://192.168.1.14:1337", getAppContext());
        vistaApi.setFolder("/sdcard/Movies/test/");
        vistaApi.update();
    }
}
