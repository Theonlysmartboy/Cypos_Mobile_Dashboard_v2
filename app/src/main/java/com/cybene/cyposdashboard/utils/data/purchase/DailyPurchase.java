package com.cybene.cyposdashboard.utils.data.purchase;

import com.google.gson.annotations.SerializedName;

public class DailyPurchase {

    @SerializedName("dates")
    private float dates;
    @SerializedName("total")
    private float total;

    public float getDate() {
        return dates;
    }

    public float getTotal() {
        return total;
    }
}
