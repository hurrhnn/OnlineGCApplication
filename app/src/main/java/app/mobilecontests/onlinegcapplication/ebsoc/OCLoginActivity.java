package app.mobilecontests.onlinegcapplication.ebsoc;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import app.mobilecontests.onlinegcapplication.R;
import app.mobilecontests.onlinegcapplication.classroom.GCLoginActivity;

public class OCLoginActivity extends AppCompatActivity {

    EditText idTextField;
    EditText pwTextField;
    Button loginBtn;
    ImageButton backBtn;

    String memberId;
    String memberPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oclogin);

        idTextField = findViewById(R.id.id_textfield);
        pwTextField = findViewById(R.id.pw_textfield);
        loginBtn = findViewById(R.id.signin_btn);
        backBtn = findViewById(R.id.back_btn);

        loginBtn.setOnClickListener(v -> {
            if(idTextField.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                return;
            }
            if(pwTextField.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                return;
            }

            memberId = idTextField.getText().toString();
            memberPw = pwTextField.getText().toString();

            if (!NetworkConnection()) {
                NotConnected_showAlert();
            } else {
                new getOC().start();
            }

            Intent intent = new Intent(OCLoginActivity.this, GCLoginActivity.class);
            startActivity(intent);
        });

        backBtn.setOnClickListener(v -> finish());
    }

    private void NotConnected_showAlert() //네트워크 연결 오류 시 어플리케이션 종료
    {
        Toast.makeText(getApplicationContext(), "네트워크 연결 오류", Toast.LENGTH_LONG).show();
        finish();
    }

    private boolean NetworkConnection() { //네트워크 연결 확인하는 코드
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

    private class getOC extends Thread {
        @Override
        public void run() {
            OCMember ocMember = new OCMember();
            Exception loginException = ocMember.setOCMember(memberId, memberPw);
            if (loginException != null) {
                System.out.println("Failed to login - " + (loginException.getMessage().contains("404") ? "Incorrect ID or Password" : loginException.getMessage()) + ".");
            }
            HTTPRequestUtils httpRequestUtils = new HTTPRequestUtils();
            OCLearning ocLearning = null;
            try {
                ocLearning = new OCLearning(new JSONObject(httpRequestUtils.GET(String.format(OCInfo.LEARNING_API_URL.getValue(), ocMember.getOcMemberSchool().getHostName()), ocMember.getLoginCookie(), (String[]) null).body()));
            } catch (Exception e) {
                e.printStackTrace();
            }
            System.out.println("Lecture num" + ocLearning.getLearningCount() + "\n");
            for (int i = 0; i < ocLearning.getLearningCount(); i++) {
                try {
                    System.out.println((i + 1) +
                            ", class Name: " + ocLearning.getLearningClassNm(i) +
                            ", lecture Name: " + ocLearning.getLearningLsnCreaseNm(i) +
                            ", class Name: " + ocLearning.getLearningLsnNm(i) +
                            ", " + ocLearning.getLearningRtPgsRt(i) + "%");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}