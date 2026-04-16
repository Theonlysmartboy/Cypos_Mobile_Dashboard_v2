package com.cybene.cyposdashboard.ui.fragment.sales;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.ApiClient;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;
import com.cybene.cyposdashboard.utils.TrailingDotsLoader;
import com.cybene.cyposdashboard.utils.data.sales.SalesManWiseSalesGrid;
import com.cybene.cyposdashboard.utils.interfaces.sales.SalesManWiseSalesGridInterface;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalesManWiseSalesDataGridFragment extends Fragment {
    private TableLayout items;
    TextView id,month,sales;
    View tableRow;
    int idNo;
    EditText from,to;
    DatePickerDialog picker;
    Button show;
    String fromVal, toVal;
    LinearLayout parentContainer;

    public SalesManWiseSalesDataGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_sales_man_wise_sales_data_grid, container, false);
        items = v.findViewById(R.id.container);
        parentContainer = v.findViewById(R.id.parentContainer);
        getData();
        from = v.findViewById(R.id.date_from);
        to = v.findViewById(R.id.date_to);
        from.setOnClickListener(view -> {
            final Calendar cldr = Calendar.getInstance();
            int day = cldr.get(Calendar.DAY_OF_MONTH);
            int month = cldr.get(Calendar.MONTH);
            int year = cldr.get(Calendar.YEAR);
            // date picker dialog
            picker = new DatePickerDialog(requireActivity(), (view1, year1, monthOfYear, dayOfMonth) -> from.setText(year1 + "-" + (monthOfYear+1) + "-" + dayOfMonth), year, month, day);
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
            picker = new DatePickerDialog(requireActivity(), (view2, year2, monthOfYear, dayOfMonth) -> to.setText(year2 + "-" + (monthOfYear+1) + "-" + dayOfMonth), year, month, day);
            picker.getDatePicker().setLayoutMode(1);
            picker.show();
        });
        show = v.findViewById(R.id.show);
        show.setOnClickListener(view -> {
            fromVal = from.getText().toString().trim();
            toVal = to.getText().toString().trim();
            getRangeBasedData(fromVal,toVal);

        });
        return v;
    }

    private void getRangeBasedData(final String fromVal, final String toVal) {
        final String TAG = requireActivity().getClass().getSimpleName();
        String tag_string_req = "req_tag";
        final TrailingDotsLoader loader = new TrailingDotsLoader(getActivity());
        loader.setPrimaryColor(Color.parseColor(AppConfig.loaderPrimaryColor));
        loader.setSecondaryColor(Color.parseColor(AppConfig.loaderSecondaryColor));
        loader.setDotCount(AppConfig.loaderDotsCount);
        loader.setDotRadius(AppConfig.loaderDotsRadius);
        loader.setAnimationDuration(AppConfig.loaderAnimationDuration);
        parentContainer.addView(loader);
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_SALESMAN_WISE_SALE, response -> {
            Log.d(TAG, "Response: " + response);
            removeRows();
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
                    month.setText(jsonObject.getString("salesman"));
                    sales.setText(jsonObject.getString("sales"));
                    items.addView(tableRow);
                }
                parentContainer.removeView(loader);
            } catch (JSONException e) {
                Log.e(TAG, "getRangeBasedData: ", e );
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

    private void getData() {
        Call<List<SalesManWiseSalesGrid>> call = ApiClient.getApiClient().create(SalesManWiseSalesGridInterface.class).getSalesInfo();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<List<SalesManWiseSalesGrid>> call, @NotNull Response<List<SalesManWiseSalesGrid>> response) {
                //check if the response body is null
                if (response.body() != null) {
                    idNo = 1;
                    for (SalesManWiseSalesGrid salesGrid : response.body()) {
                        tableRow = LayoutInflater.from(getActivity()).inflate(R.layout.table_item, null, false);
                        //table components
                        id = tableRow.findViewById(R.id.idNo);
                        month = tableRow.findViewById(R.id.month);
                        sales = tableRow.findViewById(R.id.sale);
                        id.setText(String.valueOf(idNo++));
                        month.setText(salesGrid.getSalesman());
                        sales.setText(salesGrid.getSales());
                        setBackground();
                        items.addView(tableRow);
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<SalesManWiseSalesGrid>> call, @NonNull Throwable t) {

            }
        });
    }

    private void setBackground() {
        int numRows = items.getChildCount();
        for (int i=0; i<numRows;i++) {
            if(i%2==1){
                items.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorAccent, requireActivity().getTheme()));
            }else{
                items.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white, requireActivity().getTheme()));
            }
        }
    }
    public void removeRows(){
        int numRows = items.getChildCount();
        for(int i = 1; i<numRows; i++) {
            View child = items.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }
}