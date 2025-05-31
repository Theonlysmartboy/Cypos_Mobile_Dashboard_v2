package com.cybene.cyposdashboard.utils.data.purchase;

import com.google.gson.annotations.SerializedName;

public class DailyPurchaseGrid {
    @SerializedName("date")
    private String date;
    @SerializedName("purchase")
    private String purchase;

    public String getDay() {
        return date;
    }

    public String getSale() {
        return purchase;
    }
}
