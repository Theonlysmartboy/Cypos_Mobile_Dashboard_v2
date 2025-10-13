package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.TillCashPickupList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TillCashPickupListInterface {
    @GET("/cyposbackend/controller/sales/TillCashPickUpController.php")
Call<List<TillCashPickupList>> getSalesInfo();
}
