package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.DailySalesGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DailySalesGridInterface {
    @GET("/cyposbackend/controller/sales/DailySalesDataGridController.php")
Call<List<DailySalesGrid>> getSalesInfo();
}
