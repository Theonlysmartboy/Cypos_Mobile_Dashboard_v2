package com.cybene.cyposdashboard.ui.fragment.customer;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;
import com.cybene.cyposdashboard.utils.TrailingDotsLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TotalInvoicePerMonthFragment extends Fragment {

    private String TAG;
    Spinner client;
    //An ArrayList for Spinner Items
    private ArrayList<String> clients;
    //JSON Array
    private JSONArray result;
    String code;
    TextView sales,invoice;
    ConstraintLayout parentContainer;

    public TotalInvoicePerMonthFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_total_invoice_per_month, container, false);
        client = v.findViewById(R.id.client);
        clients = new ArrayList<>();
        TAG = requireActivity().getClass().getSimpleName();
        getClientData();
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                code = getCode(position);
                getInvoiceData(code);
                getSaleData(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        sales = v.findViewById(R.id.salesCount);
        invoice = v.findViewById(R.id.invoiceCount);
        parentContainer = v.findViewById(R.id.parentContainer);
        return v;
    }
    private void getClientData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_CLIENTS,
                response -> {
                    Log.d( TAG,"Client Response " + response);
                    JSONObject j;
                    try {
                        //Parsing the fetched Json String to JSON Object
                        j = new JSONObject(response);

                        //Storing the Array of JSON String to our JSON Array
                        result = j.getJSONArray("data");

                        //Calling method getClients to get the clients from the JSON Array
                        getClients(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "getClientData: ", e );
                    }
                },
                error -> {

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
                Log.e(TAG, "getClients: ", e );
            }
        }

        //Setting adapter to show the items in the spinner
        client.setAdapter(new ArrayAdapter<>(requireActivity(), android.R.layout.simple_spinner_dropdown_item, clients));
    }
    private String getCode(int position){
        String code="";
        try {
            JSONObject json = result.getJSONObject(position);
            code = json.getString("code");
        } catch (JSONException e) {
            Log.e(TAG, "getCode: ", e );
        }
        return code;
    }
    private void getSaleData(final String code) {
        final String TAG = requireActivity().getClass().getSimpleName();
        final String tag_string_req = "req_tag";
        final TrailingDotsLoader loader = new TrailingDotsLoader(getActivity());
        loader.setPrimaryColor(Color.parseColor(AppConfig.loaderPrimaryColor));
        loader.setSecondaryColor(Color.parseColor(AppConfig.loaderSecondaryColor));
        loader.setDotCount(AppConfig.loaderDotsCount);
        loader.setDotRadius(AppConfig.loaderDotsRadius);
        loader.setAnimationDuration(AppConfig.loaderAnimationDuration);
        parentContainer.addView(loader);
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_CUSTOMER_TOTAL_SALES, response -> {
            Log.d(TAG, "Response: " + response);
            parentContainer.removeView(loader);
            try {
                JSONObject jsonObject;
                JSONObject drinkObject = new JSONObject(response);
                JSONArray jsonArray = drinkObject.getJSONArray("data");
                for(int i=0; i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    sales.setText(jsonObject.getString("TotalAmount"));
                }
                parentContainer.removeView(loader);
            } catch (JSONException e) {
                Log.e(TAG, "getSaleData: ", e );
            }
        }, error -> {
            Log.e(TAG, "Request Error: " + error.getMessage());
            Toast.makeText(getActivity(), " An error has occurred "+error.getMessage(), Toast.LENGTH_LONG).show();
            parentContainer.removeView(loader);
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
    private void getInvoiceData(final String code) {
        final String TAG = requireActivity().getClass().getSimpleName();
        final String tag_string_req = "req_tag";
        final TrailingDotsLoader loader = new TrailingDotsLoader(getActivity());
        loader.setPrimaryColor(Color.parseColor(AppConfig.loaderPrimaryColor));
        loader.setSecondaryColor(Color.parseColor(AppConfig.loaderSecondaryColor));
        loader.setDotCount(AppConfig.loaderDotsCount);
        loader.setDotRadius(AppConfig.loaderDotsRadius);
        loader.setAnimationDuration(AppConfig.loaderAnimationDuration);
        parentContainer.addView(loader);
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_CUSTOMER_TOTAL_INVOICE, response -> {
            Log.d(TAG, "Response: " + response);
            parentContainer.removeView(loader);
            try {
                JSONObject jsonObject;
                JSONObject drinkObject = new JSONObject(response);
                JSONArray jsonArray = drinkObject.getJSONArray("data");
                for(int i=0; i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    invoice.setText(jsonObject.getString("TotalInvoice"));
                }
                parentContainer.removeView(loader);
            } catch (JSONException e) {
                Log.e(TAG, "getInvoiceData: ", e );
            }
        }, error -> {
            Log.e(TAG, "Request Error: " + error.getMessage());
            Toast.makeText(getActivity(), " An error has occurred "+error.getMessage(), Toast.LENGTH_LONG).show();
            parentContainer.removeView(loader);
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

}