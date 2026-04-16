package com.cybene.cyposdashboard.ui.fragment.sales;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.ApiClient;
import com.cybene.cyposdashboard.utils.data.sales.DailySalesGrid;
import com.cybene.cyposdashboard.utils.interfaces.sales.DailySalesGridInterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailySalesDataGridFragment extends Fragment {
    private TableLayout items;
    TextView id,month,sales;
    int idNo;
    public DailySalesDataGridFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_daily_sales_data_grid, container, false);
        items = v.findViewById(R.id.container);
        getData();
        return v;
    }
    private void getData() {
        Call<List<DailySalesGrid>> call = ApiClient.getApiClient().create(DailySalesGridInterface.class).getSalesInfo();
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(@NotNull Call<List<DailySalesGrid>> call, @NotNull Response<List<DailySalesGrid>> response) {
                //check if the response body is null
                if (response.body() != null) {
                    idNo = 1;
                    for (DailySalesGrid salesGrid : response.body()) {
                        View tableRow = LayoutInflater.from(getActivity()).inflate(R.layout.table_item, null, false);
                        //table components
                        id = tableRow.findViewById(R.id.idNo);
                        month = tableRow.findViewById(R.id.month);
                        sales = tableRow.findViewById(R.id.sale);
                        id.setText(String.valueOf(idNo++));
                        month.setText(salesGrid.getDay());
                        sales.setText(salesGrid.getSale());
                        items.addView(tableRow);
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DailySalesGrid>> call, @NonNull Throwable t) {

            }
        });
    }
}