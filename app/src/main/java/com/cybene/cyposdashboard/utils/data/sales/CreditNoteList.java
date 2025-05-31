package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class CreditNoteList {
    @SerializedName("cr_no")
    private String cr_no;
    @SerializedName("customer_name")
    private String customer_name;
    @SerializedName("amount")
    private String amount;

    public String getCr_no() {
        return cr_no;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public String getAmount() {
        return amount;
    }
}
