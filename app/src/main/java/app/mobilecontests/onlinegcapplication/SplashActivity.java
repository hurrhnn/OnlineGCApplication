package app.mobilecontests.onlinegcapplication;

import android.app.Activity;
import android.os.Bundle;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler() {
            @Override
            public void publish(LogRecord record) {
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        };
    }
}