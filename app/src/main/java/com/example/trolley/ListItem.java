package com.example.trolley;

public class ListItem {

    private String prodName, prodQuantity, prodPrice;

    public ListItem(String prodName, String prodQuantity, String prodPrice) {
        this.prodName = prodName;
        this.prodQuantity = prodQuantity;
        this.prodPrice = prodPrice;
    }

    public String getProdName() {
        return prodName;
    }

    public String getProdQuantity() {
        return prodQuantity;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdQuantity(String prodQuantity) {
        this.prodQuantity = prodQuantity;
    }
}
