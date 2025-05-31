package com.cybene.cyposdashboard.utils.interfaces.purchase;

import com.cybene.cyposdashboard.utils.data.sales.MonthlySalesGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MonthlyPurchaseGridInterface {
    @GET("/cyposbackend/controller/sales/MonthlySalesDataGridController.php")
Call<List<MonthlySalesGrid>> getSalesInfo();
}
