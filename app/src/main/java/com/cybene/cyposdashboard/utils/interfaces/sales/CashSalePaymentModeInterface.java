package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.CashSalePaymentModeGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CashSalePaymentModeInterface {
    @GET("/cyposbackend/controller/sales/CashSalePaymentModeController.php")
Call<List<CashSalePaymentModeGrid>> getSalesInfo();
}
