package app.mobilecontests.onlinegcapplication;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import app.mobilecontests.onlinegcapplication.sqlite.SQLiteHelper;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

import java.text.MessageFormat;

public class ResultActivity extends AppCompatActivity {

    Button signOut;
    private GoogleSignInClient mGoogleSignInClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "ogc.db", null, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
        account(database);

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
                    Toast.makeText(ResultActivity.this, "Sign Out Successfully!", Toast.LENGTH_LONG).show();
                    finish();
                });
    }

    private void account(SQLiteDatabase database) {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        if (account != null) {
            Cursor cursor = database.query("google_classroom", new String[]{"course", "course_works"}, null, null, null, null, null);
            ((TextView) findViewById(R.id.gc_result)).setText(MessageFormat.format("{0}개의 강의가 있습니다.", cursor.getCount()));
            cursor.close();
        }
    }
}