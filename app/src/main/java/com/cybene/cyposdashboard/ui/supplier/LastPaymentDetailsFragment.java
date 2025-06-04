package com.cybene.cyposdashboard.ui.supplier;

import android.graphics.Color;
import android.os.Bundle;

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

public class LastPaymentDetailsFragment extends Fragment {

    private String TAG;
    Spinner client;
    //An ArrayList for Spinner Items
    private ArrayList<String> clients;
    //JSON Array
    private JSONArray result;
    String code;
    private TableLayout items;
    TextView rno, rDate, cCode, aCode, amType,amount, rAmount, chqNo, chqDate,remark, isRecRev,
            isAssign,created, createdDy,modified, modifiedBy, srNo;
    View tableRow;
    LinearLayout parentContainer;

    public LastPaymentDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_last_payment_details, container, false);
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
                response -> {
                    Log.d( TAG,"Client Response " + response);
                    JSONObject j;
                    try {
                        //Parsing the fetched Json String to JSON Object
                        j = new JSONObject(response);

                        //Storing the Array of JSON String to our JSON Array
                        result = j.getJSONArray("data");

                        //Calling method getClients to get the clients from the JSON Array
                        getSuppliers(result);
                    } catch (JSONException e) {
                        Log.e(TAG, "getSupplierData: ", e );
                    }
                },
                error -> {

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
                Log.e(TAG, "getSuppliers: ", e );
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

    private void getData(final String code) {
        final String TAG = requireActivity().getClass().getSimpleName();
        final String tag_string_req = "req_tag";
        final TrailingDotsLoader loader = new TrailingDotsLoader(getActivity());
        loader.setPrimaryColor(Color.parseColor(AppConfig.loaderPrimaryColor));
        loader.setSecondaryColor(Color.parseColor(AppConfig.loaderSecondaryColor));
        loader.setDotCount(AppConfig.loaderDotsCount);
        loader.setDotRadius(AppConfig.loaderDotsRadius);
        loader.setAnimationDuration(AppConfig.loaderAnimationDuration);
        parentContainer.addView(loader);
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SUPPLIER_LAST_PAYMENT_DETAILS, response -> {
            Log.d(TAG, "Response: " + response);
            parentContainer.removeView(loader);
            try {
                JSONObject jsonObject;
                JSONObject drinkObject = new JSONObject(response);
                JSONArray jsonArray = drinkObject.getJSONArray("data");
                for(int i=0; i<jsonArray.length();i++){
                    jsonObject = jsonArray.getJSONObject(i);
                    tableRow = LayoutInflater.from(getActivity()).inflate(R.layout.table_item1, null, false);
                    //table components
                    rno = tableRow.findViewById(R.id.rNo);
                    rDate = tableRow.findViewById(R.id.rDate);
                    cCode = tableRow.findViewById(R.id.cCode);
                    aCode = tableRow.findViewById(R.id.aCode);
                    amType = tableRow.findViewById(R.id.amountType);
                    amount = tableRow.findViewById(R.id.amount);
                    rAmount = tableRow.findViewById(R.id.rAmount);
                    chqNo = tableRow.findViewById(R.id.chqNo);
                    chqDate = tableRow.findViewById(R.id.chqDate);
                    remark = tableRow.findViewById(R.id.remark);
                    isRecRev = tableRow.findViewById(R.id.isRecREv);
                    isAssign = tableRow.findViewById(R.id.isAssign);
                    created = tableRow.findViewById(R.id.created);
                    createdDy = tableRow.findViewById(R.id.createdBy);
                    modified = tableRow.findViewById(R.id.modified);
                    modifiedBy = tableRow.findViewById(R.id.modifiedBy);
                    srNo = tableRow.findViewById(R.id.srNo);
                    rno.setText(jsonObject.getString("PaymentNo"));
                    rDate.setText(jsonObject.getString("PaymentDate"));
                    cCode.setText(jsonObject.getString("SupplierCode"));
                    aCode.setText(jsonObject.getString("AccCode"));
                    amType.setText(jsonObject.getString("AmountType"));
                    amount.setText(jsonObject.getString("Amount"));
                    rAmount.setText(jsonObject.getString("RemainingAmount"));
                    chqNo.setText(jsonObject.getString("ChqNo"));
                    chqDate.setText(jsonObject.getString("Remark"));
                    remark.setText(jsonObject.getString("ChqDate"));
                    isRecRev.setText(jsonObject.getString("isPayRev"));
                    isAssign.setText(jsonObject.getString("isAssign"));
                    created.setText(jsonObject.getString("Created"));
                    createdDy.setText(jsonObject.getString("CreatedBy"));
                    modified.setText(jsonObject.getString("Modified"));
                    modifiedBy.setText(jsonObject.getString("ModifiedBy"));
                    srNo.setText(jsonObject.getString("SrNo"));
                    items.addView(tableRow);
                }
                parentContainer.removeView(loader);
            } catch (JSONException e) {
                Log.e(TAG, "getData: ", e );
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
    public void removeRows(){
        int numRows = items.getChildCount();
        for(int i = 1; i<numRows; i++) {
            View child = items.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }
}