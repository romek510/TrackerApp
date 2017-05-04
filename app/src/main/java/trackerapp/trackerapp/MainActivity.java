package trackerapp.trackerapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private MainActivity self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        self = this;


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

    private void checkAccess(Runnable success, Runnable failed) {

        // TODO: połączenie z api, ktore sprawdza czy jestesmy zalogowani
        // jestesmy zalogowani
       /* if(success != null) {
            success.run();
        }*/
        // nie jestesmy zalogowani
        if(failed != null) {
            failed.run();
        }

    }


}
