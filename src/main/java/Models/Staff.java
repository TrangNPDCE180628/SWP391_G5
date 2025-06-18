/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

public class Staff {
    private String staffId;
    private String staffName;
    private String staffFullName;
    private String staffPassword;
    private String staffGender;
    private String staffImage;
    private String staffGmail;
    private String staffPhone;
    private String staffPosition;

    public Staff() {
    }

    public Staff(String staffId, String staffName, String staffFullName, String staffPassword,
                 String staffGender, String staffImage, String staffGmail,
                 String staffPhone, String staffPosition) {
        this.staffId = staffId;
        this.staffName = staffName;
        this.staffFullName = staffFullName;
        this.staffPassword = staffPassword;
        this.staffGender = staffGender;
        this.staffImage = staffImage;
        this.staffGmail = staffGmail;
        this.staffPhone = staffPhone;
        this.staffPosition = staffPosition;
    }

    public String getStaffId() {
        return staffId;
    }

    public void setStaffId(String staffId) {
        this.staffId = staffId;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getStaffFullName() {
        return staffFullName;
    }

    public void setStaffFullName(String staffFullName) {
        this.staffFullName = staffFullName;
    }

    public String getStaffPassword() {
        return staffPassword;
    }

    public void setStaffPassword(String staffPassword) {
        this.staffPassword = staffPassword;
    }

    public String getStaffGender() {
        return staffGender;
    }

    public void setStaffGender(String staffGender) {
        this.staffGender = staffGender;
    }

    public String getStaffImage() {
        return staffImage;
    }

    public void setStaffImage(String staffImage) {
        this.staffImage = staffImage;
    }

    public String getStaffGmail() {
        return staffGmail;
    }

    public void setStaffGmail(String staffGmail) {
        this.staffGmail = staffGmail;
    }

    public String getStaffPhone() {
        return staffPhone;
    }

    public void setStaffPhone(String staffPhone) {
        this.staffPhone = staffPhone;
    }

    public String getStaffPosition() {
        return staffPosition;
    }

    public void setStaffPosition(String staffPosition) {
        this.staffPosition = staffPosition;
    }

    @Override
    public String toString() {
        return "Staff{" +
                "staffId='" + staffId + '\'' +
                ", staffName='" + staffName + '\'' +
                ", staffFullName='" + staffFullName + '\'' +
                ", staffPassword='" + staffPassword + '\'' +
                ", staffGender='" + staffGender + '\'' +
                ", staffImage='" + staffImage + '\'' +
                ", staffGmail='" + staffGmail + '\'' +
                ", staffPhone='" + staffPhone + '\'' +
                ", staffPosition='" + staffPosition + '\'' +
                '}';
    }
}

