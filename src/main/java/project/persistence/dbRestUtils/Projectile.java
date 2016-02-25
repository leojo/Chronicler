package project.persistence.dbRestUtils;

import project.persistence.enums.DamageType;

/**
 * Created by leo on 23.2.2016.
 *
 * Data structure representing a projectile.
 */
public class Projectile extends Item {
    private int quantity, damageDie, diceCount, rangeIncrement, critMult, critRange; // critRange = 2 if it's "19-20" i.e. (upper-lower)+1
    private DamageType[] damageTypes;

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

    public int getRangeIncrement() {
        return rangeIncrement;
    }

    public void setRangeIncrement(int rangeIncrement) {
        this.rangeIncrement = rangeIncrement;
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

    public DamageType[] getDamageTypes() {
        return damageTypes;
    }

    public void setDamageTypes(DamageType[] damageTypes) {
        this.damageTypes = damageTypes;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    //</editor-fold>
}
