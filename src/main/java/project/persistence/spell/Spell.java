package project.persistence.spell;

import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;

/**
 * Created by leo on 25.11.2015.
 */
public class Spell {
    private final String id;
    private final String name;
    private final String shortDescription;
    private final String fullText;
    private final String description;
    private final String school;
    private final String specialVerbal;
    private final String druidFocus;
    private final String clericFocus;
    private final String bardFocus;
    private final String sorcererFocus;
    private final String wizardFocus;
    private final String arcaneFocus;
    private final String xpCost;
    private final String focus;
    private final String arcaneMat;
    private final String material;
    private final String developCost;
    private final String SR;
    private final String save;
    private final String duration;
    private final String effect;
    private final String area;
    private final String target;
    private final String range;
    private final String castingTime;
    private final String components;
    private final String level;
    private final String spellcraftDC;
    private final String subSchool;
    private final String descriptor;

    // This constructor assumes we get a ors with the cursor on the spell itself.
    public Spell(OfflineResultSet spell) {
        this.id = spell.getString("id");
        this.name = spell.getString("name");
        this.shortDescription = spell.getString("short_description");
        this.description = spell.getString("description");
        this.fullText = spell.getString("full_text");
        this.school = spell.getString("school");
        this.subSchool = spell.getString("subschool");
        this.descriptor = spell.getString("descriptor");
        this.spellcraftDC = spell.getString("spellcraft_dc");
        this.level = spell.getString("level");
        this.components = spell.getString("components");
        this.castingTime = spell.getString("casting_time");
        this.range = spell.getString("range");
        this.target = spell.getString("target");
        this.area = spell.getString("area");
        this.effect = spell.getString("effect");
        this.duration = spell.getString("duration");
        this.save = spell.getString("saving_throw");
        this.SR = spell.getString("spell_resistance");
        this.developCost = spell.getString("to_develop");
        this.material = spell.getString("material_components");
        this.arcaneMat = spell.getString("arcane_material_components");
        this.focus = spell.getString("focus");
        this.xpCost = spell.getString("xp_cost");
        this.arcaneFocus = spell.getString("arcane_focus");
        this.wizardFocus = spell.getString("wizard_focus");
        this.sorcererFocus = spell.getString("sorcerer_focus");
        this.bardFocus = spell.getString("bard_focus");
        this.clericFocus = spell.getString("cleric_focus");
        this.druidFocus = spell.getString("druid_focus");
        this.specialVerbal = spell.getString("verbal_components");
    }

    public Spell(String id){
        Lookup find = new Lookup();
        OfflineResultSet spell = find.spell(id+"/exact");
        spell.first();
        this.id = spell.getString("id");
        this.name = spell.getString("name");
        this.shortDescription = spell.getString("short_description");
        this.description = spell.getString("description");
        this.fullText = spell.getString("full_text");
        this.school = spell.getString("school");
        this.subSchool = spell.getString("subschool");
        this.descriptor = spell.getString("descriptor");
        this.spellcraftDC = spell.getString("spellcraft_dc");
        this.level = spell.getString("level");
        this.components = spell.getString("components");
        this.castingTime = spell.getString("casting_time");
        this.range = spell.getString("range");
        this.target = spell.getString("target");
        this.area = spell.getString("area");
        this.effect = spell.getString("effect");
        this.duration = spell.getString("duration");
        this.save = spell.getString("saving_throw");
        this.SR = spell.getString("spell_resistance");
        this.developCost = spell.getString("to_develop");
        this.material = spell.getString("material_components");
        this.arcaneMat = spell.getString("arcane_material_components");
        this.focus = spell.getString("focus");
        this.xpCost = spell.getString("xp_cost");
        this.arcaneFocus = spell.getString("arcane_focus");
        this.wizardFocus = spell.getString("wizard_focus");
        this.sorcererFocus = spell.getString("sorcerer_focus");
        this.bardFocus = spell.getString("bard_focus");
        this.clericFocus = spell.getString("cleric_focus");
        this.druidFocus = spell.getString("druid_focus");
        this.specialVerbal = spell.getString("verbal_components");
    }

    public Spell(int id){
        this(id+"");
    }

    // Returns this spells spell level for the given class
    public int getLevelFor(String className){
        if(!this.level.contains(className)) return -1;
        int start = this.level.indexOf(className);
        int stop = this.level.indexOf(",",start);
        if(stop == -1) stop = this.level.length();
        int lvl = Integer.parseInt(this.level.substring(start,stop).replaceAll("[^0-9]", ""));
        return lvl;
    }
    public String getId(){ return id; }

    public String getName() {
        return name;
    }

    public String getShortDescription() {
        if(this.shortDescription != null) return shortDescription;
        if(this.description == null) return "No description available";
        String descr = this.description.trim().replaceAll("<*>","");
        int len =descr.length();
        if(len<18) return descr;
        return descr.substring(0,15)+"...";
    }

    public String getFullText() {
        return fullText;
    }

    public String getDescription() {
        return description;
    }

    public String getSchool() {
        return school;
    }

    public String getSpecialVerbal() {
        return specialVerbal;
    }

    public String getDruidFocus() {
        return druidFocus;
    }

    public String getClericFocus() {
        return clericFocus;
    }

    public String getBardFocus() {
        return bardFocus;
    }

    public String getSorcererFocus() {
        return sorcererFocus;
    }

    public String getWizardFocus() {
        return wizardFocus;
    }

    public String getArcaneFocus() {
        return arcaneFocus;
    }

    public String getXpCost() {
        return xpCost;
    }

    public String getFocus() {
        return focus;
    }

    public String getArcaneMat() {
        return arcaneMat;
    }

    public String getMaterial() {
        return material;
    }

    public String getDevelopCost() {
        return developCost;
    }

    public String getSR() {
        return SR;
    }

    public String getSave() {
        return save;
    }

    public String getDuration() {
        return duration;
    }

    public String getEffect() {
        return effect;
    }

    public String getArea() {
        return area;
    }

    public String getTarget() {
        return target;
    }

    public String getRange() {
        return range;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public String getComponents() {
        return components;
    }

    public String getLevel() {
        return level;
    }

    public String getSpellcraftDC() {
        return spellcraftDC;
    }

    public String getSubSchool() {
        return subSchool;
    }

    public String getDescriptor() {
        return descriptor;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
