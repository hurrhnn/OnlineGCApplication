package app.mobilecontests.onlinegcapplication.schedule;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
// implements AdapterView.OnItemClickListener

public class SchoolSchedule extends SchoolInfo{
    String ATPT_OFCDC_SC_CODE = citynum; //교육청코드
    String SD_SCHUL_CODE= schoolnum; //학교코드
    //String AY = "";
    //String SEM = "1"; //학기
    String GRADE = "2"; //학년
    String CLASS_NM = "2"; //반
    //	https://open.neis.go.kr/hub/hisTimetable?Type=json&pIndex=1&pSize=100&ATPT_OFCDC_SC_CODE=T10&SD_SCHUL_CODE=7003713
    String pSize = "100";
    String pIndex = "1";
    String KEY = "e3dffc501f6b42c88900e6ac3139ca60";
    //	https://open.neis.go.kr/hub/hisTimetable?ATPT_OFCDC_SC_CODE=T10&SD_SCHUL_CODE=7003713&TI_FROM_YMD=20190401&TI_TO_YMD=20190405
//    String[][] ITRT_CNTNT=  new String[7][7];//과목명
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
    SimpleDateFormat yearset = new SimpleDateFormat("yyyy",Locale.KOREA);
    String AY = yearset.format(date);
    String today = sdf.format(date);
    String TI_FROM_YMD =sdf.format(date);
    String TI_TO_YMD =sdf.format(date);
    String url = "https://open.neis.go.kr/hub/hisTimetable?KEY="+KEY+"&Type=json&pIndex=" + pIndex + "&pSize=" + pSize + "&ATPT_OFCDC_SC_CODE=" + ATPT_OFCDC_SC_CODE + "&SD_SCHUL_CODE=" + SD_SCHUL_CODE + "&AY=" + AY + "&GRADE=" + GRADE + "&CLASS_NM=" + CLASS_NM+"&TI_FROM_YMD="+TI_FROM_YMD+"&TI_TO_YMD="+TI_TO_YMD;
    Document doc = null;
    int num = 1;


    public class getData extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            try {
               if(num==1){

               }
                doc = Jsoup.connect(url).get(); //html코드까지
                String Data = doc.text(); //text만
                JSONObject jsonObject ;


                try {
                    jsonObject = new JSONObject(Data);
                    JSONArray jsonArray = jsonObject.getJSONArray("hisTimetable");
                    JSONObject tmpObject = jsonArray.getJSONObject(1);
                    JSONArray rowArray = tmpObject.getJSONArray("row");
                    ArrayList<String> tmp = new ArrayList<>();



                    for(int i = 0; i<rowArray.length(); i++){
                        JSONObject jsonElement = (JSONObject) rowArray.get(i);
                        tmp.add((String) jsonElement.get("ITRT_CNTNT"));
                        }
                    System.out.println(today);
                    for(int j = 0; j< tmp.size(); j++){ //0~7까지 해야함 왠지는 모르겠음
                        System.out.println(tmp.get(j));
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
            return citynum;
        }

    }
}
