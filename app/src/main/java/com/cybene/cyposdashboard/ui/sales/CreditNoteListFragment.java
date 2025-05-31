package com.cybene.cyposdashboard.ui.sales;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.agrawalsuneet.dotsloader.loaders.RotatingCircularDotsLoader;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.ApiClient;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.AppController;
import com.cybene.cyposdashboard.utils.data.sales.CreditNoteList;
import com.cybene.cyposdashboard.utils.interfaces.sales.CreditNoteListInterface;

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
public class CreditNoteListFragment extends Fragment {
    private TableLayout items;
    TextView id,month,sales;
    View tableRow;
    EditText from,to;
    DatePickerDialog picker;
    Button show;
    String fromVal, toVal;
    LinearLayout parentContainer;
        public CreditNoteListFragment() {
        // Required empty public constructor
    }
       @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_credit_note_list, container, false);
           items = v.findViewById(R.id.container);
           parentContainer = v.findViewById(R.id.parentContainer);
           getData();
           from = v.findViewById(R.id.date_from);
           to = v.findViewById(R.id.date_to);
           from.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   final Calendar cldr = Calendar.getInstance();
                   int day = cldr.get(Calendar.DAY_OF_MONTH);
                   int month = cldr.get(Calendar.MONTH);
                   int year = cldr.get(Calendar.YEAR);
                   // date picker dialog
                   picker = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
                       @Override
                       public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                           from.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                       }
                   }, year, month, day);
                   picker.getDatePicker().setLayoutMode(1);
                   picker.show();
               }
           });
           to = v.findViewById(R.id.date_to);
           to.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   final Calendar cldr = Calendar.getInstance();
                   int day = cldr.get(Calendar.DAY_OF_MONTH);
                   int month = cldr.get(Calendar.MONTH);
                   int year = cldr.get(Calendar.YEAR);
                   // date picker dialog
                   picker = new DatePickerDialog(requireActivity(), new DatePickerDialog.OnDateSetListener() {
                       @Override
                       public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                           to.setText(year + "-" + (monthOfYear+1) + "-" + dayOfMonth);
                       }
                   }, year, month, day);
                   picker.getDatePicker().setLayoutMode(1);
                   picker.show();
               }
           });
           show = v.findViewById(R.id.show);
           show.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   fromVal = from.getText().toString().trim();
                   toVal = to.getText().toString().trim();
                   getRangeBasedData(fromVal,toVal);
               }
           });
        return v;
    }
    private void getRangeBasedData(final String fromVal, final String toVal) {
        final String TAG = getActivity().getClass().getSimpleName();
        String tag_string_req = "req_tag";
        final RotatingCircularDotsLoader loader = new RotatingCircularDotsLoader(getActivity(),
                20, 60, ContextCompat.getColor(getActivity(), R.color.design_default_color_primary));
        loader.setAnimDuration(3000);
        parentContainer.addView(loader);
        StringRequest strReq = new StringRequest(Request.Method.POST, AppConfig.URL_CREDIT_NOTE_LIST, new com.android.volley.Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response);
                removeRows();
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
                        id.setText(jsonObject.getString("cr_no"));
                        month.setText(jsonObject.getString("customer_name"));
                        sales.setText(jsonObject.getString("amount"));
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
                params.put("from", fromVal);
                params.put("to", toVal);
                return params;
            }
        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    private void getData() {
        Call<List<CreditNoteList>> call = ApiClient.getApiClient().create(CreditNoteListInterface.class).getSalesInfo();
        call.enqueue(new Callback<List<CreditNoteList>>() {
            @Override
            public void onResponse(@NotNull Call<List<CreditNoteList>> call, @NotNull Response<List<CreditNoteList>> response) {
                //check if the response body is null
                if (response.body() != null) {
                    for (CreditNoteList salesGrid : response.body()) {
                        tableRow = LayoutInflater.from(getActivity()).inflate(R.layout.table_item, null, false);
                        //table components
                        id = tableRow.findViewById(R.id.idNo);
                        month = tableRow.findViewById(R.id.month);
                        sales = tableRow.findViewById(R.id.sale);
                        id.setText(salesGrid.getCr_no());
                        month.setText(salesGrid.getCustomer_name());
                        sales.setText(salesGrid.getAmount());
                        setBackground();
                        items.addView(tableRow);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<CreditNoteList>> call, Throwable t) {

            }
        });
    }
    private void setBackground() {
        int numrows = items.getChildCount();
        for (int i=0; i<numrows;i++) {
            if(i%2==1){
                items.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }else{
                items.getChildAt(i).setBackgroundColor(getResources().getColor(R.color.white));
            }
        }
    }
    public void removeRows(){
        int numrows = items.getChildCount();
        for(int i = 1; i<numrows; i++) {
            View child = items.getChildAt(i);
            if (child instanceof TableRow) ((ViewGroup) child).removeAllViews();
        }
    }
}