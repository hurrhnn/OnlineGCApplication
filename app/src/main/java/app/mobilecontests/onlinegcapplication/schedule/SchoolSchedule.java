package app.mobilecontests.onlinegcapplication.schedule;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import app.mobilecontests.onlinegcapplication.sqlite.SQLiteHelper;
import app.mobilecontests.onlinegcapplication.utils.HTTPRequestUtils;

public class SchoolSchedule extends Thread {

    final String schoolNm;
    final Context context;
    public SchoolSchedule(String schoolNm, Context context) {
        this.schoolNm = schoolNm;
        this.context = context;
    }

    public void run() {
        try {
            SQLiteHelper sqLiteHelper = new SQLiteHelper(context, "ogc.db", null, 1);
            SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

            JSONObject responseData = (JSONObject) new JSONObject(new HTTPRequestUtils().GET(SchoolInfo.BASE_URL.getValue() + "schoolInfo?KEY=" + SchoolInfo.AUTH_KEY.getValue() + "&TYPE=json&SCHUL_NM=" + schoolNm, new HashMap<>(), (String[]) null).body()).getJSONArray("schoolInfo").getJSONObject(1).getJSONArray("row").get(0);
            String cityNum = ((String) responseData.get("ATPT_OFCDC_SC_CODE"));
            String schoolNum = ((String) responseData.get("SD_SCHUL_CODE"));

            String GRADE = "2"; // 학년
            String CLASS_NM = "2"; // 반
            //	https://open.neis.go.kr/hub/hisTimetable?Type=json&pIndex=1&pSize=100&ATPT_OFCDC_SC_CODE=T10&SD_SCHUL_CODE=7003713
            String pSize = "100";
            String pIndex = "1";
            //	https://open.neis.go.kr/hub/hisTimetable?ATPT_OFCDC_SC_CODE=T10&SD_SCHUL_CODE=7003713&TI_FROM_YMD=20190401&TI_TO_YMD=20190405

            String AY = new SimpleDateFormat("yyyy", Locale.KOREA).format(new Date());

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
            String TI_FROM_YMD = simpleDateFormat.format(new Date());
            String TI_TO_YMD = simpleDateFormat.format(new Date());

            responseData = new JSONObject(new HTTPRequestUtils().GET(SchoolInfo.BASE_URL.getValue() + "hisTimetable?KEY=" + SchoolInfo.AUTH_KEY.getValue() + "&Type=json&pIndex=" + pIndex + "&pSize=" + pSize + "&ATPT_OFCDC_SC_CODE=" + cityNum + "&SD_SCHUL_CODE=" + schoolNum + "&AY=" + AY + "&GRADE=" + GRADE + "&CLASS_NM=" + CLASS_NM + "&TI_FROM_YMD=" + TI_FROM_YMD + "&TI_TO_YMD=" + TI_TO_YMD, new HashMap<>(), (String[]) null).body());
            JSONArray jsonArray = responseData.getJSONArray("hisTimetable").getJSONObject(1).getJSONArray("row");
            // System.out.println(jsonArray.toString(4));

            boolean isTimeTableExist = false;

            Gson gson = new Gson();
            Cursor cursor = database.query("school_timetable", new String[]{"date", "timetable"}, null, null, null, null, null);
            while(cursor.moveToNext()) {
                String date = cursor.getString(0);
                if(date.equals(simpleDateFormat.format(new Date()))) {
                    isTimeTableExist = true;
                    break;
                }
            }
            cursor.close();

            if (!isTimeTableExist) {
                ContentValues contentValues = new ContentValues();
                contentValues.put("date", simpleDateFormat.format(new Date()));
                contentValues.put("timetable", gson.toJson(jsonArray).getBytes());

                database.insert("school_timetable", null, contentValues);
            }
        } catch (Exception e) { e.printStackTrace(); }
    }
}