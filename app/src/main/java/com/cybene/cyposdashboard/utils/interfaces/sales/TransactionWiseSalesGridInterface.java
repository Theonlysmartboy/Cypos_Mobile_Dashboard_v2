package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.TransactionWiseSalesGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface TransactionWiseSalesGridInterface {
    @GET("/cyposbackend/controller/sales/TransactionWiseSalesDataGridController.php")
Call<List<TransactionWiseSalesGrid>> getSalesInfo();
}
