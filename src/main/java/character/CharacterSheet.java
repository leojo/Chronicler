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
	// Maybe these should be global
	public enum AbilityID {STR, DEX, CON, INT, WIS, CHA}
	public enum SavingThrowID {FORT, REF, WILL}

	class AbilityScore {
		String name;
		String shortName;

		Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
		int totalValue;
		int modifier;

		public AbilityScore(AbilityID id) {
			switch (id) {
				case STR:
					this.name = "Strength";
					this.shortName = "STR";
					break;
				case DEX:
					this.name = "Dexterity";
					this.shortName = "DEX";
					break;
				case CON:
					this.name = "Constitution";
					this.shortName = "CON";
					break;
				case INT:
					this.name = "Intelligence";
					this.shortName = "INT";
					break;
				case WIS:
					this.name = "Wisdom";
					this.shortName = "WIS";
					break;
				case CHA:
					this.name = "Charisma";
					this.shortName = "CHA";
					break;
			}
			this.totalValue = 0;
			this.modifier = 0;
			this.bonuses = new HashMap<>();
			this.bonuses.put("Base Score", 10);
		}

		public void update(CharacterSheet character) {
			this.totalValue = 0;
			for (int v : this.bonuses.values()) {
				this.totalValue += v;
			}

			this.modifier = (this.totalValue / 2) - 5;
		}

		@Override
		public String toString() {
			return "AbilityScore{" +
					       "shortName='" + shortName + '\'' +
					       ", totalValue=" + totalValue +
					       ", bonuses=" + bonuses +
					       '}';
		}
	}

	class Skill {
		String name;
		AbilityID baseSkill;
		int id;
		boolean trainedOnly;
		boolean armorPenalty;

		Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
		int totalValue;

		public Skill(int id) {
			this.id = id;
			// TODO: This should load the correct values from database
			this.name = "Appraise";
			this.baseSkill = AbilityID.INT;
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
			return "Ability{" +
					       "name='" + name + '\'' +
					       '}';
		}
	}

	class SavingThrow {
		String name;
		AbilityID baseSkill;
		Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
		int totalValue;

		public SavingThrow(SavingThrowID id) {
			// id is the enum value
			switch (id) {
				case FORT:
					this.name = "Fortitude";
					this.baseSkill = AbilityID.CON;
					break;
				case REF:
					this.name = "Reflex";
					this.baseSkill = AbilityID.DEX;
					break;
				case WILL:
					this.name = "Will";
					this.baseSkill = AbilityID.WIS;
					break;
			}

			this.bonuses = new HashMap<>();
			// Probably take all this out and do this in the update function
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
    public Map<AbilityID, AbilityScore> abilityScores;
	public Map<Integer, Skill> skills;
	public Vector<Integer> featID; // TODO: Refactor to map for consistency
	public Map<SavingThrowID, SavingThrow> savingThrows;
	public String name;
	public String gender;
	public int age; // Should we incorporate aging rules?
	public String height; // Maybe do something more with this?
	public String bio;
	public String appearance;

    public CharacterSheet() {
	    this.resetAbilities();
	    this.resetSkills();
	    this.classID = new HashMap<>();
	    this.featID = new Vector<>();
	    this.resetSavingThrows();
	    this.raceID = null;

	    this.name = "";
	    this.gender = "";
	    this.age = 0;
	    this.height = "";
	    this.bio = "";
	    this.appearance = "";

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
		this.abilityScores.values().stream().forEach(v -> v.update(this));
		this.savingThrows.values().stream().forEach(v -> v.update(this));
	}

	/*
	 * Ability score
	 */

	public void resetAbilities() {
	    this.abilityScores = new HashMap<>();
		for (AbilityID id : AbilityID.values()) {
			this.abilityScores.put(id, new AbilityScore(id));
		}
	}

	/*
	 * Saving throws
	 */

	public void resetSavingThrows() {
		this.savingThrows = new HashMap<>();
		for (SavingThrowID id: SavingThrowID.values()) {
			this.savingThrows.put(id, new SavingThrow(id));
		}
	}

	/*
	 * Feats
	 */

	public void acquireFeat(int id) {
		// TODO: check for prerequisites
		this.featID.add(id);
	}

	/*
	 * Skills
	 */

	public void resetSkills() {
		this.skills = new HashMap<>();
		IntStream skillList = IntStream.range(1, 10); // TODO: Make these items load from database
		skillList.forEach(skillID -> {
			this.skills.put(skillID, new Skill(skillID));
		});
	}

    public static void main(String[] args) {
        CharacterSheet c = new CharacterSheet();
	    c.acquireFeat(2);
        System.out.println(c.abilityScores.get(AbilityID.STR));
	    System.out.println(c.savingThrows.get(SavingThrowID.FORT));
	    System.out.println(c.featID);
	    c.levelUp(1);
	    c.levelUp(2);
	    c.levelUp(2);
	    System.out.println(c.classID);
	    System.out.println(c.skills);
    }
}



