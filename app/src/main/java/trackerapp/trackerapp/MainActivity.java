package trackerapp.trackerapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.BaseJsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.entity.StringEntity;

public class MainActivity extends Activity {

    public static final String PREFERENCES_NAME = "auth";
    private MainActivity self;
    private AlertDialog ad;
    private boolean requestInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        self = this;

        new android.os.Handler().postDelayed(new Runnable() {
            public void run() {
                checkAccess(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(self, TrackerActivity.class);
                        self.finish();
                        self.startActivity(intent);
                    }
                }, new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(self, LoginActivity.class);
                        self.finish();
                        self.startActivity(intent);
                    }
                });
            }
        }, 1000);

    }

    private void checkAccess(final Runnable success, final Runnable failed) {

        final ProgressDialog progress = new ProgressDialog(self);
        progress.setTitle(getResources().getString(R.string.checking_log_in));
        progress.setMessage(getResources().getString(R.string.wait_for_checking_log_in));

        SharedPreferences preferences = getSharedPreferences(PREFERENCES_NAME, 0);

        final AsyncHttpClient client = new AsyncHttpClient();
        client.addHeader("Authorization", preferences.getString("token_type", "") + " " + preferences.getString("access_token", ""));

        if (!requestInProgress) {

            final JSONObject jsonParams = new JSONObject();
            StringEntity entity = null;
            try {
                jsonParams.put("locale", Locale.getDefault().toString());
                entity = new StringEntity(jsonParams.toString());
            } catch (JSONException e) {
                Log.e("error", e.toString());
                //ErrorService.log(getApplicationContext(), "JSONException", e.toString());
            } catch (UnsupportedEncodingException e) {
                Log.e("error1", e.toString());
                //ErrorService.log(getApplicationContext(), "UnsupportedEnco", e.toString());
            }

            client.post(this, "localhost/trackerapp", entity, "application/json", new BaseJsonHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, String rawJsonResponse, Object response) {
                    if (success != null) {
                        success.run();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, String rawJsonData, Object errorResponse) {
                    if (failed != null) {
                        failed.run();
                    }
                }

                @Override
                protected Object parseResponse(String rawJsonData, boolean isFailure) throws Throwable {
                    return null;
                }
            });
        }
    }

}
