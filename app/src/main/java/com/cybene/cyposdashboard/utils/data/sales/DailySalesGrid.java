package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class DailySalesGrid {
    @SerializedName("date")
    private String date;
    @SerializedName("total")
    private String total;

    public String getDay() {
        return date;
    }

    public String getSale() {
        return total;
    }
}
