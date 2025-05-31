package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.SalesManWiseSalesGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface SalesManWiseSalesGridInterface {
    @GET("/cyposbackend/controller/sales/SalesManWiseSalesDataGridController.php")
Call<List<SalesManWiseSalesGrid>> getSalesInfo();
}
