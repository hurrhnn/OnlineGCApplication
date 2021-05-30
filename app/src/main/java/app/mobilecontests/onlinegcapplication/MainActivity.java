package app.mobilecontests.onlinegcapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Animation topAnim;
    Animation bottomAnim;

    ImageView school;
    Button startBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAnim = AnimationUtils.loadAnimation(this, R.anim.school_animation);

        bottomAnim = AnimationUtils.loadAnimation(this, R.anim.splash_school_animation);
        bottomAnim.setRepeatCount(Animation.INFINITE);
        bottomAnim.setRepeatMode(Animation.RESTART);

        school = findViewById(R.id.main_school_image);
        school.setAnimation(topAnim);
//        school.setAnimation(bottomAnim);

        startBtn = findViewById(R.id.startBtn);
        startBtn.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OCLoginActivity.class);
            startActivity(intent);
        });
    }

}