package project.service.item;

import project.service.dbLookup.Lookup;
import project.service.dbLookup.OfflineResultSet;

/**
 * Created by leo on 28.11.2015.
 */
public class SpecialItem extends Item{
    private final String specialAbility;
    private final String aura;
    private final String casterLevel;
    private final String price;
    private final String manifesterLevel;
    private final String prereq;
    private int charges = -1;

    public SpecialItem(String desc){
        super(desc,true);
        String[] info = desc.split(":");
        String id = info[0];

        Lookup find = new Lookup();
        OfflineResultSet mundaneItem = find.mundaneItem(id + "/exact");
        mundaneItem.first();
        this.specialAbility = mundaneItem.getString("special_ability");
        this.aura = mundaneItem.getString("aura");
        this.casterLevel= mundaneItem.getString("caster_level");
        this.price = mundaneItem.getString("price");
        this.manifesterLevel = mundaneItem.getString("manifester_level");
        this.prereq= mundaneItem.getString("prereq");

        if(info.length == 2) setName(info[1]);
        if(info.length == 3) setCharges(Integer.parseInt(info[2]));
    }

    public String getSpecialAbility() {
        return specialAbility;
    }

    public String getAura() {
        return aura;
    }

    public String getCasterLevel() {
        return casterLevel;
    }

    public String getPrice() {
        return price;
    }

    public String getManifesterLevel() {
        return manifesterLevel;
    }

    public String getPrereq() {
        return prereq;
    }

    public int getCharges() {
        return charges;
    }

    public void setCharges(int charges) {
        this.charges = charges;
    }

    @Override
    public String toString(){
        String s = getId() + ":" + getName() + ":" + isEquipped();
        if(charges >= 0) s += ":" + charges;
        return s;
    }
}
