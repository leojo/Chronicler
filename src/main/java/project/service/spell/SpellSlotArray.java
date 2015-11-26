package project.service.spell;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by leo on 25.11.2015.
 */
public class SpellSlotArray {
    // Git why you no find me?
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

    public HashMap<Integer,ArrayList<SpellSlot>> getSpellSlots() {
        HashMap<Integer,ArrayList<SpellSlot>> spellSlotTable = new HashMap<Integer,ArrayList<SpellSlot>>();
        for(SpellSlot ss : this.spellSlots){
            int level = ss.getLevel();
            if(!spellSlotTable.containsKey(level)) spellSlotTable.put(level, new ArrayList<SpellSlot>());
            spellSlotTable.get(level).add(ss);
        }
        return spellSlotTable;
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
