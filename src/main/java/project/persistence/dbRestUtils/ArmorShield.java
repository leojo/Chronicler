package project.persistence.dbRestUtils;


import project.persistence.enums.ArmorType;

/**
 * Created by leo on 23.2.2016.
 *
 * A single piece of armor or shield.
 */
public class ArmorShield extends Equipment {
    private int ACbonus, maxDex, arcaneSpellFailure, armorCheckPen, speed30, speed20;
    private ArmorType type;

    //<editor-fold desc="Getters and Setters">
    public int getACbonus() {
        return ACbonus;
    }

    public void setACbonus(int ACbonus) {
        this.ACbonus = ACbonus;
    }

    public int getMaxDex() {
        return maxDex;
    }

    public void setMaxDex(int maxDex) {
        this.maxDex = maxDex;
    }

    public int getArcaneSpellFailure() {
        return arcaneSpellFailure;
    }

    public void setArcaneSpellFailure(int arcaneSpellFailure) {
        this.arcaneSpellFailure = arcaneSpellFailure;
    }

    public int getArmorCheckPen() {
        return armorCheckPen;
    }

    public void setArmorCheckPen(int armorCheckPen) {
        this.armorCheckPen = armorCheckPen;
    }

    public int getSpeed30() {
        return speed30;
    }

    public void setSpeed30(int speed30) {
        this.speed30 = speed30;
    }

    public int getSpeed20() {
        return speed20;
    }

    public void setSpeed20(int speed20) {
        this.speed20 = speed20;
    }

    public ArmorType getType() {
        return type;
    }

    public void setType(ArmorType type) {
        this.type = type;
    }
    //</editor-fold>
}