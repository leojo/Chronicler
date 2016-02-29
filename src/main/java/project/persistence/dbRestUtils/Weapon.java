package project.persistence.dbRestUtils;

/**
 * Created by leo on 23.2.2016.
 *
 * A weapon
 */
public class Weapon extends Equipment{

    private boolean twoHand, oneHand, ranged, thrown, light;
    private String damageTypes, damage, crit, wepCat, type, rangeIncr;

    public Weapon(){
        super();
    }

    //TODO: Finish implementing Weapon


    //<editor-fold desc="Getters and Setters">
    public boolean isTwoHand() {
        return twoHand;
    }

    public void setTwoHand(boolean twoHand) {
        this.twoHand = twoHand;
    }

    public boolean isOneHand() {
        return oneHand;
    }

    public void setOneHand(boolean oneHand) {
        this.oneHand = oneHand;
    }

    public boolean isRanged() {
        return ranged;
    }

    public void setRanged(boolean ranged) {
        this.ranged = ranged;
    }

    public boolean isThrown() {
        return thrown;
    }

    public void setThrown(boolean thrown) {
        this.thrown = thrown;
    }

    public boolean isLight() {
        return light;
    }

    public void setLight(boolean light) {
        this.light = light;
    }

    public String getDamageTypes() {
        return damageTypes;
    }

    public void setDamageTypes(String damageTypes) {
        this.damageTypes = damageTypes;
    }

    public String getDamage() {
        return damage;
    }

    public void setDamage(String damage) {
        this.damage = damage;
    }

    public String getCrit() {
        return crit;
    }

    public void setCrit(String crit) {
        this.crit = crit;
    }

    public String getWepCat() {
        return wepCat;
    }

    public void setWepCat(String wepCat) {
        this.wepCat = wepCat;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRangeIncr() {
        return rangeIncr;
    }

    public void setRangeIncr(String rangeIncr) {
        this.rangeIncr = rangeIncr;
    }

    //</editor-fold>
}

