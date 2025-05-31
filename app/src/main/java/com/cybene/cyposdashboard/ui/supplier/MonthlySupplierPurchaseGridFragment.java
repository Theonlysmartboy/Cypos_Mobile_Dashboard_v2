package com.cybene.cyposdashboard.ui.supplier;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.RotatingCircularDotsLoader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MonthlySupplierPurchaseGridFragment extends Fragment {

    private String TAG;
    Spinner client;
    //An ArrayList for Spinner Items
    private ArrayList<String> clients;
    //JSON Array
    private JSONArray result;
    String code;
    private TableLayout items;
    TextView id,month,sales;
    View tableRow;
    LinearLayout parentContainer;
    int idNo;

    public MonthlySupplierPurchaseGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_monthly_supplier_purchase_grid, container, false);
        client = v.findViewById(R.id.client);
        clients = new ArrayList<>();
        TAG = requireActivity().getClass().getSimpleName();
        getSupplierData();
        client.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                code = getCode(position);
                removeRows();
                getData(code);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        items = v.findViewById(R.id.container);
        parentContainer = v.findViewById(R.id.parentContainer);
        return v;
    }
    private void getSupplierData(){
        //Creating a string request
        StringRequest stringRequest = new StringRequest(Request.Method.GET, AppConfig.URL_SUPPLIERS,
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
                            getSuppliers(result);
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
    private void getSuppliers(JSONArray j){
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
        final RotatingCircularDotsLoader loader = new RotatingCircularDotsLoader(requireActivity(),
                20, 60, ContextCompat.getColor(requireActivity(), R.color.design_default_color_primary));
        loader.setAnimDuration(3000);
        parentContainer.addView(loader);
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SUPPLIER_MONTHLY_PURCHASE_GRID, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);
                parentContainer.removeView(loader);
                try {
                    idNo = 1;
                    JSONObject jsonObject;
                    JSONObject drinkObject = new JSONObject(response);
                    JSONArray jsonArray = drinkObject.getJSONArray("data");
                    for(int i=0; i<jsonArray.length();i++){
                        jsonObject = jsonArray.getJSONObject(i);
                        tableRow = LayoutInflater.from(getActivity()).inflate(R.layout.table_item, null, false);
                        //table components
                        id = tableRow.findViewById(R.id.idNo);
                        month = tableRow.findViewById(R.id.month);
                        sales = tableRow.findViewById(R.id.sale);
                        id.setText(String.valueOf(idNo++));
                        month.setText(jsonObject.getString("Month"));
                        sales.setText(jsonObject.getString("Purchase"));
                        items.addView(tableRow);
                    }
                    parentContainer.removeView(loader);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new com.android.volley.Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Request Error: " + error.getMessage());
                Toast.makeText(getActivity(), " An error has occurred "+error.getMessage(), Toast.LENGTH_LONG).show();
                parentContainer.removeView(loader);
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

    public void removeRows(){
        int numrows = items.getChildCount();
        for(int i = 1; i<numrows; i++) {
            View child = items.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }
}