package project.service.spell;

import project.service.dbLookup.OfflineResultSet;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;

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

    public SpellList(){
        this.spells = new ArrayList<Spell>();
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
}
