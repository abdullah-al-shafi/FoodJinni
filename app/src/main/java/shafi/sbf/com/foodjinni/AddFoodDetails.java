package shafi.sbf.com.foodjinni;

import java.io.Serializable;

class AddFoodDetails implements Serializable {

    private String foodId;
    private  String foodName;
    private  String foodNumber;
    private  String foodDescription;
    private  String foodPrice;

    public AddFoodDetails() {
    }


    public AddFoodDetails(String foodId, String foodName, String foodNumber, String foodDescription, String foodPrice) {
        this.foodId = foodId;
        this.foodName = foodName;
        this.foodNumber = foodNumber;
        this.foodDescription = foodDescription;
        this.foodPrice = foodPrice;
    }

    public String getFoodNumber() {
        return foodNumber;
    }

    public void setFoodNumber(String foodNumber) {
        this.foodNumber = foodNumber;
    }

    public String getFoodId() {
        return foodId;
    }

    public void setFoodId(String foodId) {
        this.foodId = foodId;
    }

    public String getFoodName() {
        return foodName;
    }

    public void setFoodName(String foodName) {
        this.foodName = foodName;
    }

    public String getFoodDescription() {
        return foodDescription;
    }

    public void setFoodDescription(String foodDescription) {
        this.foodDescription = foodDescription;
    }

    public String getFoodPrice() {
        return foodPrice;
    }

    public void setFoodPrice(String foodPrice) {
        this.foodPrice = foodPrice;
    }

}
