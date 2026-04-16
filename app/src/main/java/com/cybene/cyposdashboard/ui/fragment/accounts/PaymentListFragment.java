package com.cybene.cyposdashboard.ui.fragment.accounts;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;
import com.cybene.cyposdashboard.utils.TrailingDotsLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class PaymentListFragment extends Fragment {
    private TableLayout items;
    TextView id,month,sales;
    View tableRow;
    EditText from,to;
    DatePickerDialog picker;
    Button show;
    String fromVal, toVal;
    LinearLayout parentContainer;

    public PaymentListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_accounts_payment_list, container, false);
        items = v.findViewById(R.id.container);
        parentContainer = v.findViewById(R.id.parentContainer);
        from = v.findViewById(R.id.date_from);
        to = v.findViewById(R.id.date_to);
        from.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireActivity(), (view2, year2, monthOfYear, dayOfMonth) -> from.setText(year2 + "-" + (monthOfYear+1) + "-" + dayOfMonth), year, month, day);
            picker.getDatePicker().setLayoutMode(1);
            picker.show();
        });
        to = v.findViewById(R.id.date_to);
        to.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireActivity(), (view1, year1, monthOfYear, dayOfMonth) -> to.setText(year1 + "-" + (monthOfYear+1) + "-" + dayOfMonth), year, month, day);
            picker.getDatePicker().setLayoutMode(1);
            picker.show();
        });
        show = v.findViewById(R.id.show);
        show.setOnClickListener(view -> {
            fromVal = from.getText().toString().trim();
            toVal = to.getText().toString().trim();
            getData(fromVal,toVal);

        });
        return v;
    }
    private void getData(final String fromVal, final String toVal) {
        final String TAG = requireActivity().getClass().getSimpleName();
        final String tag_string_req = "req_tag";
        final TrailingDotsLoader loader = new TrailingDotsLoader(getActivity());
        loader.setPrimaryColor(Color.parseColor(AppConfig.loaderPrimaryColor));
        loader.setSecondaryColor(Color.parseColor(AppConfig.loaderSecondaryColor));
        loader.setDotCount(AppConfig.loaderDotsCount);
        loader.setDotRadius(AppConfig.loaderDotsRadius);
        loader.setAnimationDuration(AppConfig.loaderAnimationDuration);
        parentContainer.addView(loader);
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_PAYMENT_LIST, response -> {
            Log.d(TAG, "Response: " + response);
            parentContainer.removeView(loader);
            try {
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
                    id.setText(String.valueOf(jsonObject.getInt("PaymentNo")));
                    month.setText(jsonObject.getString("SupplierName"));
                    sales.setText(jsonObject.getString("amount"));
                    items.addView(tableRow);
                }
                parentContainer.removeView(loader);
            } catch (JSONException e) {
                Log.e(TAG, "getData: ", e);
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
                params.put("from", fromVal);
                params.put("to", toVal);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
}