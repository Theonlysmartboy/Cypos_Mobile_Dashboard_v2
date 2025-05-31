package com.cybene.cyposdashboard.utils.data.purchase;

import com.google.gson.annotations.SerializedName;

public class MonthlyPurchaseGrid {
    @SerializedName("month")
    private String month;
    @SerializedName("sales")
    private String sales;

    public String getMonth() {
        return month;
    }

    public String getSales() {
        return sales;
    }
}
