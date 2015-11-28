package project.service.item;

import project.service.dbLookup.Lookup;
import project.service.dbLookup.OfflineResultSet;

/**
 * Created by leo on 28.11.2015.
 */
public class MundaneItem {
    private final String id;
    private String name = "";
    private final String family;
    private final String category;
    private final String subcategory;
    private final String cost;
    private final String dmgSmall;
    private final String armorShieldBonus;
    private final String maxDex;
    private final String dmgMedium;
    private final String weight;
    private final String crit;
    private final String acPen;
    private final String spellFail;
    private final String rangeIncrement;
    private final String speed30;
    private final String type;
    private final String speed20;
    private final String fullText;
    private final String reference;

    public MundaneItem(String desc){
        String[] info = desc.split(":");
        String id = info[0];

        Lookup find = new Lookup();
        OfflineResultSet mundaneItem = find.mundaneItem(id + "/exact");
        mundaneItem.first();
        this.id = mundaneItem.getString("id");
        this.name = mundaneItem.getString("name");
        this.family = mundaneItem.getString("family");
        this.category = mundaneItem.getString("category");
        this.subcategory = mundaneItem.getString("subcategory");
        this.cost = mundaneItem.getString("cost");
        this.dmgSmall = mundaneItem.getString("dmg_s");
        this.armorShieldBonus = mundaneItem.getString("armor_shield_bonus");
        this.maxDex = mundaneItem.getString("maximum_dex_bonus");
        this.dmgMedium = mundaneItem.getString("dmg_m");
        this.weight = mundaneItem.getString("weight");
        this.crit = mundaneItem.getString("critical");
        this.acPen = mundaneItem.getString("armor_check_penalty");
        this.spellFail = mundaneItem.getString("arcane_spell_failure_chance");
        this.rangeIncrement = mundaneItem.getString("range_increment");
        this.speed30 = mundaneItem.getString("speed_30");
        this.type = mundaneItem.getString("type");
        this.speed20 = mundaneItem.getString("speed_20");
        this.fullText = mundaneItem.getString("full_text");
        this.reference = mundaneItem.getString("reference");
    }
}
