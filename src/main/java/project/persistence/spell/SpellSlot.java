package project.persistence.spell;

import java.util.ArrayList;

/**
 * Created by leo on 25.11.2015.
 *
 * This class represents a single spell slot
 */
public class SpellSlot {
    private Spell spell;
    private boolean available = true;
    private final int level;
    private final String className;


    public SpellSlot(int level, String className){
        this.level = level;
        this.className = className;
        this.spell = null;
    }

    public SpellSlot(String className, int level){
        this(level,className);
    }

    //Constructor to load form toString output.
    public SpellSlot(String fromString){
        String[] info = fromString.split(":");
        this.className = info[0];
        this.level = Integer.parseInt(info[1]);
        if(info.length == 4) {
            this.spell = new Spell(info[2]);
            this.available = Boolean.parseBoolean(info[3]);
        }
    }

    public boolean prepare(Spell s){
        if(this.level != s.getLevelFor(this.className)) return false;
        this.spell = s;
        this.available = true;
        return true;
    }

    public boolean prepare(int spellID){
        return prepare(new Spell(spellID));
    }

    public boolean containsSpell(){
        return (this.spell != null);
    }

    public Spell cast(){
        this.available = false;
        return this.spell;
    }

    public String getStatus(){ return (this.available ? "available" : "spent");}

    public String getClassName() {
        return className;
    }

    public int getLevel() {
        return level;
    }

    public Spell getSpell() {
        return spell;
    }

    public String getSpellID() {
        return spell.getId();
    }

    public String getType() {return ""+this.className+this.level;}

    public ArrayList<Spell> getPossibleSpells(ArrayList<Spell> spellList){
        ArrayList<Spell> possibleSpells = new ArrayList<Spell>();
        for(Spell s : spellList){
            if(s.getLevelFor(this.className) == this.level) possibleSpells.add(s);
        }
        return possibleSpells;
    }

    public void setSpell(Spell spell) {
        this.spell = spell;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }

    @Override
    public String toString(){
        String s = this.className+":"+this.level;
        if(this.spell != null) s += ":"+this.spell.toString()+":"+this.available;
        return s;
    }
}
