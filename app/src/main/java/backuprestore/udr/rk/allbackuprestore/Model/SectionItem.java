package backuprestore.udr.rk.allbackuprestore.Model;

import backuprestore.udr.rk.allbackuprestore.interfac.Item;

public class SectionItem implements Item {


    public String name;


    public SectionItem(String name) {
        this.name = name;

    }


    @Override
    public boolean isSection() {
        return true;
    }


}
