package app.mobilecontests.onlinegcapplication;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
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
            Intent intent = new Intent(MainActivity.this, OCLoginActivity.class);
            startActivity(intent);
        });
    }
}
