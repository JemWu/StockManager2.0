package bean;

import android.text.Editable;

import cn.bmob.v3.BmobObject;

/**
 * Created by jem on 2017/4/25.
 */

public class Commodity extends BmobObject {
    public String kind;
    public String brand;
    public String model;
    public Integer price;
    public Integer number;

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }
}
