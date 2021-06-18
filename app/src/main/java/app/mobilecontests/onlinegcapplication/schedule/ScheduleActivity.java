package app.mobilecontests.onlinegcapplication.schedule;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import app.mobilecontests.onlinegcapplication.R;
import app.mobilecontests.onlinegcapplication.ebsoc.OCLoginActivity;
import app.mobilecontests.onlinegcapplication.ebsoc.OCMember;
import app.mobilecontests.onlinegcapplication.sqlite.SQLiteHelper;

public class ScheduleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "ogc.db", null, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        String schoolName = ""; // EditText needed.

        Gson gson = new Gson();
        Cursor cursor = database.query("ebs_classroom", new String[]{"account_info", "lectures"}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            byte[] accountInfoBlob = cursor.getBlob(0);

            String accountInfoBlobJson = new String(accountInfoBlob);
            OCMember ocMember = gson.fromJson(accountInfoBlobJson, new TypeToken<OCMember>() {
            }.getType());
            schoolName = ocMember.getOCSchool().getSchoolCodeNm().split(" ")[2];
        }
        cursor.close();

        Thread thread = new SchoolSchedule(schoolName, this);
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Intent intent = new Intent(ScheduleActivity.this, OCLoginActivity.class);
        startActivity(intent);
    }
}