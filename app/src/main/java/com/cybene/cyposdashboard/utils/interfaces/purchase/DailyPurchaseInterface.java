package com.cybene.cyposdashboard.utils.interfaces.purchase;

import com.cybene.cyposdashboard.utils.data.purchase.DailyPurchase;
import com.cybene.cyposdashboard.utils.data.sales.DailySales;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface DailyPurchaseInterface {
    @GET("/cyposbackend/controller/purchases/DailyPurchaseController.php")
Call<List<DailyPurchase>> getSalesInfo();
}
