package project.persistence.dbRestUtils;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by leo on 25.11.2015.
 *
 * This class represents the list of known or available spells for the character.
 */
public class SpellList implements Serializable {
    private ArrayList<Spell> spells;

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

    // returns true if removed successfully
    public boolean remove(Spell s){
        return spells.removeAll(Collections.singleton(s));
    }

    //Returns an alphabetically ordered list of spells in the spell-list that are for the desired class and level
    //f.x. a Druid/Wizard can get a list of his available lvl 3 Wizard spells by getSpellsFor("Wizard",3)
    @JsonIgnore
    public ArrayList<Spell> getSpellsFor(String className, int level){
        ArrayList<Spell> subList = new ArrayList<Spell>();
        for(Spell s : this.spells){
            if(s.getLevelFor(className)==level) subList.add(s);
        }
        Collections.sort(subList);
        return subList;
    }

    //<editor-fold desc="Getters and Setters">
    public ArrayList<Spell> getSpells() {
        return spells;
    }

    public void setSpells(ArrayList<Spell> spells) {
        this.spells = spells;
    }
    //</editor-fold>
}
