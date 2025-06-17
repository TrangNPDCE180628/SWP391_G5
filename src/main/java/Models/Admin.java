/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Models;

public class Admin {
    private String adminId;
    private String adminName;
    private String adminFullName;
    private String adminPassword;
    private String adminGmail;
    private String adminImage;

    public Admin() {
    }

    public Admin(String adminId, String adminName, String adminFullName, String adminPassword, String adminGmail, String adminImage) {
        this.adminId = adminId;
        this.adminName = adminName;
        this.adminFullName = adminFullName;
        this.adminPassword = adminPassword;
        this.adminGmail = adminGmail;
        this.adminImage = adminImage;
    }

    public String getAdminId() {
        return adminId;
    }

    public void setAdminId(String adminId) {
        this.adminId = adminId;
    }

    public String getAdminName() {
        return adminName;
    }

    public void setAdminName(String adminName) {
        this.adminName = adminName;
    }

    public String getAdminFullName() {
        return adminFullName;
    }

    public void setAdminFullName(String adminFullName) {
        this.adminFullName = adminFullName;
    }

    public String getAdminPassword() {
        return adminPassword;
    }

    public void setAdminPassword(String adminPassword) {
        this.adminPassword = adminPassword;
    }

    public String getAdminGmail() {
        return adminGmail;
    }

    public void setAdminGmail(String adminGmail) {
        this.adminGmail = adminGmail;
    }

    public String getAdminImage() {
        return adminImage;
    }

    public void setAdminImage(String adminImage) {
        this.adminImage = adminImage;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "adminId='" + adminId + '\'' +
                ", adminName='" + adminName + '\'' +
                ", adminFullName='" + adminFullName + '\'' +
                ", adminPassword='" + adminPassword + '\'' +
                ", adminGmail='" + adminGmail + '\'' +
                ", adminImage='" + adminImage + '\'' +
                '}';
    }
}

