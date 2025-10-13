package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class InvoiceList {
    @SerializedName("inv_no")
    private String inv_no;
    @SerializedName("customer_name")
    private String customer_name;
    @SerializedName("amount")
    private String amount;

    public String getInv_no() {
        return inv_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getAmount() {
        return amount;
    }
}
