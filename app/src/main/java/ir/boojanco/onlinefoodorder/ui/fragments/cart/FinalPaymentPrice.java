package ir.boojanco.onlinefoodorder.ui.fragments.cart;

import android.os.Parcel;
import android.os.Parcelable;

public class FinalPaymentPrice implements Parcelable {
    private long id;
    private String name;
    private int discountedPrice;

    public  FinalPaymentPrice(){}

    protected FinalPaymentPrice(Parcel in) {
        id = in.readLong();
        name = in.readString();
        discountedPrice = in.readInt();
    }

    public static final Creator<FinalPaymentPrice> CREATOR = new Creator<FinalPaymentPrice>() {
        @Override
        public FinalPaymentPrice createFromParcel(Parcel in) {
            return new FinalPaymentPrice(in);
        }

        @Override
        public FinalPaymentPrice[] newArray(int size) {
            return new FinalPaymentPrice[size];
        }
    };

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeInt(discountedPrice);
    }
}
