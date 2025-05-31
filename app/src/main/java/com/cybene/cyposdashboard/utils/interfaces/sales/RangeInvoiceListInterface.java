package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.AllInvoiceList;
import com.cybene.cyposdashboard.utils.data.sales.InvoiceList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface RangeInvoiceListInterface {
    @POST("/cyposbackend/controller/sales/InvoiceListController.php")
    Call<List<InvoiceList>> getSalesInfo(@Body AllInvoiceList allInvoiceList);
}
