package project.service.spell;

/**
 * Created by leo on 25.11.2015.
 */
public class SpellSlot {
    // Git why you no find me?
    private Spell spell;
    private boolean unspent;
    private final int level;
    private final String className;

    public SpellSlot(int level, String className){
        this.level = level;
        this.className = className;
    }

    public SpellSlot(String className, int level){
        this(level,className);
    }

    public boolean prepare(Spell s){
        if(this.level != s.getLevelFor(this.className)) return false;
        this.spell = s;
        this.unspent = true;
        return true;
    }

    public boolean isUnspent(){
        return unspent;
    }

    public boolean containsSpell(){
        return (this.spell != null);
    }

    public Spell cast(){
        this.unspent = false;
        return this.spell;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public String toString(){
        return this.className+":"+this.level;
    }
}
