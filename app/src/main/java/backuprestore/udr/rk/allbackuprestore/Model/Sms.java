package backuprestore.udr.rk.allbackuprestore.Model;

import org.xml.sax.helpers.DefaultHandler;

/**
 * Created by Ravi kumawat on 2017-01-29.
 */

public class Sms extends DefaultHandler {
    private String _id;
    private String _address;
    private String _msg;
    private String _readState; //"0" for have not read sms and "1" for have read sms
    private String _time;
    private String _folderName;

    private String isSelected;

    private String isBackup;
    private String smsAppId;
    private String smsreadState;
    private boolean selected;

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSmsAppId() {
        return smsAppId;
    }

    public void setSmsAppId(String smsAppId) {
        this.smsAppId = smsAppId;
    }

    public String getSmsreadState() {
        return smsreadState;
    }

    public void setSmsreadState(String smsreadState) {
        this.smsreadState = smsreadState;
    }

    public String getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(String isSelected) {
        this.isSelected = isSelected;
    }

    public String getIsBackup() {
        return isBackup;
    }

    public void setIsBackup(String isBackup) {
        this.isBackup = isBackup;
    }

    public String getId() {
        return _id;
    }

    public String getAddress() {
        return _address;
    }

    public String getMsg() {
        return _msg;
    }

    public String getReadState() {
        return _readState;
    }

    public String getTime() {
        return _time;
    }

    public String getFolderName() {
        return _folderName;
    }


    public void setId(String id) {
        _id = id;
    }

    public void setAddress(String address) {
        _address = address;
    }

    public void setMsg(String msg) {
        _msg = msg;
    }

    public void setReadState(String readState) {
        _readState = readState;
    }

    public void setTime(String time) {
        _time = time;
    }

    public void setFolderName(String folderName) {
        _folderName = folderName;
    }

}