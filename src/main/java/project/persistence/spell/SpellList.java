package project.persistence.spell;

import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by leo on 25.11.2015.
 */
public class SpellList {
    private final ArrayList<Spell> spells;

    public SpellList(OfflineResultSet ors){
        this();
        ors.beforeFirst();
        while(ors.next()){
            Spell s = new Spell(ors);
            this.spells.add(s);
        }
    }

    public SpellList(Spell[] spells){
        this();
        for(Spell s : spells){
            this.spells.add(s);
        }
    }

    public SpellList(String spellList){
        this();
        // Git why you no find me?
        Lookup find = new Lookup();
        for(String spellID : spellList.split(";")){
            if(spellID.length()==0) continue;
            OfflineResultSet ors = find.spell(spellID+"/exact");
            if(ors == null){
                System.out.println("Spell "+spellID+" wasn't found!");
                continue;
            }
            ors.first();
            Spell s = new Spell(ors);
            this.spells.add(s);
        }
    }

    public SpellList(){
        this.spells = new ArrayList<Spell>();
    }

    public ArrayList<Spell> getSpells() {
        return spells;
    }

    // usability functions

    // returns true if added successfully
    public boolean add(Spell s){
        try{
            this.spells.add(s);
        } catch (Exception e){
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }

    public boolean remove(Spell s){
        return spells.removeAll(Collections.singleton(s));
    }

    public Spell[] getSpellsFor(String className, int level){
        ArrayList<Spell> subList = new ArrayList<Spell>();
        for(Spell s : this.spells){
            if(s.getLevelFor(className)==level) subList.add(s);
        }
        return (Spell[]) subList.toArray();
    }

    @Override
    public String toString() {
        String retString = "";
        for(Spell s : this.spells){
            retString += s.getId()+";";
        }
        return retString;
    }
}
