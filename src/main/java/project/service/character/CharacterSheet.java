package project.service.character;

import org.json.simple.JSONObject;
import project.service.dbLookup.Lookup;
import project.service.dbLookup.OfflineResultSet;
import project.service.globals.AbilityID;
import project.service.globals.AdvancementTable;
import project.service.globals.SavingThrowID;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * Created by BjornBjarnsteins on 10/2/15.
 */
public class CharacterSheet {

	// Added this and removed a lot of global variables instead because
	// since we need to use the bean anyway for html display reason, we
	// shouldn't have a bunch of global variables that are basically just
	// the variables of the bean anyway.
	public CharacterBean bean;

	// Lookup object for database stuff:
	public Lookup find;

	// VARIABLES THAT ARE NOT COMMON WITH BEAN AND THEREFORE NEED TO BE UPDATED
	// Note: These need special edit function in the system if they are to be edited.
	public Map<Integer, Integer> classID; //Placeholders
	public Integer raceID;
	public Map<AbilityID, AbilityScore> abilityScores;
	public Map<SavingThrowID, SavingThrow> savingThrows;
	// Combat stats
	public Map<Integer, Integer> hitDice;
	public Map<String, Integer> AC;
	public Map<String, Integer> grapple;
	public Vector<OfflineResultSet> inventory; // TODO: Change this to use an Item class
	public Vector<OfflineResultSet> equipped;


	// TODO: Make compatible with bean
	public Map<Integer, Skill> skills;
	public Map<Integer, OfflineResultSet> feats; // TODO: Refactor to map for consistency
	// items

	// bio, appearance??? Do something with this?


    public CharacterSheet(CharacterBean bean, boolean fresh) {
		this.find = new Lookup();

		if(fresh) this.bean = initializeBean(bean);
		else {

			this.bean = bean;

			this.resetAbilities();
			try {
				this.resetSkills();
			} catch (SQLException e) {
				e.printStackTrace();
			}

			this.resetSavingThrows();

			this.classID = new HashMap<>();
			this.feats = new HashMap<>();

			this.raceID = null;

			this.inventory = new Vector<>();

			this.hitDice = new HashMap<>();

			this.AC = new HashMap<>();
			this.grapple = new HashMap<>();
			/*
			this.bio = "";
			this.appearance = "";
			*/
			this.update();
		}
    }

	private CharacterBean initializeBean(CharacterBean bean) {
		String classname  = bean.getClassName();
		String race = bean.getRace();
		find.advTable(classname, 1);


		// might even be unneccessary? It seems that the bean sets these as 0 and "" by default
		/*bean.setName("");
		bean.setGender("");
		bean.setAge("");
		bean.setHeight("");
		bean.setMaxHp(0);
		bean.setCurrHp(0);
		bean.setTempHP(0);*/

		return bean;
	}

	private void updateBean() {
		this.bean.setSTR(this.abilityScores.get(AbilityID.STR).totalValue);
		this.bean.setDEX(this.abilityScores.get(AbilityID.DEX).totalValue);
		this.bean.setCON(this.abilityScores.get(AbilityID.CON).totalValue);
		this.bean.setINT(this.abilityScores.get(AbilityID.INT).totalValue);
		this.bean.setWIS(this.abilityScores.get(AbilityID.WIS).totalValue);
		this.bean.setCHA(this.abilityScores.get(AbilityID.CHA).totalValue);

		this.bean.setFort(this.savingThrows.get(SavingThrowID.FORT).totalValue);
		this.bean.setWill(this.savingThrows.get(SavingThrowID.WILL).totalValue);
		this.bean.setReflex(this.savingThrows.get(SavingThrowID.REF).totalValue);

		this.bean.setAc(this.getAC());
		this.bean.setFlatAc(this.getFlatFootedAC());
		this.bean.setTouchAc(this.getTouchAC());
		this.bean.setGrapple(this.getGrappleModifier());
	}

	public CharacterBean getBean() {
		updateBean();
		return this.bean;
	}

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
		int BAB = 0;

		for (int i : this.classID.keySet()) {
			OfflineResultSet advancement = this.find.advTableByClassID(i,this.classID.get(i));

			Integer ClassBAB = 0; // Get the first number
			ClassBAB = Integer.valueOf(advancement.getString("base_attack_bonus").split("/")[0]);

			BAB += ClassBAB;
		}

		this.bean.setBab(BAB);
		return BAB;
	}

	public Vector<Integer> getBAB() {
		Vector<Integer> BAB = new Vector<>();
		int currentBAB = this.bean.getBab();

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

	public void acquireFeat(OfflineResultSet feat) {
		// TODO: check for prerequisites
		this.feats.put(feat.getInt("id"), feat);
	}

	/*
	 * Skills
	 */

	public void resetSkills() throws SQLException {
		this.skills = new HashMap<>();
		OfflineResultSet rs = this.find.skill("*");

		while (rs.next()) {
			int skillID = rs.getInt("id");
			this.skills.put(skillID, new Skill(this, rs));
		}
	}

	/*
	 * Inventory
	 */

	public void pickupItem(OfflineResultSet item) {
		this.inventory.add(item);
	}

	public void pickupMundaneItem(String itemName) {
		OfflineResultSet rs = this.find.mundaneItem(itemName + "/exact");
		this.pickupItem(rs);
	}

	public void dropItem(String itemName) {
		for (OfflineResultSet item : inventory) {
			if (item.getString("name") == itemName) {
                this.inventory.remove(item);
            }

		}
	}

	public void equipItem(String itemName) {
		for (OfflineResultSet item : inventory) {
			if (item.getString("name") == itemName) {
                this.equipped.add(item);
            }
		}
	}

	public void unequipItem(String itemName) {
		for (OfflineResultSet item : inventory) {
			if (item.getString("name") == itemName) {
                this.equipped.remove(item);
            }
		}
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

    }
}



