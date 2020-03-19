package shafi.sbf.com.foodjinni.pojo;

import java.io.Serializable;
import java.util.List;

import shafi.sbf.com.foodjinni.OrderChart;
import shafi.sbf.com.foodjinni.register.User;

public class ConfirmOrder implements Serializable {

    private String orderID;
    private List<OrderChart> chartList;
    private RestaurantDetails restaurantDetails;
    private User user;
    private String tottalPrice;
    private String includeVatTotalPrice;
    private String status;
    private String address;

    public ConfirmOrder() {
    }

    public ConfirmOrder(String orderID, List<OrderChart> chartList, RestaurantDetails restaurantDetails, User user, String tottalPrice, String includeVatTotalPrice, String status) {
        this.orderID = orderID;
        this.chartList = chartList;
        this.restaurantDetails = restaurantDetails;
        this.user = user;
        this.tottalPrice = tottalPrice;
        this.includeVatTotalPrice = includeVatTotalPrice;
        this.status = status;
    }

    public ConfirmOrder(String orderID, List<OrderChart> chartList, RestaurantDetails restaurantDetails, User user, String tottalPrice, String includeVatTotalPrice, String status, String address) {
        this.orderID = orderID;
        this.chartList = chartList;
        this.restaurantDetails = restaurantDetails;
        this.user = user;
        this.tottalPrice = tottalPrice;
        this.includeVatTotalPrice = includeVatTotalPrice;
        this.status = status;
        this.address = address;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public List<OrderChart> getChartList() {
        return chartList;
    }

    public void setChartList(List<OrderChart> chartList) {
        this.chartList = chartList;
    }

    public RestaurantDetails getRestaurantDetails() {
        return restaurantDetails;
    }

    public void setRestaurantDetails(RestaurantDetails restaurantDetails) {
        this.restaurantDetails = restaurantDetails;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTottalPrice() {
        return tottalPrice;
    }

    public void setTottalPrice(String tottalPrice) {
        this.tottalPrice = tottalPrice;
    }

    public String getIncludeVatTotalPrice() {
        return includeVatTotalPrice;
    }

    public void setIncludeVatTotalPrice(String includeVatTotalPrice) {
        this.includeVatTotalPrice = includeVatTotalPrice;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
