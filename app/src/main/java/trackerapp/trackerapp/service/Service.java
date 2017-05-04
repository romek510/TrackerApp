package trackerapp.trackerapp.service;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by Krystian on 04.05.2017.
 */

public class Service {
    public boolean networkIsEnabled;
    public Activity activity;

    public Service() {
        this.networkIsEnabled = false;
    }

    public Service(Activity activity) {
        this.networkIsEnabled = isNetworkAvailable(activity);;
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}
