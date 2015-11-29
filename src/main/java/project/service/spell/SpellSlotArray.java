package project.service.spell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 25.11.2015.
 */
public class SpellSlotArray {
    private final ArrayList<SpellSlot> spellSlots;

    public SpellSlotArray(){
        this.spellSlots = new ArrayList<SpellSlot>();
    }

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

    public HashMap<Integer,ArrayList<SpellSlot>> getSpellSlots() {
        HashMap<Integer,ArrayList<SpellSlot>> spellSlotTable = new HashMap<Integer,ArrayList<SpellSlot>>();
        for(SpellSlot ss : this.spellSlots){
            int level = ss.getLevel();
            if(!spellSlotTable.containsKey(level)) spellSlotTable.put(level, new ArrayList<SpellSlot>());
            spellSlotTable.get(level).add(ss);
        }
        return spellSlotTable;
    }

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

    public void updateSpells(ArrayList<String> newSpellInfo){
        System.out.println("The number of total spell slots is "+this.spellSlots.size());
        HashMap<Integer,ArrayList<SpellSlot>> spellSlotTable = getSpellSlots();
        for(String spell : newSpellInfo){
            String[] info = spell.split(":");
            Integer level = Integer.parseInt(info[0]);
            Integer slotNum = Integer.parseInt(info[1]);
            Integer spellID = Integer.parseInt(info[2]);
            System.out.println("Updating spellslot "+level+","+slotNum+" with spell "+spellID);
            System.out.println("is the spellSlotTable null?? ");
            System.out.println(spellSlotTable==null);
            System.out.println("does the spellSlot table have a level "+level+"?? ");
            System.out.println(spellSlotTable.get(level)!=null);
            System.out.println("does the spellSlot table have a slot number "+slotNum+"?? ");
            System.out.println(spellSlotTable.get(level).get(slotNum)!=null);
            spellSlotTable.get(level).get(slotNum).setSpell(new Spell(spellID));
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
            if(slot.getLevel() == level) count++;
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
