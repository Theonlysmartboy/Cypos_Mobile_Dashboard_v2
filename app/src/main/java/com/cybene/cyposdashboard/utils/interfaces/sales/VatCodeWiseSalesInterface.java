package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.VatCodeWiseSalesGrid;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface VatCodeWiseSalesInterface {
    @GET("/cyposbackend/controller/sales/VatCodeWiseSalesController.php")
Call<List<VatCodeWiseSalesGrid>> getSalesInfo();
}
