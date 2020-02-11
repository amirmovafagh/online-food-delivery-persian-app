package ir.boojanco.onlinefoodorder.ui.activities.cart;

import java.io.Serializable;

public class FinalPaymentPrice implements Serializable {
    private long id;
    private String name;
    private int discountedPrice;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(int discountedPrice) {
        this.discountedPrice = discountedPrice;
    }
}
