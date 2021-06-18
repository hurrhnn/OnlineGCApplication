package app.mobilecontests.onlinegcapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import java.util.ArrayList;
import java.util.List;

public class DashboardActivity extends AppCompatActivity {

    private ArrayList<DashboardProperty> mArrayList;
    private DashboardAdapter mAdapter;
    private int count = -1;

    private LineChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        chart = findViewById(R.id.chart);

        ArrayList<Entry> values = new ArrayList<>();

        for (int i = 0; i < 10; i++) {

            float val = (float) (Math.random() * 10);
            values.add(new Entry(i, val));
        }

        LineDataSet set1;
        set1 = new LineDataSet(values, "DataSet 1");

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1); // add the data sets

        // create a data object with the data sets
        LineData data = new LineData(dataSets);

        // black lines and points
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);

        // set data
        chart.setData(data);


        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.class_list);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);


        mArrayList = new ArrayList<>();

        mAdapter = new DashboardAdapter(mArrayList);
        mRecyclerView.setAdapter(mAdapter);


        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
                mLinearLayoutManager.getOrientation());
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        // 여기서 데이터 받아서 ArrayList에 값 넣어주면 됩니다.
        DashboardProperty ListData = new DashboardProperty("1",
                                                        "강의 시작 : 2021-08-31",
                                                        "강의 종료 : 2021-09-03",
                                                        "2학년 : 영어A1(1반~12반)",
                                                        "영어A 3과 강의 | 15강 Lesson 3. We Are All Connected(6)",
                                                        "OC",
                                                        50,
                                                        "50%");
        mArrayList.add(ListData);
        ListData = new DashboardProperty("2",
                "강의 시작 : 2021-08-31",
                "강의 종료 : 2021-09-03",
                "2학년 : 물리학1 (1반~6반)",
                "1학기 중간고사 범위 | 1차시",
                "OC",
                100,
                "100%");
        mArrayList.add(ListData);
        mArrayList.add(ListData);
        mArrayList.add(ListData);
        mArrayList.add(ListData);
        mArrayList.add(ListData);
        mArrayList.add(ListData);
        mAdapter.notifyDataSetChanged();
    }
}