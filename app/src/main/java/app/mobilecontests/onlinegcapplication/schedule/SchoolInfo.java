package app.mobilecontests.onlinegcapplication.schedule;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Scanner;


public class SchoolInfo extends AsyncTask<String, Void, Void> {

    Scanner sc = new Scanner(System.in);
    String SCHUL_NM = "선린인터넷고등학교"; //추후에 EditText로 변환
    String KEY = "e3dffc501f6b42c88900e6ac3139ca60";
    String url = "https://open.neis.go.kr/hub/schoolInfo?KEY=" + KEY + "TYPE=json&SCHUL_NM=" + SCHUL_NM;
    String citynum;
    String schoolnum;
    Document doc;
    String Data;
    JSONArray jsonArray = null;
    JSONObject jsonObject = null;
    JSONObject tmpObject = null;
    JSONArray rowArray = null;
    JSONObject jsonElement = null;
    @Override
    protected Void doInBackground(String... strings) {
        String[] scinfo = new String[0];
        try {

            scinfo = new String[2];

            doc = Jsoup.connect(url).get();
            Data = doc.text(); //text만
            jsonObject = new JSONObject(Data);
            jsonArray = jsonObject.getJSONArray("schoolInfo");
            tmpObject = jsonArray.getJSONObject(1);
            rowArray = tmpObject.getJSONArray("row");
            jsonElement = (JSONObject) rowArray.get(1);
            citynum = ((String) jsonElement.get("ATPT_OFCDC_SC_CODE"));
            schoolnum = ((String) jsonElement.get("SD_SCHUL_CODE"));
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}