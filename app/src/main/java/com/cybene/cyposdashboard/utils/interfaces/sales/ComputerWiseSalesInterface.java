package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.ComputerWiseSalesGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ComputerWiseSalesInterface {
    @GET("/cyposbackend/controller/sales/ComputerWiseSalesController.php")
Call<List<ComputerWiseSalesGrid>> getSalesInfo();
}
