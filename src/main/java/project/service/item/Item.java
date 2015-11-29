package project.service.item;

import project.service.dbLookup.Lookup;
import project.service.dbLookup.OfflineResultSet;

/**
 * Created by leo on 28.11.2015.
 */
public class Item {
    private final String id;
    private String name = "";
    private final String category;
    private final String subcategory;
    private final String cost;
    private final String weight;
    private final String fullText;
    private final String reference;
    private final boolean special;
    private boolean equipped = false;

    public Item(String desc, boolean special){
        this.special = special;
        System.out.println("Got this descriptor for an item in the super class: "+desc);

        String[] info = desc.split(":");
        String id = info[0];

        Lookup find = new Lookup();
        OfflineResultSet item;

        if(special) item = find.specialItem(id + "/exact");
        else item = find.mundaneItem(id + "/exact");

        item.first();
        this.id = item.getString("id");
        this.name = item.getString("name");
        this.category = item.getString("category");
        this.subcategory = item.getString("subcategory");
        this.cost = item.getString("cost");
        this.weight = item.getString("weight");
        this.fullText = item.getString("full_text");
        this.reference = item.getString("reference");
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public String getSubcategory() {
        return subcategory;
    }

    public String getCost() {
        return cost;
    }

    public String getWeight() {
        return weight;
    }

    public String getFullText() {
        return fullText;
    }

    public String getReference() {
        return reference;
    }

    public boolean isSpecial() {
        return special;
    }

    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    @Override
    public String toString() {
        return "You should not be seeing this, it should be handled in subclasses";
    }
}
