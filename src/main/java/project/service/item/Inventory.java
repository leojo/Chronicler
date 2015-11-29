package project.service.item;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 28.11.2015.
 */
public class Inventory {
    public ArrayList<Item> items = new ArrayList<Item>();

    public Inventory(String desc){
        for(String itemInfo : desc.split(";")){
            if(itemInfo.equals("")) continue;
            boolean special = Boolean.parseBoolean(itemInfo.substring(0,itemInfo.indexOf(":")));
            String itemDesc = itemInfo.substring(itemInfo.indexOf(":")+1);
            if(special){
                this.items.add(new SpecialItem(itemDesc));
            }
            else{
                this.items.add(new MundaneItem(itemDesc));
            }
        }
    }

    public Inventory() {
        /* We want to be able to create an empty inventory without the hacky use of "" */
    }

    public void add(Item item){
        items.add(item);
    }

    public boolean remove(Item item){
        return items.remove(item);
    }

    public ArrayList<Item> getEquipped(){
        ArrayList<Item> equipped = new ArrayList<Item>();
        for(Item item : items){
            if(item.isEquipped()) equipped.add(item);
        }
        return equipped;
    }

    public ArrayList<Item> getNotEquipped(){
        ArrayList<Item> notEquipped = new ArrayList<Item>();
        for(Item item : items){
            if(!item.isEquipped()) notEquipped.add(item);
        }
        return notEquipped;
    }

    @Override
    public String toString() {
        String s  = "";
        for(Item item : items){
            s += item.isSpecial()+":"+item.toString()+";";
        }
        return s;
    }
}
