package com.cybene.cyposdashboard.utils.interfaces.purchase;

import com.cybene.cyposdashboard.utils.data.purchase.MonthlyPurchase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MonthlyPurchaseInterface {
    @GET("/cyposbackend/controller/purchases/MonthlyPurchaseController.php")
Call<List<MonthlyPurchase>> getSalesInfo();
}
