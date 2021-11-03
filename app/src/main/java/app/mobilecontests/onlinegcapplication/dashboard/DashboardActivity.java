package app.mobilecontests.onlinegcapplication.dashboard;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.json.JSONException;

import java.util.ArrayList;

import app.mobilecontests.onlinegcapplication.R;
import app.mobilecontests.onlinegcapplication.ebsoc.OCLearning;
import app.mobilecontests.onlinegcapplication.sqlite.SQLiteHelper;

public class DashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        SQLiteHelper sqLiteHelper = new SQLiteHelper(this, "ogc.db", null, 1);
        SQLiteDatabase database = sqLiteHelper.getWritableDatabase();

        LineChart chart = findViewById(R.id.chart);
        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }

        LineDataSet lineDataSet;
        lineDataSet = new LineDataSet(values, "Example Graph");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        lineDataSet.setColor(Color.BLACK);
        lineDataSet.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);

        RecyclerView recyclerView = findViewById(R.id.class_list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<DashboardProperty> propertyArrayList = new ArrayList<>();

        DashboardAdapter mAdapter = new DashboardAdapter(propertyArrayList);
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(dividerItemDecoration);

        // 여기서 데이터 받아서 ArrayList에 값 넣어주면 됩니다.
        Gson gson = new Gson();
        Cursor cursor = database.query("ebs_classroom", new String[]{"account_info", "lectures"}, null, null, null, null, null);
        if (cursor.moveToNext()) {
            byte[] lecturesBlob = cursor.getBlob(1);
            String lecturesJson = new String(lecturesBlob);
            OCLearning lectures = gson.fromJson(lecturesJson, new TypeToken<OCLearning>() {
            }.getType());

            int cnt = 0;
            for (int i = 0; i < lectures.getLearningCount(); i++) {
                try {
                    if(lectures.getLearningRtPgsRt(i) == 100)
                        continue;

                    DashboardProperty dashboardProperty = new DashboardProperty(String.valueOf(++cnt),
                            "강의 시작: " + dateTimeReFormatter(org.joda.time.DateTime.parse(lectures.getLearningLrnBgnDt(i),
                                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss"))),
                            "강의 종료: " + dateTimeReFormatter(org.joda.time.DateTime.parse(lectures.getLearningLrnEndDt(i),
                                    DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss"))),
                            lectures.getLearningClassNm(i),
                            lectures.getLearningLsnCreaseNm(i) + " | " + lectures.getLearningClassNm(i),
                            "OC",
                            lectures.getLearningRtPgsRt(i),
                            lectures.getLearningRtPgsRt(i) + "%");

                    propertyArrayList.add(dashboardProperty);
                } catch (JSONException ignored) {
                }
            }
            mAdapter.notifyDataSetChanged();
        } else {
            Toast.makeText(getApplicationContext(), "An error occurred while loading the classroom.", Toast.LENGTH_LONG).show();
            finish();
        }
        cursor.close();
    }

    private String dateTimeReFormatter(DateTime dateTime) {
        return DateTimeFormat.forPattern("yyyy-MM-dd").print(dateTime);
    }
}