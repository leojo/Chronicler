package project.persistence.dbRestUtils;

import project.persistence.enums.WeaponCategory;
import project.persistence.enums.WeaponType;

/**
 * Created by leo on 23.2.2016.
 *
 * A weapon
 */
public class Weapon extends Equipment{

    private int damageDie, diceCount, critMult, critRange; // critRange = 2 if it's "19-20" i.e. (upper-lower)+1
    private boolean twoHand, oneHand, ranged, thrown, finessable;
    private String damageTypes;
    private WeaponCategory wepCategory;
    private WeaponType weaponType;
    private int rangeIncrement;
    private Projectile ammo;

    public Weapon(){
        super();
    }

    //TODO: Finish implementing Weapon


    //<editor-fold desc="Getters and Setters">
    public int getDamageDie() {
        return damageDie;
    }

    public void setDamageDie(int damageDie) {
        this.damageDie = damageDie;
    }

    public int getDiceCount() {
        return diceCount;
    }

    public void setDiceCount(int diceCount) {
        this.diceCount = diceCount;
    }

    public int getCritMult() {
        return critMult;
    }

    public void setCritMult(int critMult) {
        this.critMult = critMult;
    }

    public int getCritRange() {
        return critRange;
    }

    public void setCritRange(int critRange) {
        this.critRange = critRange;
    }

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

    public boolean isFinessable() {
        return finessable;
    }

    public void setFinessable(boolean finessable) {
        this.finessable = finessable;
    }

    public String getDamageTypes() {
        return damageTypes;
    }

    public void setDamageTypes(String damageTypes) {
        this.damageTypes = damageTypes;
    }

    public WeaponCategory getWepCategory() {
        return wepCategory;
    }

    public void setWepCategory(WeaponCategory wepCategory) {
        this.wepCategory = wepCategory;
    }

    public WeaponType getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(WeaponType weaponType) {
        this.weaponType = weaponType;
    }

    public int getRangeIncrement() {
        return rangeIncrement;
    }

    public void setRangeIncrement(int rangeIncrement) {
        this.rangeIncrement = rangeIncrement;
    }

    public Projectile getAmmo() {
        return ammo;
    }

    public void setAmmo(Projectile ammo) {
        this.ammo = ammo;
    }
    //</editor-fold>
}
