package character;

import dbLookup.Lookup;
import globals.AbilityID;
import globals.SavingThrowID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;
import java.util.function.IntSupplier;

import static globals.AbilityID.*;

/**
 * Created by BjornBjarnsteins on 10/2/15.
 */
public class CharacterSheet {

	class AbilityScore {
		String name;
		String shortName;

		int baseValue;
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
		AbilityScore baseSkill;
		int id;
		boolean trainedOnly;
		boolean armorPenalty;
		int ranks;

		Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
		int totalValue;

		public Skill(CharacterSheet character, ResultSet skillInfo) throws SQLException {
			this.id = skillInfo.getInt("id");
			this.name = skillInfo.getString("name");
			this.baseSkill = character.abilityScores.get(fromString(skillInfo.getString("base_skill")));
			this.trainedOnly = skillInfo.getBoolean("trained_only");
			this.armorPenalty = skillInfo.getBoolean("armor_check_penalty");

			this.ranks = 0;
			this.totalValue = 0;
			this.bonuses = new HashMap<>();
			this.update(character);
		}

		public void update(CharacterSheet character) {
			this.bonuses.put("Ranks", this.ranks);
			if (this.baseSkill != null) this.bonuses.put("Ability Modifier", this.baseSkill.totalValue);

			this.totalValue = 0;
			for (int v : this.bonuses.values()) {
				this.totalValue += v;
			}
		}

		@Override
		public String toString() {
			return "Skill{" +
					       "totalValue=" + totalValue +
					       ", name='" + name + '\'' +
					       ", baseSkill=" + baseSkill +
					       '}';
		}
	}

	class SavingThrow {
		String name;
		String shortName;
		AbilityScore baseSkill;
		Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
		int totalValue;

		public SavingThrow(CharacterSheet character, SavingThrowID id) {
			// id is the enum value
			switch (id) {
				case FORT:
					this.name = "Fortitude";
					this.shortName = "FORT";
					this.baseSkill = character.abilityScores.get(AbilityID.CON);
					break;
				case REF:
					this.name = "Reflex";
					this.shortName = "REF";
					this.baseSkill = character.abilityScores.get(AbilityID.DEX);
					break;
				case WILL:
					this.name = "Will";
					this.shortName = "WILL";
					this.baseSkill = character.abilityScores.get(AbilityID.WIS);
					break;
			}

			this.bonuses = new HashMap<>();
			this.update(character);
		}

		public void update(CharacterSheet character) {
			// TODO: Load base save from class template
			this.bonuses.put("Base Save", 0);
			this.bonuses.put("Ability Modifier", this.baseSkill.totalValue);

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

	public int level;

	public Map<Integer, Integer> classID; //Placeholders
	public Integer raceID;
    public Map<AbilityID, AbilityScore> abilityScores;
	public Map<Integer, Skill> skills;
	public Map<Integer, ResultSet> feats; // TODO: Refactor to map for consistency
	public Map<SavingThrowID, SavingThrow> savingThrows;

	public Vector<String> inventory; // TODO: Change this to use an Item class
	public Vector<String> equipped;

	public Lookup find;

	// Combat stats
	public Map<Integer, Integer> hitDice;
	public int maxHP;
	public int currentHP;
	public int tempHP;

	public Vector<Integer> BAB;

	// Fluff stuff
	public String name;
	public String gender;
	public Integer age; // Should we incorporate aging rules?
	public String height; // Maybe do something more with this?
	public String bio;
	public String appearance;

    public CharacterSheet() {
	    this.find = new Lookup("data/dnd.sqlite");

	    this.level = 1;

	    this.resetAbilities();
	    try {
		    this.resetSkills();
	    } catch (SQLException e) {
		    e.printStackTrace();
	    }
	    this.classID = new HashMap<>();
	    this.feats = new HashMap<>();
	    this.resetSavingThrows();
	    this.raceID = null;

	    this.inventory = new Vector<>();

	    this.hitDice = new HashMap<>();
	    this.maxHP = 0;
	    this.currentHP = 0;
	    this.tempHP = 0;

	    this.BAB = new Vector<>();

	    this.name = "";
	    this.gender = "";
	    this.age = null;
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
		this.skills.values().stream().forEach(v -> v.update(this));
	}

	/*
	 * Ability score
	 */

	public void resetAbilities() {
	    this.abilityScores = new HashMap<>();
		for (AbilityID id : values()) {
			this.abilityScores.put(id, new AbilityScore(id));
		}
	}

	/*
	 * Saving throws
	 */

	public void resetSavingThrows() {
		this.savingThrows = new HashMap<>();
		for (SavingThrowID id: SavingThrowID.values()) {
			this.savingThrows.put(id, new SavingThrow(this, id));
		}
	}

	/*
	 * Feats
	 */

	public void acquireFeat(ResultSet feat) {
		// TODO: check for prerequisites
		try {
			this.feats.put(feat.getInt("id"), feat);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/*
	 * Skills
	 */

	public void resetSkills() throws SQLException {
		this.skills = new HashMap<>();
		ResultSet rs = this.find.skill("*");

		while (rs.next()) {
			int skillID = rs.getInt("id");
			this.skills.put(skillID, new Skill(this, rs));
		}
	}

	/*
	 * Inventory
	 */

	// NOTE: THIS IS ALL SUBJECT TO CHANGE AS SOON AS I FIND A BETTER WAY TO DO THIS
	// TODO: Find a better way to do this

	public void pickupItem(ResultSet item) {
		try {
			this.inventory.add(item.getString("name"));
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void pickupItem(String itemName) {
		this.inventory.add(itemName);
	}

	public void dropItem(String itemName) {
		this.inventory.remove(itemName);
	}

	public void equipItem(String itemName) {
		this.equipped.add(itemName);
	}


    public static void main(String[] args) {
        CharacterSheet c = new CharacterSheet();
	    c.abilityScores.get(AbilityID.WIS).bonuses.put("Base Score", 14);
        System.out.println(c.abilityScores.get(STR));
	    System.out.println(c.savingThrows.get(SavingThrowID.FORT));
	    ResultSet ImprovedInitiative = c.find.feat("Improved Initiative");
	    c.acquireFeat(ImprovedInitiative);
	    System.out.println(c.feats.keySet());
	    c.levelUp(1);
	    c.levelUp(2);
	    c.levelUp(2);
	    System.out.println(c.classID);
	    c.update();
	    c.skills.forEach((k, v) -> System.out.println(v));
    }
}



