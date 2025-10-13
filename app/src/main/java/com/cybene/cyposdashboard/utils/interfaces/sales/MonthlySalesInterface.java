package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.MonthlySales;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MonthlySalesInterface {
    @GET("/cyposbackend/controller/sales/MonthlySalesController.php")
Call<List<MonthlySales>> getSalesInfo();
}
