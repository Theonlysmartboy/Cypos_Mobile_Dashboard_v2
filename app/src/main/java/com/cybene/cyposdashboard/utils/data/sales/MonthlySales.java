package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class MonthlySales {

    @SerializedName("month")
    private float month;
    @SerializedName("total")
    private float total;

    public float getMonth() {
        return month;
    }

    public float getTotal() {
        return total;
    }
}
