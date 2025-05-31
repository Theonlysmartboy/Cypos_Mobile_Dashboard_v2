package com.cybene.cyposdashboard.utils.interfaces.purchase;

import com.cybene.cyposdashboard.utils.data.purchase.DailyPurchaseGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DailyPurchaseGridInterface {
    @GET("/cyposbackend/controller/purchases/DailyPurchaseDataGridController.php")
Call<List<DailyPurchaseGrid>> getSalesInfo();
}
