package project.service.character;

import org.json.simple.JSONObject;
import project.service.dbLookup.Lookup;
import project.service.globals.AbilityID;
import project.service.globals.AdvancementTable;
import project.service.globals.SavingThrowID;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by BjornBjarnsteins on 10/2/15.
 */
public class CharacterSheet {

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
	public Map<String, Integer> AC;
	public Map<String, Integer> grapple;

	// Fluff stuff
	public String name;
	public String gender;
	public Integer age; // Should we incorporate aging rules?
	public String height; // Maybe do something more with this?
	public String bio;
	public String appearance;

    public CharacterSheet() {
	    this.find = new Lookup();

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

	    this.AC = new HashMap<>();
	    this.grapple = new HashMap<>();

	    this.name = "";
	    this.gender = "";
	    this.age = null;
	    this.height = "";
	    this.bio = "";
	    this.appearance = "";

	    this.update();
    }

	// TODO: Construct sheet from JSON

	public void levelUp(int classID) {
		// TODO: Check prerequisites if multiclassing
		this.classID.compute(classID, (k, v) -> (v == null) ? 1 : v + 1); //Increment level counter
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

	public Integer updateBAB() {
		this.BAB = 0;

		for (int i : this.classID.keySet()) {
			ResultSet advancement = this.find.advTableByClassID(i,this.classID.get(i));

			Integer ClassBAB = 0; // Get the first number
			try {
				ClassBAB = Integer.valueOf(advancement.getString("base_attack_bonus").split("/")[0]);
			} catch (SQLException e) {
				e.printStackTrace();
			}

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

	public int getAC() {
		// TODO: Make this factor in item dex caps
		return this.AC.values().stream().reduce(0, (a, b) -> a + b);
	}

	public int getTouchAC() {
		return 10 + this.abilityScores.get(AbilityID.DEX).modifier;
	}

	public int getFlatFootedAC() {
		return this.getAC() - this.abilityScores.get(AbilityID.DEX).modifier;
	}

	public int getGrappleModifier() {
		return this.grapple.values()
				       .stream()
				       .reduce(0, (a, b) -> a + b);
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

	public void pickupMundaneItem(String itemName) {
		ResultSet rs = this.find.mundaneItem(itemName + "/exact");
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

	/*
	 * JSON dumper
	 */
	public JSONObject toJSON() {
		JSONObject sheet = new JSONObject();
		sheet.put("name", this.name);
		sheet.put("gender", this.gender);
		sheet.put("age", this.age);
		sheet.put("height", this.height);
		sheet.put("bio", this.bio);
		sheet.put("appearance", this.appearance);
		sheet.put("classes", this.classID);
		sheet.put("race", this.raceID);

		JSONObject abilityJSON = new JSONObject();
		for (AbilityScore ab : this.abilityScores.values()) {
			abilityJSON.put(ab.shortName, ab.bonuses);
		}
		sheet.put("abilityScores", abilityJSON);

		JSONObject saveJSON = new JSONObject();
		for (SavingThrow st : this.savingThrows.values()) {
			saveJSON.put(st.shortName, st.bonuses);
		}
		sheet.put("savingThrows", saveJSON);

		JSONObject skillJSON = new JSONObject();
		for (Skill s : this.skills.values()) {
			skillJSON.put(s.name, s.bonuses);
		}
		sheet.put("skills", skillJSON);

		sheet.put("feats", this.feats.keySet());
		sheet.put("items", this.inventory);
		sheet.put("equipped", this.equipped);
		sheet.put("maxHP", this.maxHP);
		sheet.put("currentHP", this.currentHP);
		sheet.put("tempHP", this.tempHP);
		sheet.put("AC", this.AC);
		sheet.put("grapple", this.grapple);
		System.out.println(sheet.toJSONString());
		return sheet;
	}

	/*
	 * Convenience functions for retrieving values from containers
	 */

	public AbilityScore get(AbilityID id) {
		return this.abilityScores.get(id);
	}

	public SavingThrow get(SavingThrowID id) {
		return this.savingThrows.get(id);
	}



	public static void main(String[] args) {
		CharacterSheet c = new CharacterSheet();
		c.abilityScores.get(AbilityID.WIS).bonuses.put("Base Score", 14);
		System.out.println(c.abilityScores.get(AbilityID.STR));
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
		System.out.println("Ref: " + c.savingThrows.get(SavingThrowID.REF).totalValue);
		System.out.println("Will: " + c.savingThrows.get(SavingThrowID.WILL).totalValue);
		c.toJSON();
    }
}



