package app.mobilecontests.onlinegcapplication.ebsoc;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.Arrays;

import app.mobilecontests.onlinegcapplication.R;
import app.mobilecontests.onlinegcapplication.classroom.GCLoginActivity;
import app.mobilecontests.onlinegcapplication.sqlite.SQLiteHelper;
import app.mobilecontests.onlinegcapplication.utils.HTTPRequestUtils;
import app.mobilecontests.onlinegcapplication.utils.SharedPreferenceManager;

public class OCLoginActivity extends AppCompatActivity {

    SQLiteHelper sqLiteHelper;
    SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oclogin);

        sqLiteHelper = new SQLiteHelper(this, "ogc.db", null, 1);
        database = sqLiteHelper.getWritableDatabase();

        if (isOCAccountSaved()) {
            ebsOCLogin(SharedPreferenceManager.getString(getApplicationContext(), "ebsoc_id"),
                    SharedPreferenceManager.getString(getApplicationContext(), "ebsoc_pw"), true);
        }

        EditText idTextField = findViewById(R.id.id_textfield);
        EditText pwTextField = findViewById(R.id.pw_textfield);

        findViewById(R.id.signin_btn).setOnClickListener(v -> {
            if (idTextField.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                return;
            } else if (pwTextField.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                return;
            }

            if (!hasNetworkConnection())
                alertNetworkConnection();
            else
                ebsOCLogin(idTextField.getText().toString(), pwTextField.getText().toString(), false);
        });

        findViewById(R.id.back_btn).setOnClickListener(v -> finish());
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

    private boolean isOCAccountSaved() {
        return !(SharedPreferenceManager.getString(getApplicationContext(), "ebsoc_id").equals("")
                && SharedPreferenceManager.getString(getApplicationContext(), "ebsoc_pw").equals(""));
    }

    private void ebsOCLogin(String id, String pw, Boolean isAutoLogin) {
        final Boolean[] isLoginSuccessful = {false};
        Thread getOCThread = new Thread() {
            @Override
            public void run() {
                OCMember ocMember = new OCMember();
                Exception loginException = ocMember.setOCMember(id, pw);
                if (loginException != null && loginException.getMessage() != null) {
                    Toast.makeText(getApplicationContext(), "Failed to login - " + (loginException.getMessage().contains("404") ? "Incorrect ID or Password" : loginException.getMessage()) + ".", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        HTTPRequestUtils httpRequestUtils = new HTTPRequestUtils();
                        OCLearning ocLearning = new OCLearning(new JSONObject(httpRequestUtils.GET(String.format(OCInfo.LEARNING_API_URL.getValue(), ocMember.getOCSchool().getHostName()), ocMember.getLoginCookie(), (String[]) null).body()));
                        System.out.println("Lecture num" + ocLearning.getLearningCount() + "\n");
                        for (int i = 0; i < ocLearning.getLearningCount(); i++) {
                            System.out.println((i + 1) +
                                    ", class Name: " + ocLearning.getLearningClassNm(i) +
                                    ", lecture Name: " + ocLearning.getLearningLsnCreaseNm(i) +
                                    ", class Name: " + ocLearning.getLearningLsnNm(i) +
                                    ", " + ocLearning.getLearningRtPgsRt(i) + "%");
                        }
                        isLoginSuccessful[0] = true;
                        System.out.println(ocMember);

                        boolean isOCExist = false, isOCOverride = false;
                        byte[] existedAccountInfoBlob = null, existedLecturesBlob = null;

                        Gson gson = new Gson();
                        Cursor cursor = database.query("ebs_classroom", new String[]{"account_info", "lectures"}, null, null, null, null, null);
                        if(cursor.moveToNext()) {
                            byte[] accountInfoBlob = cursor.getBlob(0);
                            byte[] lecturesBlob = cursor.getBlob(1);

                            String lecturesJson = new String(lecturesBlob);
                            OCLearning existedOCLearning = gson.fromJson(lecturesJson, new TypeToken<OCLearning>() {
                            }.getType());

                            isOCExist = true;
                            if (existedOCLearning.getLearningCount() != ocLearning.getLearningCount()) {
                                existedAccountInfoBlob = accountInfoBlob;
                                existedLecturesBlob = lecturesBlob;
                                isOCOverride = true;
                            }
                        }
                        cursor.close();

                        if (isOCExist) {
                            if (isOCOverride) {
                                database.delete("ebs_classroom", "account_info=? AND lectures=?", new String[]{Arrays.toString(existedAccountInfoBlob), Arrays.toString(existedLecturesBlob)});

                                ContentValues contentValues = new ContentValues();
                                contentValues.put("account_info", gson.toJson(ocMember).getBytes());
                                contentValues.put("lectures", gson.toJson(ocLearning).getBytes());

                                database.insert("ebs_classroom", null, contentValues);
                            }
                        } else {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put("account_info", gson.toJson(ocMember).getBytes());
                            contentValues.put("lectures", gson.toJson(ocLearning).getBytes());

                            database.insert("ebs_classroom", null, contentValues);
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), e.getClass().getCanonicalName() + ": " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            }
        };
        getOCThread.start();

        try {
            getOCThread.join();
        } catch (InterruptedException ignored) {
        }
        if (isLoginSuccessful[0]) {
            if(!isOCAccountSaved()) {
                SharedPreferenceManager.addString(getApplicationContext(), "ebsoc_id", id);
                SharedPreferenceManager.addString(getApplicationContext(), "ebsoc_pw", pw);
            }

            Toast.makeText(getApplicationContext(), isAutoLogin ? "EBSOC auto login successful!" : "EBSOC login successful!", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(OCLoginActivity.this, GCLoginActivity.class);
            startActivity(intent);
        }
    }
}
