package com.cybene.cyposdashboard.ui.sales;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.cybene.cyposdashboard.R;
import com.cybene.cyposdashboard.utils.ApiClient;
import com.cybene.cyposdashboard.utils.data.sales.MonthlySalesGrid;
import com.cybene.cyposdashboard.utils.interfaces.sales.MonthlySalesGridInterface;

import org.jetbrains.annotations.NotNull;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MonthlySalesDataGridFragment extends Fragment {
    private TableLayout items;
    TextView id,month,sales;
    View tableRow;
    int idNo;


    public MonthlySalesDataGridFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_monthly_sales_data_grid, container, false);
        items = v.findViewById(R.id.container);
        getData();
        return v;
    }

    private void getData() {
        Call<List<MonthlySalesGrid>> call = ApiClient.getApiClient().create(MonthlySalesGridInterface.class).getSalesInfo();
        call.enqueue(new Callback<List<MonthlySalesGrid>>() {
            @Override
            public void onResponse(@NotNull Call<List<MonthlySalesGrid>> call, @NotNull Response<List<MonthlySalesGrid>> response) {
                //check if the response body is null
                if (response.body() != null) {
                    idNo = 1;
                    for (MonthlySalesGrid salesGrid : response.body()) {
                        tableRow = LayoutInflater.from(getActivity()).inflate(R.layout.table_item, null, false);
                        //table components
                        id = tableRow.findViewById(R.id.idNo);
                        month = tableRow.findViewById(R.id.month);
                        sales = tableRow.findViewById(R.id.sale);
                        id.setText(String.valueOf(idNo++));
                        month.setText(salesGrid.getMonth());
                        sales.setText(salesGrid.getSales());
                        setBackground();
                        items.addView(tableRow);
                    }
                }
            }

            @Override
            public void onFailure(Call<List<MonthlySalesGrid>> call, Throwable t) {

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
}