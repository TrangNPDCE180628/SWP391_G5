/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

public class Customer {
    private String cusId;
    private String username;
    private String cusPassword;
    private String cusFullName;
    private String cusGender;
    private String cusImage;
    private String cusGmail;
    private String cusPhone;

    public Customer() {
    }

    public Customer(String cusId, String username, String cusPassword, String cusFullName,
                    String cusGender, String cusImage, String cusGmail, String cusPhone) {
        this.cusId = cusId;
        this.username = username;
        this.cusPassword = cusPassword;
        this.cusFullName = cusFullName;
        this.cusGender = cusGender;
        this.cusImage = cusImage;
        this.cusGmail = cusGmail;
        this.cusPhone = cusPhone;
    }

    public String getCusId() {
        return cusId;
    }

    public void setCusId(String cusId) {
        this.cusId = cusId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCusPassword() {
        return cusPassword;
    }

    public void setCusPassword(String cusPassword) {
        this.cusPassword = cusPassword;
    }

    public String getCusFullName() {
        return cusFullName;
    }

    public void setCusFullName(String cusFullName) {
        this.cusFullName = cusFullName;
    }

    public String getCusGender() {
        return cusGender;
    }

    public void setCusGender(String cusGender) {
        this.cusGender = cusGender;
    }

    public String getCusImage() {
        return cusImage;
    }

    public void setCusImage(String cusImage) {
        this.cusImage = cusImage;
    }

    public String getCusGmail() {
        return cusGmail;
    }

    public void setCusGmail(String cusGmail) {
        this.cusGmail = cusGmail;
    }

    public String getCusPhone() {
        return cusPhone;
    }

    public void setCusPhone(String cusPhone) {
        this.cusPhone = cusPhone;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "cusId='" + cusId + '\'' +
                ", username='" + username + '\'' +
                ", cusPassword='" + cusPassword + '\'' +
                ", cusFullName='" + cusFullName + '\'' +
                ", cusGender='" + cusGender + '\'' +
                ", cusImage='" + cusImage + '\'' +
                ", cusGmail='" + cusGmail + '\'' +
                ", cusPhone='" + cusPhone + '\'' +
                '}';
    }

    public void setPassword(String password) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public void setFullName(String fullname) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

