package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class MonthlySalesGrid {
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
