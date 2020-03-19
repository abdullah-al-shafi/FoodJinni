package shafi.sbf.com.foodjinni.register;

import java.io.Serializable;

/**
 * Created by Black Shadow on 11/10/2017.
 */

public class User implements Serializable {

   private String UserId;
    private String UserName;
    private String UserNumber;
    private String UserMail;
    private String Userpassword;
    private String UserGender;

    public User(){

    }
    public User(String UserId, String UserName, String UserNumber, String UserMail, String Userpassword, String UserGender) {
        this.UserId = UserId;
        this.UserName = UserName;
        this.UserNumber = UserNumber;
        this.UserMail=UserMail;
        this.Userpassword=Userpassword;
        this.UserGender=UserGender;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public void setUserNumber(String userNumber) {
        UserNumber = userNumber;
    }

    public void setUserMail(String userMail) {
        UserMail = userMail;
    }

    public void setUserpassword(String userpassword) {
        Userpassword = userpassword;
    }

    public void setUserGender(String userGender) {
        UserGender = userGender;
    }

    public String getUserId() {
        return UserId;
    }

    public String getUserName() {
        return UserName;
    }

    public String getUserNumber() {
        return UserNumber;
    }


    public String getUserMail() {
        return UserMail;
    }

    public String getUserpassword() {
        return Userpassword;
    }

    public String getUserGender() {
        return UserGender;
    }
}
