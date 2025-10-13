package com.cybene.cyposdashboard.ui.purchase;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.sales.MonthlySalesFragment;
import com.cybene.cyposdashboard.utils.ApiClient;
import com.cybene.cyposdashboard.utils.data.purchase.MonthlyPurchase;
import com.cybene.cyposdashboard.utils.interfaces.purchase.MonthlyPurchaseInterface;
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

public class MonthlyPurchaseChartFragment extends Fragment implements OnChartGestureListener {
    private BarChart chart;

    public MonthlyPurchaseChartFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_monthly_purchase_chart, container, false);
        chart = v.findViewById(R.id.chart);
        getSales();
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

    private void getSales() {
        Call<List<MonthlyPurchase>> call = ApiClient.getApiClient().create(MonthlyPurchaseInterface.class).getSalesInfo();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<List<MonthlyPurchase>> call, @NotNull Response<List<MonthlyPurchase>> response) {
                //check if the response body is null
                if (response.body() != null) {
                    List<BarEntry> barEntries = new ArrayList<>();
                    for (MonthlyPurchase dailySales : response.body()) {
                        barEntries.add(new BarEntry(dailySales.getMonth(), dailySales.getTotal()));
                    }
                    BarDataSet dataSet = new BarDataSet(barEntries, "Monthly Purchases");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    BarData data = new BarData(dataSet);
                    //data.setBarWidth(10f);
                    chart.setVisibility(View.VISIBLE);
                    chart.animateXY(2000, 2000);
                    chart.setData(data);
                    chart.setFitBars(true);
                    Description description = new Description();
                    description.setText("Purchases per month");
                    chart.setDescription(description);
                    chart.invalidate();
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    //xAxis.setTypeface();
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter(new MonthlySalesFragment.MyFormatter());
                    xAxis.setGranularity(1f);
                    xAxis.setGranularityEnabled(true);
                }

            }

            @Override
            public void onFailure(@NotNull Call<List<MonthlyPurchase>> call, @NonNull Throwable t) {

            }
        });

    }
}