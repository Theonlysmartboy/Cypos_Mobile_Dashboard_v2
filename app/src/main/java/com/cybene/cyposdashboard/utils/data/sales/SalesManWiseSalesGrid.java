package com.cybene.cyposdashboard.utils.data.sales;

import com.google.gson.annotations.SerializedName;

public class SalesManWiseSalesGrid {
    @SerializedName("salesman")
    private String salesman;
    @SerializedName("sales")
    private String sales;

    public String getSalesman() {
        return salesman;
    }

    public String getSales() {
        return sales;
    }
}
