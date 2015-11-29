package project.service.item;

import java.util.ArrayList;

/**
 * Created by leo on 28.11.2015.
 */
public class Inventory {
    public ArrayList<MundaneItem> mundane = new ArrayList<MundaneItem>();
    public ArrayList<SpecialItem> special = new ArrayList<SpecialItem>();

    public Inventory(String desc){
        for(String itemInfo : desc.split(";")){
            if(itemInfo.equals("")) continue;
            boolean special = Boolean.parseBoolean(itemInfo.split("|")[0]);
            String itemDesc = itemInfo.split("|")[1];
            if(special) this.special.add(new SpecialItem(itemDesc));
            else this.mundane.add(new MundaneItem(itemDesc));
        }
    }

    public Inventory() {
        /* We want to be able to create an empty inventory without the hacky use of "" */
    }

    public void add(Item item){
        if(item.isSpecial()) special.add((SpecialItem)item);
        else mundane.add((MundaneItem)item);
    }

    public void add(SpecialItem item){
        special.add(item);
    }

    public void add(MundaneItem item){
        mundane.add(item);
    }

    public boolean remove(Item item){
        return false;
    }
}
