package backuprestore.udr.rk.allbackuprestore.Model;

import android.graphics.drawable.Drawable;

/**
 * Created by NetSupport on 25-01-2017.
 */

public class AppModel {

    String appName;
    Drawable appIcon;
    String appPackage;
    String appPath;
    String appVerName;
    String appSize;
    String appId;
    String appIsBackup;
    String appIsDelete;

    public String getAppIsDelete() {
        return appIsDelete;
    }

    public void setAppIsDelete(String appIsDelete) {
        this.appIsDelete = appIsDelete;
    }

    private boolean selected;


    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getAppIsBackup() {
        return appIsBackup;
    }

    public void setAppIsBackup(String appIsBackup) {
        this.appIsBackup = appIsBackup;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppImage() {
        return appImage;
    }

    public void setAppImage(String appImage) {
        this.appImage = appImage;
    }

    String appDateTime;
    String appImage;

    public String getAppDateTime() {
        return appDateTime;
    }

    public void setAppDateTime(String appDateTime) {
        this.appDateTime = appDateTime;
    }

    public String getAppVerName() {
        return appVerName;
    }

    public void setAppVerName(String appVerName) {
        this.appVerName = appVerName;
    }

    public String getAppSize() {
        return appSize;
    }

    public void setAppSize(String appSize) {
        this.appSize = appSize;
    }

    public String getAppPath() {
        return appPath;
    }

    public void setAppPath(String appPath) {
        this.appPath = appPath;
    }

    private String isSelected;

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }


    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {
        return appIcon;
    }

    public void setAppIcon(Drawable appIcon) {
        this.appIcon = appIcon;
    }

    public String getAppPackage() {
        return appPackage;
    }

    public void setAppPackage(String appPackage) {
        this.appPackage = appPackage;
    }
}
