package backuprestore.udr.rk.allbackuprestore.Model;


import backuprestore.udr.rk.allbackuprestore.interfac.Item;

public class EntryItem implements Item {


    public String contact;
    public String type;


    public EntryItem(String contact, String type) {
        this.contact = contact;
        this.type = type;

    }

    @Override
    public boolean isSection() {
        return false;
    }

}
