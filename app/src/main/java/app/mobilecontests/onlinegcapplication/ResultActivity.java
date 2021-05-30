package app.mobilecontests.onlinegcapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class ResultActivity extends AppCompatActivity {

    ImageView imageView;
    TextView name;
    Button signOut;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        Intent intent = getIntent();

//        ArrayList<Course> classNames = intent.getStringArrayListExtra("classNames");
//        ArrayList<String> classIds = intent.getStringArrayListExtra("classIds");

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        imageView = findViewById(R.id.iv_profile);
        name = findViewById(R.id.tv_result);
        account();

        signOut = findViewById(R.id.btn_logout); //로그아웃
        signOut.setOnClickListener(v -> {
            if (v.getId() == R.id.btn_logout) {
                signOut();
            }
        });
    }

    private void signOut() {
        mGoogleSignInClient.signOut()
                .addOnCompleteListener(this, task -> {
                    Toast.makeText(ResultActivity.this,"Sign Out Successfully!", Toast.LENGTH_LONG).show();
                    finish();
                });
    }
    private void account(){
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if(account != null){
            String Name = account.getDisplayName();
            Uri photo = account.getPhotoUrl();
            name.setText(Name);
            if(photo != null)
                Glide.with(this).load(String.valueOf(photo)).into(imageView);
            else
                Toast.makeText(getApplicationContext(),"사진이 없습니다.",Toast.LENGTH_LONG).show();
        }
    }
}