package com.cybene.cyposdashboard.ui.customer;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.RotatingCircularDotsLoader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.ui.sales.MonthlySalesFragment;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MonthlyCustomerPurchaseFragment extends Fragment  implements OnChartGestureListener {
    private String TAG;
    Spinner client;
    //An ArrayList for Spinner Items
    private ArrayList<String> clients;
    //JSON Array
    private JSONArray result;
    String code;
    private BarChart chart;

    public MonthlyCustomerPurchaseFragment() {
        // Required empty public constructor
    }

    public static  MonthlyCustomerPurchaseFragment newInstance(){
        return new MonthlyCustomerPurchaseFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_monthly_customer_purchase, container, false);
        client = v.findViewById(R.id.client);
        clients = new ArrayList<>();
        TAG = requireActivity().getClass().getSimpleName();
        getClientData();
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                code = getCode(position);
                getData(code);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        chart = v.findViewById(R.id.chart);
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
    private void getClientData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_CLIENTS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d( TAG,"Client Response " + response);
                        JSONObject j;
                        try {
                            //Parsing the fetched Json String to JSON Object
                            j = new JSONObject(response);

                            //Storing the Array of JSON String to our JSON Array
                            result = j.getJSONArray("data");

                            //Calling method getclients to get the clients from the JSON Array
                            getClients(result);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
        //Creating a request queue
        RequestQueue requestQueue = Volley.newRequestQueue(requireActivity());
        //Adding request to the queue
        requestQueue.add(stringRequest);
    }
    private void getClients(JSONArray j){
        //Traversing through all the items in the json array
        for(int i=0;i<j.length();i++){
            try {
                //Getting json object
                JSONObject json = j.getJSONObject(i);

                //Adding the name of the student to array list
                clients.add(json.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        //Setting adapter to show the items in the spinner
        client.setAdapter(new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item, clients));
    }

    //Method to get zone name of a particular position
    private String getName(int position){
        String name="";
        try {
            //Getting object of given index
            JSONObject json = result.getJSONObject(position);

            //Fetching name from that object
            name = json.getString("name");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //Returning the name
        return name;
    }

    //Doing the same with this method as we did with getName()
    private String getCode(int position){
        String code="";
        try {
            JSONObject json = result.getJSONObject(position);
            code = json.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return code;
    }
    private void getData(final String code) {
        final String TAG = requireActivity().getClass().getSimpleName();
        final String tag_string_req = "req_tag";
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_CUSTOMER_MONTHLY_PURCHASE, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);
                try {
                    List<BarEntry> barEntries = new ArrayList<>();
                    JSONObject jsonObject;
                    JSONObject drinkObject = new JSONObject(response);
                    JSONArray jsonArray = drinkObject.getJSONArray("data");
                    for(int i=0; i<jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        barEntries.add(new BarEntry(jsonObject.getInt("Month"), jsonObject.getInt("Sales")));
                    }
                    BarDataSet dataSet = new BarDataSet(barEntries, "Customer Monthly Sales");
                    dataSet.setColors(ColorTemplate.COLORFUL_COLORS);
                    BarData data = new BarData(dataSet);
                    //data.setBarWidth(10f);
                    chart.setVisibility(View.VISIBLE);
                    chart.animateXY(2000, 2000);
                    chart.setData(data);
                    chart.setFitBars(true);
                    Description description = new Description();
                    description.setText("Customer Purchase per month");
                    chart.setDescription(description);
                    chart.invalidate();
                    XAxis xAxis = chart.getXAxis();
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    //xAxis.setTypeface();
                    xAxis.setDrawGridLines(false);
                    xAxis.setValueFormatter(new MonthlySalesFragment.MyFormatter());
                    xAxis.setGranularity(1f);
                    xAxis.setGranularityEnabled(true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Request Error: " + error.getMessage());
                Toast.makeText(getActivity(), " An error has occurred "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting params to register url
                Map<String, String> params = new HashMap<>();
                params.put("client", code);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
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