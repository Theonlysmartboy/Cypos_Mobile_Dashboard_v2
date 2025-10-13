package com.cybene.cyposdashboard.utils.data.purchase;

import com.google.gson.annotations.SerializedName;

public class MonthlyPurchase {

    @SerializedName("month")
    private float month;
    @SerializedName("sales")
    private float sales;

    public float getMonth() {
        return month;
    }

    public float getTotal() {
        return sales;
    }
}
