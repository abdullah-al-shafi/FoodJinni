package shafi.sbf.com.foodjinni;

import java.io.Serializable;

public class OrderChart implements Serializable {

    private String productName;
    private String productDiscription;
    private String productQuantity;
    private String productPrice;
    private String note;

    public OrderChart() {
    }

    public OrderChart(String productName, String productDiscription, String productQuantity, String productPrice, String note) {
        this.productName = productName;
        this.productDiscription = productDiscription;
        this.productQuantity = productQuantity;
        this.productPrice = productPrice;
        this.note = note;
    }

    public String getProductDiscription() {
        return productDiscription;
    }

    public void setProductDiscription(String productDiscription) {
        this.productDiscription = productDiscription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
