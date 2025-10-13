package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class CustomerWiseSalesGrid {
    @SerializedName("customer")
    private String customer;
    @SerializedName("sales")
    private String sales;

    public String getCustomer() {
        return customer;
    }

    public String getSales() {
        return sales;
    }
}
