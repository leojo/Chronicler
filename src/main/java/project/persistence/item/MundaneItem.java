package project.persistence.item;

import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;

/**
 * Created by leo on 28.11.2015.
 *
 *
 */
public class MundaneItem extends Item{
    private final String family;
    private final String dmgSmall;
    private final String armorShieldBonus;
    private final String maxDex;
    private final String dmgMedium;
    private final String crit;
    private final String acPen;
    private final String spellFail;
    private final String rangeIncrement;
    private final String speed30;
    private final String type;
    private final String speed20;

    public MundaneItem(String desc){
        super(desc,false);
        String[] info = desc.split(":");
        String id = info[0];

        Lookup find = new Lookup();
        OfflineResultSet mundaneItem = find.mundaneItem(id + "/exact");
        mundaneItem.first();
        this.family = mundaneItem.getString("family");
        this.dmgSmall = mundaneItem.getString("dmg_s");
        this.armorShieldBonus = mundaneItem.getString("armor_shield_bonus");
        this.maxDex = mundaneItem.getString("maximum_dex_bonus");
        this.dmgMedium = mundaneItem.getString("dmg_m");
        this.crit = mundaneItem.getString("critical");
        this.acPen = mundaneItem.getString("armor_check_penalty");
        this.spellFail = mundaneItem.getString("arcane_spell_failure_chance");
        this.rangeIncrement = mundaneItem.getString("range_increment");
        this.speed30 = mundaneItem.getString("speed_30");
        this.type = mundaneItem.getString("type");
        this.speed20 = mundaneItem.getString("speed_20");

        if(info.length == 2) setName(info[1]);
    }

    public String getFamily() {
        return family;
    }

    public String getDmgSmall() {
        return dmgSmall;
    }

    public String getArmorShieldBonus() {
        return armorShieldBonus;
    }

    public String getMaxDex() {
        return maxDex;
    }

    public String getDmgMedium() {
        return dmgMedium;
    }

    public String getCrit() {
        return crit;
    }

    public String getAcPen() {
        return acPen;
    }

    public String getSpellFail() {
        return spellFail;
    }

    public String getRangeIncrement() {
        return rangeIncrement;
    }

    public String getSpeed30() {
        return speed30;
    }

    public String getType() {
        return type;
    }

    public String getSpeed20() {
        return speed20;
    }

    @Override
    public String getShortDescription() {
        String desc = "";
        switch (getFamily()) {
            case "Weapons":
                desc = "";
                desc += "Type: "+getCategory().split(" ")[0];
                desc += " "+getSubcategory();
                desc += ",  Damage: "+getDmgMedium();
                if(!(getRangeIncrement().equalsIgnoreCase("none") || getRangeIncrement().equalsIgnoreCase("-")))
                    desc += ",  Range Increment: "+getRangeIncrement();
                desc += ",  Damage type: "+getType();
                desc += ",  Critical: "+getCrit();
                return desc;
            case "Armor and Shields":
                desc = "";
                desc += getSubcategory();
                desc += ",  AC bonus: "+getArmorShieldBonus();
                desc += ",  Armor check penalty: "+getAcPen();
                desc += ",  Max dex bonus: "+getMaxDex();
                desc += ",  Max speed (for medium creatures): "+getSpeed30();
                desc += ",  Arcane Spell Failure: "+getSpellFail();
                return desc;
            default:
                return "No description available for "+getFamily();
        }
    }

    @Override
    public String toString() {
        return getId() + ":" + getName() + ":" + isEquipped();
    }
}
