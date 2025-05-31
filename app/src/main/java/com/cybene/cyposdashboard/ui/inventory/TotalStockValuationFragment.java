package com.cybene.cyposdashboard.ui.inventory;

import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.agrawalsuneet.dotsloader.loaders.RotatingCircularDotsLoader;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TotalStockValuationFragment extends Fragment {

    private TableLayout items;
    TextView id,month,sales;
    View tableRow;
    int idNo;
    LinearLayout parentContainer;

    public TotalStockValuationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_total_stock_valuation, container, false);
        items = v.findViewById(R.id.container);
        parentContainer = v.findViewById(R.id.parentContainer);
        getData();
        return v;
    }
    private void getData() {
        final String TAG = getActivity().getClass().getSimpleName();
        String tag_string_req = "req_tag";
        final RotatingCircularDotsLoader loader = new RotatingCircularDotsLoader(getActivity(),
                20, 60, ContextCompat.getColor(getActivity(), R.color.red));
        loader.setAnimDuration(3000);
        parentContainer.addView(loader);
        StringRequest strReq = new StringRequest(Request.Method.GET, AppConfig.URL_TOTAL_STOCK, new com.android.volley.Response.Listener<String>() {
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
                        month.setText(jsonObject.getString("TYPE"));
                        sales.setText(jsonObject.getString("TOTALVALUATION"));
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
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}