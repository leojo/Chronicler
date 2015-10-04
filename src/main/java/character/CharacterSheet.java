package character;

import javax.validation.constraints.Null;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Created by BjornBjarnsteins on 10/2/15.
 */
public class CharacterSheet {
	public final String[] abilities = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};

	public Map<Integer, Integer> classID; //Placeholders
	public Integer raceID;
    public Map<String, Integer> abilityScores;
	public Map<Integer, Integer> skills;
	public Vector<Integer> featID;

    public CharacterSheet() {
	    this.resetAbilities();
	    this.resetSkills();
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
	    this.abilityScores = new HashMap<>();
		for (String name : abilities) {
			this.abilityScores.put(name, 10);
		}
	}

    public void setAbility(String abilityName, int value) {
        this.abilityScores.replace(abilityName, value);
    }

    public void incrementAbility(String abilityName) {
	    // Increments ability abilityName by 1
        this.abilityScores.computeIfPresent(abilityName, (x, oldValue) -> oldValue + 1);
    }

	public void decrementAbility(String abilityName) {
		// Decrements ability abilityName by 1
		this.abilityScores.computeIfPresent(abilityName, (x, oldValue) -> oldValue - 1);
	}

	/*
	 * Utilities for feat manipulation
	 */

	public void acquireFeat(int id) {
		// TODO: check for prerequisites
		this.featID.add(id);
	}

	/*
	 * Utilities for skill manipulation
	 */

	public void resetSkills() {
		this.skills = new HashMap<>();
		IntStream skillList = IntStream.range(1, 10); // TODO: Make these items load from database
		skillList.forEach(skill -> {
			this.skills.put(skill, 0);
		});
	}

    public static void main(String[] args) {
        CharacterSheet c = new CharacterSheet();
	    c.incrementAbility("STR");
	    c.setAbility("INT", 14);
	    c.acquireFeat(2);
        System.out.println(c.abilityScores);
	    System.out.println(c.featID);
	    c.levelUp(1);
	    c.levelUp(2);
	    c.levelUp(2);
	    System.out.println(c.classID);
	    System.out.println(c.skills);
    }
}



