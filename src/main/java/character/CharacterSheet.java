package character;

import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by BjornBjarnsteins on 10/2/15.
 */
public class CharacterSheet {
	public Map<Integer, Integer> classID; //Placeholders
	public Integer raceID;
    public Map<String, Integer> abilities;
	public Vector<Integer> featID;

    public CharacterSheet() {
	    this.resetAbilities();
	    this.featID = new Vector<>();
	    this.classID = new HashMap<>();
	    this.raceID = null;
    }

	public void levelUp(int classID) {
		// TODO: Check prerequisites if multiclassing
		this.classID.compute(classID, (k, v) -> (v == null) ? 1 : v + 1);
	}

	/*
	 * Utilities for ability score manipulation
	 */

	public void resetAbilities() {
	    this.abilities = new HashMap<>();
        this.abilities.put("STR", 10);
        this.abilities.put("DEX", 10);
        this.abilities.put("CON", 10);
        this.abilities.put("INT", 10);
        this.abilities.put("WIS", 10);
        this.abilities.put("CHA", 10);
	}

    public void setAbility(String abilityName, int value) {
        this.abilities.replace(abilityName, value);
    }

    public void incrementAbility(String abilityName) {
	    // Increments ability abilityName by 1
        this.abilities.computeIfPresent(abilityName, (x, oldValue) -> oldValue + 1);
    }

	public void decrementAbility(String abilityName) {
		// Decrements ability abilityName by 1
		this.abilities.computeIfPresent(abilityName, (x, oldValue) -> oldValue - 1);
	}

	/*
	 * Utilities for feat manipulation
	 */

	public void acquireFeat(int id) {
		// TODO: check for prerequisites
		this.featID.add(id);
	}

    public static void main(String[] args) {
        CharacterSheet c = new CharacterSheet();
	    c.incrementAbility("STR");
	    c.setAbility("INT", 14);
	    c.acquireFeat(2);
        System.out.println(c.abilities);
	    System.out.println(c.featID);
	    c.levelUp(1);
	    c.levelUp(2);
	    c.levelUp(2);
	    System.out.println(c.classID);
    }
}



