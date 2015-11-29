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
        OfflineResultSet specialItem = find.specialItem(id + "/exact");
        specialItem.first();

        this.specialAbility = specialItem.getString("special_ability");
        this.aura = specialItem.getString("aura");
        this.casterLevel= specialItem.getString("caster_level");
        this.price = specialItem.getString("price");
        this.manifesterLevel = specialItem.getString("manifester_level");
        this.prereq= specialItem.getString("prereq");

        if(info.length >= 2) setName(info[1]);
        if(info.length >= 3) setEquipped(Boolean.parseBoolean(info[2]));
        if(info.length == 4) setCharges(Integer.parseInt(info[3]));
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


    public String getShortDescription() {
        String desc = "No description available.";
        if(getCategory().equalsIgnoreCase("Wondrous")){
            String fullText = getFullText();
            int descStart = fullText.indexOf("</b>")+5; // Magic number baby! (its the length of the string "</b> ")
            int descStop = fullText.indexOf("</p>",descStart);
            desc = fullText.substring(descStart,descStop);
        }
        return desc;
    }

    @Override
    public String toString(){
        String s = getId() + ":" + getName() + ":" + isEquipped();
        if(charges >= 0) s += ":" + charges;
        return s;
    }
}
