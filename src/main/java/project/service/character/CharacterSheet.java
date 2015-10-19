package project.service.character;

import org.json.JSONObject;
import project.service.dbLookup.Lookup;
import project.service.globals.AbilityID;
import project.service.globals.AdvancementTable;
import project.service.globals.SavingThrowID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import static project.service.globals.AbilityID.*;

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
					this.shortName = "Fort";
					this.baseSkill = character.abilityScores.get(AbilityID.CON);
					break;
				case REF:
					this.name = "Reflex";
					this.shortName = "Ref";
					this.baseSkill = character.abilityScores.get(AbilityID.DEX);
					break;
				case WILL:
					this.name = "Will";
					this.shortName = "Will";
					this.baseSkill = character.abilityScores.get(AbilityID.WIS);
					break;
			}

			this.bonuses = new HashMap<>();
			this.update(character);
		}

		public void update(CharacterSheet character) {
			int BaseSave = 0;
			for (int c : character.classID.keySet()) {
				// TODO: This probably needs optimizing, i.e. minimizing number of times the table is retrieved from db
				AdvancementTable advancement = character.find.advTableByClassID(c);

				Integer ClassSave = Integer.valueOf(advancement.getJSON()                    // Retrieve the JSON
					              .getJSONObject(String.valueOf(character.classID.get(c)))   // Get the JSON for this level only
					              .getString(this.shortName));                                          // Get the BAB for this level

				BaseSave += ClassSave;
			}
			this.bonuses.put("Base Save", BaseSave);
			this.bonuses.put("Ability Modifier", this.baseSkill.modifier);

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

	public Vector<ResultSet> inventory; // TODO: Change this to use an Item class
	public Vector<ResultSet> equipped;

	public Lookup find;

	// Combat stats
	public Map<Integer, Integer> hitDice;
	public int maxHP;
	public int currentHP;
	public int tempHP;

	public int BAB;

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
		this.classID.compute(classID, (k, v) -> (v == null) ? 1 : v + 1); //Increment level counter
	}

	public Integer updateBAB() {
		this.BAB = 0;

		for (int i : this.classID.keySet()) {
			AdvancementTable advancement = this.find.advTableByClassID(i);

			Integer ClassBAB = Integer.valueOf(advancement.getJSON()                    // Retrieve the JSON
					              .getJSONObject(String.valueOf(this.classID.get(i)))   // Get the JSON for this level only
					              .getString("BAB")                                     // Get the BAB for this level
					              .split("/")[0]);                                      // Get the first number

			this.BAB += ClassBAB;
		}

		return this.BAB;
	}

	public Vector<Integer> getBAB() {
		Vector<Integer> BAB = new Vector<>();
		int currentBAB = this.BAB;

		while (currentBAB > 0) {
			BAB.add(currentBAB);
			currentBAB -= 5;
		}

		return BAB;
	}

	/*
	 * Functions to update calculated fields
	 */

	public void update() {
		this.abilityScores.values().stream().forEach(v -> v.update(this));
		this.savingThrows.values().stream().forEach(v -> v.update(this));
		this.skills.values().stream().forEach(v -> v.update(this));
		this.updateBAB();
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

	public void pickupItem(ResultSet item) {
		this.inventory.add(item);
	}

	public void pickupItem(String itemName) {
		ResultSet rs = this.find.item(itemName+"/exact");
		this.pickupItem(rs);
	}

	public void dropItem(String itemName) {
		for (ResultSet item : inventory) {
			try {
				if (item.getString("name") == itemName) {
					this.inventory.remove(item);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}

		}
	}

	public void equipItem(String itemName) {
		for (ResultSet item : inventory) {
			try {
				if (item.getString("name") == itemName) {
					this.equipped.add(item);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public void unequipItem(String itemName) {
		for (ResultSet item : inventory) {
			try {
				if (item.getString("name") == itemName) {
					this.equipped.remove(item);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}


	public static void main(String[] args) {
		CharacterSheet c = new CharacterSheet();
		c.abilityScores.get(AbilityID.WIS).bonuses.put("Base Score", 14);
		System.out.println(c.abilityScores.get(STR));
		System.out.println(c.savingThrows.get(SavingThrowID.FORT));
		ResultSet ImprovedInitiative = c.find.feat("Improved Initiative");
		c.acquireFeat(ImprovedInitiative);
		System.out.println(c.feats.keySet());
		c.classID.put(9, 10);
		c.classID.put(8, 1);
		c.update();
		//c.skills.forEach((k, v) -> System.out.println(v));
		System.out.println(c.getBAB());
		System.out.println("Fort: "+c.savingThrows.get(SavingThrowID.FORT).totalValue);
		System.out.println("Ref: "+c.savingThrows.get(SavingThrowID.REF).totalValue);
		System.out.println("Will: "+c.savingThrows.get(SavingThrowID.WILL).totalValue);
    }
}



