package com.cybene.cyposdashboard.ui.sales;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.AppConfig;
import com.cybene.cyposdashboard.utils.TrailingDotsLoader;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SalesMan extends Fragment {
    LinearLayout parentContainer;
    private TableLayout container;
    private RequestQueue requestQueue;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup containerParent, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sales_man, containerParent, false);
        parentContainer = view.findViewById(R.id.parentContainer);
        container = view.findViewById(R.id.container);
        requestQueue = Volley.newRequestQueue(requireContext());

        loadRemoteData();

        return view;
    }

    private void loadRemoteData() {
        final TrailingDotsLoader loader = new TrailingDotsLoader(getActivity());
        loader.setPrimaryColor(Color.parseColor(AppConfig.loaderPrimaryColor));
        loader.setSecondaryColor(Color.parseColor(AppConfig.loaderSecondaryColor));
        loader.setDotCount(AppConfig.loaderDotsCount);
        loader.setDotRadius(AppConfig.loaderDotsRadius);
        loader.setAnimationDuration(AppConfig.loaderAnimationDuration);
        parentContainer.addView(loader);
        String url = AppConfig.URL_SALESMAN_ITEMS;


        JsonArrayRequest request = new JsonArrayRequest(
                Request.Method.GET,
                url,
                null,
                response -> {
                    parentContainer.removeView(loader);
                    populateTable(response);
                },
                error -> {
                    parentContainer.removeView(loader);
                    Toast.makeText(requireContext(), "Failed to load data: " + error.getMessage(), Toast.LENGTH_LONG).show();
                }
        );

        requestQueue.add(request);
    }

    private void populateTable(JSONArray dataArray) {
        // Clear previous rows but keep header (row 0)
        container.removeViews(1, Math.max(0, container.getChildCount() - 1));

        for (int i = 0; i < dataArray.length(); i++) {
            try {
                JSONObject obj = dataArray.getJSONObject(i);

                // Generate a 1-based index like DataTables row number
                String index = String.valueOf(i + 1);

                String code = obj.optString("code", "-");
                String productName = obj.optString("product_name", "-");
                String quantity = obj.optString("quantity", "-");
                String price = obj.optString("price", "-");

                TableRow row = new TableRow(requireContext());
                row.setLayoutParams(new TableLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                ));

                // Add cells (index first)
                row.addView(createCell(index));
                row.addView(createCell(code));
                row.addView(createCell(productName));
                row.addView(createCell(quantity));
                row.addView(createCell(price));

                container.addView(row);

            } catch (JSONException e) {
                Log.e("Error","Error parsing JSON: " + e.getMessage());
            }
        }
    }


    private TextView createCell(String text) {
        TextView tv = new TextView(requireContext());
        tv.setText(text);
        tv.setPadding(8, 8, 8, 8);
        tv.setTextSize(16);
        tv.setTextColor(Color.BLACK);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundResource(R.drawable.table_border);
        return tv;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (requestQueue != null) requestQueue.cancelAll(this);
    }
}
