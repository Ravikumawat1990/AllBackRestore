package backuprestore.udr.rk.allbackuprestore.Model;

/**
 * Created by NetSupport on 02-02-2017.
 */

public class CalenderParentModel {

    String calendar_id;
    String title;
    String description;
    String eventLocation;
    String dtstart;
    String dtend;
    String eventStatus;
    String eventTimezone;
    String hasAlarm;

    public String getCalendar_id() {
        return calendar_id;
    }

    public void setCalendar_id(String calendar_id) {
        this.calendar_id = calendar_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventLocation() {
        return eventLocation;
    }

    public void setEventLocation(String eventLocation) {
        this.eventLocation = eventLocation;
    }

    public String getDtstart() {
        return dtstart;
    }

    public void setDtstart(String dtstart) {
        this.dtstart = dtstart;
    }

    public String getDtend() {
        return dtend;
    }

    public void setDtend(String dtend) {
        this.dtend = dtend;
    }

    public String getEventStatus() {
        return eventStatus;
    }

    public void setEventStatus(String eventStatus) {
        this.eventStatus = eventStatus;
    }

    public String getEventTimezone() {
        return eventTimezone;
    }

    public void setEventTimezone(String eventTimezone) {
        this.eventTimezone = eventTimezone;
    }

    public String getHasAlarm() {
        return hasAlarm;
    }

    public void setHasAlarm(String hasAlarm) {
        this.hasAlarm = hasAlarm;
    }
}
