package com.cybene.cyposdashboard.ui.sales;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.ApiClient;
import com.cybene.cyposdashboard.utils.data.sales.DailySales;
import com.cybene.cyposdashboard.utils.interfaces.sales.DailySalesInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class DailySalesFragment extends Fragment implements OnChartGestureListener {
    private BarChart chart;
    public DailySalesFragment() {
        // Required empty public constructor
    }

    public static DailySalesFragment newInstance() {
        return new DailySalesFragment();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_daily_sales, container, false);
        chart = v.findViewById(R.id.chart);
        getDailySales();
        // Inflate the layout for this fragment
        return v;
    }
    @Override
    public void onChartGestureStart(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "START");
    }

    @Override
    public void onChartGestureEnd(MotionEvent me, ChartTouchListener.ChartGesture lastPerformedGesture) {
        Log.i("Gesture", "END");
        chart.highlightValues(null);
    }

    @Override
    public void onChartLongPressed(MotionEvent me) {
        Log.i("LongPress", "Chart long pressed.");
    }

    @Override
    public void onChartDoubleTapped(MotionEvent me) {
        Log.i("DoubleTap", "Chart double-tapped.");
    }

    @Override
    public void onChartSingleTapped(MotionEvent me) {
        Log.i("SingleTap", "Chart single-tapped.");
    }

    @Override
    public void onChartFling(MotionEvent me1, MotionEvent me2, float velocityX, float velocityY) {
        Log.i("Fling", "Chart fling. VelocityX: " + velocityX + ", VelocityY: " + velocityY);
    }

    @Override
    public void onChartScale(MotionEvent me, float scaleX, float scaleY) {
        Log.i("Scale / Zoom", "ScaleX: " + scaleX + ", ScaleY: " + scaleY);
    }

    @Override
    public void onChartTranslate(MotionEvent me, float dX, float dY) {
        Log.i("Translate / Move", "dX: " + dX + ", dY: " + dY);
    }

    private void getDailySales() {
        Call<List<DailySales>> call = ApiClient.getApiClient().create(DailySalesInterface.class).getSalesInfo();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<List<DailySales>> call, @NotNull Response<List<DailySales>> response) {
                //check if the response body is null
                if (response.body() != null) {
                    List<BarEntry> barEntries = new ArrayList<>();
                    for (DailySales dailySales : response.body()) {
                        barEntries.add(new BarEntry(dailySales.getDate(), dailySales.getTotal()));
                    }
                    BarDataSet dataSet = new BarDataSet(barEntries, "Daily Sales");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    BarData data = new BarData(dataSet);
                    //data.setBarWidth(10f);
                    chart.setVisibility(View.VISIBLE);
                    chart.animateXY(2000, 2000);
                    chart.setData(data);
                    chart.setFitBars(true);
                    Description description = new Description();
                    description.setText("Sales per day");
                    chart.setDescription(description);
                    chart.invalidate();
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setLabelCount(31, true);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    //xAxis.setTypeface();
                    xAxis.setDrawGridLines(false);
                    //xAxis.setValueFormatter(new MonthlyCustomerSalesFragment.MyFormatter());
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<DailySales>> call, @NonNull Throwable t) {

            }
        });

    }
}