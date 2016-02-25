package project.persistence.dbRestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;

/**
 * Created by leo on 25.11.2015.
 *
 * This class is a wrapper for the data that defines a spell.
 * It has no other functions than getters and setters.
 */
public class Spell implements Serializable, Comparable {
    // All the information the database has stored for a spell.
    private String id;
    private String name;
    private String shortDescription;
    private String fullText;
    private String description;
    private String school;
    private String specialVerbal;
    private String druidFocus;
    private String clericFocus;
    private String bardFocus;
    private String sorcererFocus;
    private String wizardFocus;
    private String arcaneFocus;
    private String xpCost;
    private String focus;
    private String arcaneMat;
    private String material;
    private String developCost;
    private String SR;
    private String save;
    private String duration;
    private String effect;
    private String area;
    private String target;
    private String range;
    private String castingTime;
    private String components;
    private String level;
    private String spellcraftDC;
    private String subSchool;
    private String descriptor;

    // Returns this spells spell-level for the given class
    @JsonIgnore
    public int getLevelFor(String className){
        // we start with some level string f.x. "Cleric 5, Druid 5, Ranger 3"
        if(!this.level.contains(className)) return -1; //If the class name isn't in the list then we return a value indicating that
        int start = this.level.indexOf(className); //Otherwize we find the indexes that define the substring f.x. "Druid 5"
        int stop = this.level.indexOf(",",start);
        if(stop == -1) stop = this.level.length(); //If there is no ',' after the class name then it was the last in the list.
        // Substring the desired part and trim everything that isn't a number, then parse to Integer.
        int lvl = Integer.parseInt(this.level.substring(start,stop).replaceAll("[^0-9]", ""));
        return lvl;
    }

    //<editor-fold desc="Getters and Setters">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShortDescription() {
        if(this.shortDescription != null) return shortDescription;
        if(this.description == null) return "No description available";
        //If we don't have a shortDescription, we just show the first part of the full description.
        // The description is html formatted so we need to remove all the html tags and such.
        String descr = this.description.trim().replaceAll("<*>","");
        // Showing just the first few characters, because a 200 word short description isn't what we want.
        int len = descr.length();
        if(len<30) return descr;
        return descr.substring(0,27)+"...";
    }

    public void setShortDescription(String shortDescription) {
        this.shortDescription = shortDescription;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getSpecialVerbal() {
        return specialVerbal;
    }

    public void setSpecialVerbal(String specialVerbal) {
        this.specialVerbal = specialVerbal;
    }

    public String getDruidFocus() {
        return druidFocus;
    }

    public void setDruidFocus(String druidFocus) {
        this.druidFocus = druidFocus;
    }

    public String getClericFocus() {
        return clericFocus;
    }

    public void setClericFocus(String clericFocus) {
        this.clericFocus = clericFocus;
    }

    public String getBardFocus() {
        return bardFocus;
    }

    public void setBardFocus(String bardFocus) {
        this.bardFocus = bardFocus;
    }

    public String getSorcererFocus() {
        return sorcererFocus;
    }

    public void setSorcererFocus(String sorcererFocus) {
        this.sorcererFocus = sorcererFocus;
    }

    public String getWizardFocus() {
        return wizardFocus;
    }

    public void setWizardFocus(String wizardFocus) {
        this.wizardFocus = wizardFocus;
    }

    public String getArcaneFocus() {
        return arcaneFocus;
    }

    public void setArcaneFocus(String arcaneFocus) {
        this.arcaneFocus = arcaneFocus;
    }

    public String getXpCost() {
        return xpCost;
    }

    public void setXpCost(String xpCost) {
        this.xpCost = xpCost;
    }

    public String getFocus() {
        return focus;
    }

    public void setFocus(String focus) {
        this.focus = focus;
    }

    public String getArcaneMat() {
        return arcaneMat;
    }

    public void setArcaneMat(String arcaneMat) {
        this.arcaneMat = arcaneMat;
    }

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public String getDevelopCost() {
        return developCost;
    }

    public void setDevelopCost(String developCost) {
        this.developCost = developCost;
    }

    public String getSR() {
        return SR;
    }

    public void setSR(String SR) {
        this.SR = SR;
    }

    public String getSave() {
        return save;
    }

    public void setSave(String save) {
        this.save = save;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    public String getRange() {
        return range;
    }

    public void setRange(String range) {
        this.range = range;
    }

    public String getCastingTime() {
        return castingTime;
    }

    public void setCastingTime(String castingTime) {
        this.castingTime = castingTime;
    }

    public String getComponents() {
        return components;
    }

    public void setComponents(String components) {
        this.components = components;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getSpellcraftDC() {
        return spellcraftDC;
    }

    public void setSpellcraftDC(String spellcraftDC) {
        this.spellcraftDC = spellcraftDC;
    }

    public String getSubSchool() {
        return subSchool;
    }

    public void setSubSchool(String subSchool) {
        this.subSchool = subSchool;
    }

    public String getDescriptor() {
        return descriptor;
    }

    public void setDescriptor(String descriptor) {
        this.descriptor = descriptor;
    }
    //</editor-fold>

    @Override
    public int compareTo(Object another) {
        if(another instanceof Spell){
            return name.compareTo(((Spell) another).getName());
        }
        return 0;
    }
}
