package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.CustomerWiseSalesGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CustomerWiseSalesGridInterface {
    @GET("/cyposbackend/controller/sales/CustomerWiseSalesDataGridController.php")
Call<List<CustomerWiseSalesGrid>> getSalesInfo();
}
