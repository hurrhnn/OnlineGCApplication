package app.mobilecontests.onlinegcapplication;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import app.mobilecontests.onlinegcapplication.ebsoc.OCLoginActivity;
import app.mobilecontests.onlinegcapplication.sqlite.SQLiteHelper;

public class MainActivity extends AppCompatActivity {

    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "ogc.db", null, 1);
        database = sqLiteHelper.getWritableDatabase();
        sqLiteHelper.onCreate(database);

        Animation topAnim = AnimationUtils.loadAnimation(this, R.anim.school_animation);
        ImageView school = findViewById(R.id.main_school_image);
        school.setAnimation(topAnim);

        Button startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(v -> {
            if (!hasNetworkConnection())
                alertNetworkConnection();
            else {
                Intent intent = new Intent(MainActivity.this, OCLoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void alertNetworkConnection() //네트워크 연결 오류 시 Toast
    {
        Toast.makeText(getApplicationContext(), "네트워크 연결 상태를 확인해주세요.", Toast.LENGTH_SHORT).show();
    }

    private boolean hasNetworkConnection() { //네트워크 연결 확인하는 코드
        int[] networkTypes = {ConnectivityManager.TYPE_MOBILE, ConnectivityManager.TYPE_WIFI};
        try {
            ConnectivityManager manager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
            for (int networkType : networkTypes) {
                NetworkInfo activeNetwork = manager.getActiveNetworkInfo();
                if (activeNetwork != null && activeNetwork.getType() == networkType) {
                    return true;
                }
            }
        } catch (Exception e) {
            return false;
        }
        return false;
    }
}
