package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class ComputerWiseSalesGrid {
    @SerializedName("name")
    private String name;
    @SerializedName("total")
    private String total;

    public String getName() {
        return name;
    }

    public String getTotal() {
        return total;
    }
}
