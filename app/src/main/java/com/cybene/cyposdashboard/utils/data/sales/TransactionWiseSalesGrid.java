package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class TransactionWiseSalesGrid {
    @SerializedName("transaction")
    private String transaction;
    @SerializedName("sales")
    private String sales;

    public String getTransaction() {
        return transaction;
    }

    public String getSales() {
        return sales;
    }
}
