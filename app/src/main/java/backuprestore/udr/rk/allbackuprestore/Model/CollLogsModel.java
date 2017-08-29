package backuprestore.udr.rk.allbackuprestore.Model;

/**
 * Created by NetSupport on 01-02-2017.
 */

public class CollLogsModel {

    String number;
    String date;
    String duration;
    String type;
    String newNo;
    String cached_name;
    String cached_number_type;
    String cached_number_label;

//    public CollLogsModel(String number, String date) {
//        this.number = number;
//        this.date = date;
//
//    }


    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCached_name() {
        return cached_name;
    }

    public void setCached_name(String cached_name) {
        this.cached_name = cached_name;
    }

    public String getNewNo() {
        return newNo;
    }

    public void setNewNo(String newNo) {
        this.newNo = newNo;
    }

    public String getCached_number_type() {
        return cached_number_type;
    }

    public void setCached_number_type(String cached_number_type) {
        this.cached_number_type = cached_number_type;
    }

    public String getCached_number_label() {
        return cached_number_label;
    }

    public void setCached_number_label(String cached_number_label) {
        this.cached_number_label = cached_number_label;
    }
}
