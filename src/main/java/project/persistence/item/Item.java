package project.persistence.item;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;

import java.io.Serializable;

/**
 * Created by leo on 28.11.2015.
 *
 * Abstract class for a single item. Each item should belong to one of the item subcategories,
 * each with it's own class.
 */
@JsonTypeInfo(use = Id.NAME, include = As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = Equipment.class),
        @Type(value = MundaneItem.class),
        @Type(value = Projectile.class),
        @Type(value = Reagent.class)
})
public abstract class Item  extends SheetObject implements Serializable, Comparable {
    private String name = "";
    private String cost;
    private String weight;

    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCost() {
        return cost;
    }

    public void setCost(String cost) {
        this.cost = cost;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }
    //</editor-fold>

    @Override
    public int compareTo(Object another) {
        if(another instanceof Item){
            return name.compareTo(((Item) another).getName());
        }
        return 0;
    }
}
