package project.persistence.dbRestUtils;


import project.persistence.enums.ArmorType;

/**
 * Created by leo on 23.2.2016.
 *
 * A single piece of armor or shield.
 */
public class ArmorShield extends Equipment {
    private String ACbonus, maxDex, arcaneSpellFailure, armorCheckPen;
    private String speed20, speed30;
    private ArmorType type;

    //<editor-fold desc="Getters and Setters">
    public String getACbonus() {
        return ACbonus;
    }

    public void setACbonus(String ACbonus) {
        this.ACbonus = ACbonus;
    }

    public String getMaxDex() {
        return maxDex;
    }

    public void setMaxDex(String maxDex) {
        this.maxDex = maxDex;
    }

    public String getArcaneSpellFailure() {
        return arcaneSpellFailure;
    }

    public void setArcaneSpellFailure(String arcaneSpellFailure) {
        this.arcaneSpellFailure = arcaneSpellFailure;
    }

    public String getArmorCheckPen() {
        return armorCheckPen;
    }

    public void setArmorCheckPen(String armorCheckPen) {
        this.armorCheckPen = armorCheckPen;
    }

    public String getSpeed20() {
        return speed20;
    }

    public void setSpeed20(String speed20) {
        this.speed20 = speed20;
    }

    public String getSpeed30() {
        return speed30;
    }

    public void setSpeed30(String speed30) {
        this.speed30 = speed30;
    }

    public ArmorType getType() {
        return type;
    }

    public void setType(ArmorType type) {
        this.type = type;
    }
    //</editor-fold>
}