package project.persistence.spell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 25.11.2015.
 *
 * Class that represents the collection of spell slots for a single character.
 */
public class SpellSlotArray {
    private final ArrayList<SpellSlot> spellSlots;

    public SpellSlotArray(){
        this.spellSlots = new ArrayList<SpellSlot>();
    }

    // constructor to load from toString output
    public SpellSlotArray(String s){
        this();
        for(String pair : s.split(";")){
            if(pair.length()==0) continue;
            this.spellSlots.add(new SpellSlot(pair));
        }
    }

    public void add(SpellSlot ss){
        this.spellSlots.add(ss);
    }

    public void remove(String className, int level){
        for(SpellSlot slot : this.spellSlots){
            if(slot.getClassName()==className && slot.getLevel() == level){
                this.spellSlots.remove(slot);
                return;
            }
        }
    }

    // get the spellslots nicely formated for thymeleaf to foreach through them
    public HashMap<Integer,ArrayList<SpellSlot>> getSpellSlots() {
        HashMap<Integer,ArrayList<SpellSlot>> spellSlotTable = new HashMap<Integer,ArrayList<SpellSlot>>();
        for(SpellSlot ss : this.spellSlots){
            int level = ss.getLevel();
            if(!spellSlotTable.containsKey(level)) spellSlotTable.put(level, new ArrayList<SpellSlot>());
            spellSlotTable.get(level).add(ss);
        }
        return spellSlotTable;
    }

    // get a list of the unique spell slot types.
    public ArrayList<SpellSlot> getSpellSlotTypes() {
        ArrayList<String> typeNames = new ArrayList<String>();
        ArrayList<SpellSlot> types = new ArrayList<SpellSlot>();
        for(SpellSlot slot : this.spellSlots){
            String typeName = slot.getType();
            if(!typeNames.contains(typeName)){
                typeNames.add(typeName);
                types.add(slot);
            }
        }
        return types;
    }

    //Update the spells in the array
    public void updateSpells(ArrayList<String> newSpellInfo){
        HashMap<Integer,ArrayList<SpellSlot>> spellSlotTable = getSpellSlots();
        for(String spell : newSpellInfo){
            String[] info = spell.split(":");
            Integer level = Integer.parseInt(info[0]);
            Integer slotNum = Integer.parseInt(info[1]);
            Integer spellID = Integer.parseInt(info[2]);
            if(spellID == 0) continue;
            spellSlotTable.get(level).get(slotNum).setSpell(new Spell(spellID));
        }
    }

    public void updateSpellStatus(ArrayList<String> newPrepInfo) {
        HashMap<Integer,ArrayList<SpellSlot>> spellSlotTable = getSpellSlots();
        for(String spell : newPrepInfo){
            String[] info = spell.split(":");
            Integer level = Integer.parseInt(info[0]);
            Integer slotNum = Integer.parseInt(info[1]);
            String status = info[2];
            spellSlotTable.get(level).get(slotNum).setAvailable(!status.equalsIgnoreCase("spent"));
        }
    }

    public void update(String className, int level, int numSpells){
        int oldCount = this.count(className, level);
        if(numSpells >= oldCount){
            for (int i = 0; i < numSpells - oldCount; i++) {
                this.add(new SpellSlot(className,level));
            }
        } else {
            // Pretty sure this will never happen
            for (int i = 0; i < oldCount - numSpells; i++) {
                this.remove(className,level);
            }
        }
    }

    public int count(String className, int level){
        int count = 0;
        for(SpellSlot slot : this.spellSlots){
            if(slot.getLevel() == level && slot.getClassName() == className) count++;
        }
        return count;
    }

    @Override
    public String toString(){
        String s = "";
        for(SpellSlot ss : this.spellSlots){
            s += ss.toString()+";";
        }
        return s;
    }
}
