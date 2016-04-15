package project.persistence.dbRestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by leo on 28.11.2015.
 *
 * Class to keep track of a single characters inventory
 */
public class Inventory implements Serializable {
    private ArrayList<Item> items = new ArrayList<>();
    
    public void add(Item item){
        items.add(item);
    } // FIXME: 25.2.2016 Each item should only appear once in the inventory, if there are more than one the quantity should be updated

    public boolean remove(Item item){
        return items.remove(item);
    } // FIXME: 25.2.2016 Each item should only appear once in the inventory, if there are more than one the quantity should be updated


    // Returns an alphabetically sorted list of equipped items
    @JsonIgnore
    public ArrayList<Item> getEquipped(){
        ArrayList<Item> equipped = new ArrayList<Item>();
        for(Item item : items){
            if(item instanceof Equipment && ((Equipment) item).isEquipped()) equipped.add(item);
        }
        Collections.sort(equipped);
        return equipped;
    }

    // Returns an alphabetically sorted list of items in your bags
    @JsonIgnore
    public ArrayList<Item> getNotEquipped(){
        ArrayList<Item> notEquipped = new ArrayList<Item>();
        for(Item item : items){
            if(!(item instanceof Equipment)) notEquipped.add(item);
            else{
                if(!((Equipment) item).isEquipped()) notEquipped.add(item);
            }
        }
        Collections.sort(notEquipped);
        return notEquipped;
    }

    //<editor-fold desc="Getters and Setters">

    public ArrayList<Item> getItems() {
        return items;
    }

    public void setItems(ArrayList<Item> items) {
        this.items = items;
    }

    // </editor-fold>
}
