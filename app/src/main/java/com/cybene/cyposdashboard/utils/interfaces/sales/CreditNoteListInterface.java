package com.cybene.cyposdashboard.utils.interfaces.sales;

import com.cybene.cyposdashboard.utils.data.sales.CreditNoteList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface CreditNoteListInterface {
    @GET("/cyposbackend/controller/sales/CreditNoteListController.php")
Call<List<CreditNoteList>> getSalesInfo();
}
