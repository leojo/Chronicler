package project.service.spell;

import java.util.ArrayList;

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
            String className = pair.split(":")[0];
            int level = Integer.parseInt(pair.split(":")[1]);
            this.spellSlots.add(new SpellSlot(className,level));
        }
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
