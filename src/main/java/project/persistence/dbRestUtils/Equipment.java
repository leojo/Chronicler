package project.persistence.dbRestUtils;


import project.persistence.enums.SizeCategory;

/**
 * Created by leo on 23.2.2016.
 *
 * Abstract class for the shared properties of all equippable items.
 */
public abstract class Equipment extends Item{
    private boolean equipped;
    private boolean masterwork;
    private String slot;
    private String equipAction;
    private String description;

    public Equipment(){
        this("Slotless","Move Action");
    }

    public Equipment(String slot){
        this(slot,"Move Action");
    }

    public Equipment(String slot, String equipAction){
        this.equipAction = equipAction;
        this.equipped = false;
        this.masterwork = false;
        this.slot = slot;
    }

    public void equip(){ equipped = true;}
    public void unequip(){ equipped = false;}

    //<editor-fold desc="Getters and Setters">
    public boolean isEquipped() {
        return equipped;
    }

    public void setEquipped(boolean equipped) {
        this.equipped = equipped;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public boolean isMasterwork() {
        return masterwork;
    }

    public void setMasterwork(boolean masterwork) {
        this.masterwork = masterwork;
    }

    public String getEquipAction() {
        return equipAction;
    }

    public void setEquipAction(String equipAction) {
        this.equipAction = equipAction;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    //</editor-fold>
}
