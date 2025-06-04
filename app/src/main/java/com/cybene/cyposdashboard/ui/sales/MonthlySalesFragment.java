package com.cybene.cyposdashboard.ui.sales;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.ApiClient;
import com.cybene.cyposdashboard.utils.data.sales.MonthlySales;
import com.cybene.cyposdashboard.utils.interfaces.sales.MonthlySalesInterface;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.listener.ChartTouchListener;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthlySalesFragment extends Fragment implements OnChartGestureListener {
    private BarChart chart;

    public MonthlySalesFragment() {
        // Required empty public constructor
    }

    public static MonthlySalesFragment newInstance() {
       return new MonthlySalesFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_monthly_sales, container, false);
        chart = v.findViewById(R.id.chart);
        getMonthlySales();
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

    private void getMonthlySales() {
        Call<List<MonthlySales>> call = ApiClient.getApiClient().create(MonthlySalesInterface.class).getSalesInfo();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<List<MonthlySales>> call, @NotNull Response<List<MonthlySales>> response) {
                //check if the response body is null
                if (response.body() != null) {
                    List<BarEntry> barEntries = new ArrayList<>();
                    for (MonthlySales monthlySales : response.body()) {
                        barEntries.add(new BarEntry(monthlySales.getMonth(), monthlySales.getTotal()));
                    }
                    BarDataSet dataSet = new BarDataSet(barEntries, "Monthly Sales");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    BarData data = new BarData(dataSet);
                    //data.setBarWidth(10f);
                    chart.setVisibility(View.VISIBLE);
                    chart.animateXY(2000, 2000);
                    chart.setData(data);
                    chart.setFitBars(true);
                    Description description = new Description();
                    description.setText("Sales per month");
                    chart.setDescription(description);
                    chart.invalidate();
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    //xAxis.setTypeface();
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter(new MyFormatter());
                    xAxis.setGranularity(1f);
                    xAxis.setGranularityEnabled(true);
                }

            }

            @Override
            public void onFailure(@NonNull Call<List<MonthlySales>> call, @NonNull Throwable t) {

            }
        });

    }
    public static class MyFormatter extends ValueFormatter {
        public String getAxisLabel(float value, AxisBase axis) {
            if (Math.round(value) == 1) {
                return "JAN"; //make it a string and return
            } else  if (Math.round(value) == 2) {
                return "FEB"; //make it a string and return
            } else if (Math.round(value) == 3) {
                return "MAR"; //make it a string and return
            } else  if (Math.round(value) == 4) {
                return "APR"; //make it a string and return
            } else  if (Math.round(value) == 5) {
                return "MAY"; //make it a string and return
            } else  if (Math.round(value) == 6) {
                return "JUN"; //make it a string and return
            } else  if (Math.round(value) == 7) {
                return "JUL"; //make it a string and return
            } else  if (Math.round(value) == 8) {
                return "AUG"; //make it a string and return
            } else  if (Math.round(value) == 9) {
                return "SEP"; //make it a string and return
            } else  if (Math.round(value) == 10) {
                return "OCT"; //make it a string and return
            } else  if (Math.round(value) == 11) {
                return "NOV"; //make it a string and return
            } else  if (Math.round(value) == 12) {
                return "DEC"; //make it a string and return
            } else {
                return ""; // return empty for other values where you don't want to print anything on the X Axis
            }
        }
    }
}