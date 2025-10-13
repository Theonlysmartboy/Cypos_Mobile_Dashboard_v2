package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.InvoiceList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface InvoiceListInterface {
    @GET("/cyposbackend/controller/sales/InvoiceListController.php")
Call<List<InvoiceList>> getSalesInfo();
}
