package app.mobilecontests.onlinegcapplication;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import app.mobilecontests.onlinegcapplication.ebsoc.HTTPRequestUtils;
import app.mobilecontests.onlinegcapplication.ebsoc.OCInfo;
import app.mobilecontests.onlinegcapplication.ebsoc.OCLearning;
import app.mobilecontests.onlinegcapplication.ebsoc.OCMember;

public class OCLoginActivity extends AppCompatActivity {
    String memberId;
    String memberPw;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oclogin);

        EditText id = findViewById(R.id.edit_id);
        EditText pw = findViewById(R.id.edit_pw);
        Button login = findViewById(R.id.signinbtn);

        login.setOnClickListener(v -> {
            memberId = id.getText().toString();
            memberPw = pw.getText().toString();

            if (!NetworkConnection()) {
                NotConnected_showAlert();
            } else {
                new getOC().start();
            }
        });
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