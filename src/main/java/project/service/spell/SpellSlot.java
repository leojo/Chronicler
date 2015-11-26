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
        this.spell = null;
    }

    public SpellSlot(String className, int level){
        this(level,className);
    }

    public SpellSlot(String fromString){
        String[] info = fromString.split(":");
        this.className = info[0];
        this.level = Integer.parseInt(info[1]);
        if(info.length == 3) this.spell = new Spell(info[2]);
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

    public Spell getSpell() {
        return spell;
    }

    @Override
    public String toString(){
        if(this.spell == null) return this.className+":"+this.level;
        return this.className+":"+this.level+":"+this.spell.toString();
    }
}
