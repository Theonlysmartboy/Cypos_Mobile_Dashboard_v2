package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class TillCashPickupList {
    @SerializedName("computer")
    private String computer;
    @SerializedName("total")
    private String total;

    public String getComputer() {
        return computer;
    }

    public String getTotal() {
        return total;
    }
}
