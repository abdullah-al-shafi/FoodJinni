package shafi.sbf.com.foodjinni.pojo;


import java.io.Serializable;

public class RestaurantDetails implements Serializable {

    private String restaurantID;
    private String restaurantName;
    private String restaurantType;
    private String image;
    private String phone;
    private String email;
    private String password;
    private String area;

    public RestaurantDetails() {
    }

    public RestaurantDetails(String restaurantID, String restaurantName, String restaurantType, String image, String phone, String email, String password, String area) {
        this.restaurantID = restaurantID;
        this.restaurantName = restaurantName;
        this.restaurantType = restaurantType;
        this.image = image;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.area = area;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    public String getRestaurantType() {
        return restaurantType;
    }

    public void setRestaurantType(String restaurantType) {
        this.restaurantType = restaurantType;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
