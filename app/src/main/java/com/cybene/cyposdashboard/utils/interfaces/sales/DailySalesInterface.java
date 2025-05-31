package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.DailySales;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DailySalesInterface {
    @GET("/cyposbackend/controller/sales/DailySalesController.php")
Call<List<DailySales>> getSalesInfo();
}
