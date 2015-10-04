package character;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.stream.IntStream;

/**
 * Created by BjornBjarnsteins on 10/2/15.
 */
public class CharacterSheet {
	// TODO: Make this enum
	public final String[] abilities = {"STR", "DEX", "CON", "INT", "WIS", "CHA"};
	public enum SavingThrowID {FORT, REF, WILL}

	class Skill {
		String name;
		String baseSkill;
		int id;
		boolean trainedOnly;
		boolean armorPenalty;

		Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
		int totalValue;

		public Skill(int id) {
			this.id = id;
			// TODO: This should load the correct values from database
			this.name = "Appraise";
			this.baseSkill = "INT";
			this.trainedOnly = false;
			this.armorPenalty = false;

			this.totalValue = 0;
			this.bonuses = new HashMap<>();
			this.bonuses.put("Ranks", 0); // TODO: Make this more general, probably with a final list of sources
		}

		public void update(CharacterSheet character) {
			this.totalValue = 0;
			for (int v : this.bonuses.values()) {
				this.totalValue += v;
			}
		}

		@Override
		public String toString() {
			return "Skill{" +
					       "name='" + name + '\'' +
					       '}';
		}
	}

	class SavingThrow {
		String name;
		String baseSkill;
		Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
		int totalValue;

		public SavingThrow(SavingThrowID id) {
			// id is the enum value
			switch (id) {
				case FORT:
					this.name = "Fortitude";
					this.baseSkill = "CON";
					break;
				case REF:
					this.name = "Reflex";
					this.baseSkill = "DEX";
					break;
				case WILL:
					this.name = "Will";
					this.baseSkill = "WIS";
					break;
			}

			this.bonuses = new HashMap<>();
			this.bonuses.put("Base Save", 0);
			this.bonuses.put("Ability Modifier", 0);
		}

		public void update(CharacterSheet character) {
			this.totalValue = 0;
			for (int v : this.bonuses.values()) {
				this.totalValue += v;
			}
		}

		@Override
		public String toString() {
			return "SavingThrow{" +
					       "totalValue=" + totalValue +
					       ", name='" + name + '\'' +
					       ", bonuses=" + bonuses +
					       '}';
		}
	}


	public Map<Integer, Integer> classID; //Placeholders
	public Integer raceID;
    public Map<String, Integer> abilityScores;
	public Vector<Skill> skills;
	public Vector<Integer> featID;
	public Vector<SavingThrow> savingThrows;

    public CharacterSheet() {
	    this.resetAbilities();
	    this.resetSkills();
	    this.classID = new HashMap<>();
	    this.featID = new Vector<>();
	    this.savingThrows = new Vector<>();
	    this.raceID = null;
	    this.update();
    }

	public void levelUp(int classID) {
		// TODO: Check prerequisites if multiclassing
		this.classID.compute(classID, (k, v) -> (v == null) ? 1 : v + 1);
	}

	/*
	 * Functions to update calculated fields
	 */

	public void update() {
		this.calculateSavingThrowID();
	}

	public void calculateSavingThrowID() {
		// getBaseSave();
	}

	public void calculateSavingThrow() {

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
		this.skills = new Vector<>();
		IntStream skillList = IntStream.range(1, 10); // TODO: Make these items load from database
		skillList.forEach(skillID -> {
			this.skills.add(new Skill(skillID));
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



