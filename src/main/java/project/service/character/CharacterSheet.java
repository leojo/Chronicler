package project.service.character;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import project.service.dbLookup.Lookup;
import project.service.dbLookup.OfflineResultSet;
import project.service.globals.AbilityID;
import project.service.globals.SavingThrowID;

import java.io.IOException;
import java.sql.SQLException;
import java.util.*;

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
	public Integer totalClassLevel;
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

		if(fresh) initializeBean(bean);
		else {

			this.bean = bean;

			this.resetSavingThrows();

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

	private void initializeBean(CharacterBean bean) {
		this.bean = bean;
		this.totalClassLevel = 1;
		OfflineResultSet ors = find.playerClass(bean.getClassName());
		ors.first();
		Integer classID = ors.getInt("id");
		String race = bean.getRace();
		this.setRacialMods(race);

		this.classID = new HashMap<>();
		this.levelUp(classID);

		// might even be unneccessary? It seems that the bean sets these as 0 and "" by default
		/*bean.setName("");
		bean.setGender("");
		bean.setAge("");
		bean.setHeight("");
		bean.setMaxHp(0);
		bean.setCurrHp(0);
		bean.setTempHP(0);*/
	}

	private void setRacialMods(String race) {
		if(true)return;
		// TODO: This is broken - low-ish priority
		OfflineResultSet raceData = find.race(race);
		raceData.first();
		ObjectMapper jsonParser = new ObjectMapper();
		HashMap<String,String> abilityBonus = null;
		Collection<String> autoLanguages = null;
		Collection<String> bonusLanguages = null;
		HashMap<String,String> condSkillBonus = null;
		HashMap<String,String> condSaveBonus = null;
		HashMap<String,String> condACBonus = null;
		try {
			abilityBonus = jsonParser.readValue(raceData.getString("abilit_bonus"), HashMap.class);
			HashMap<String,String> languages = jsonParser.readValue(raceData.getString("languages"), HashMap.class);
			autoLanguages = jsonParser.readValue(languages.get("Automatic Languages"), HashMap.class).values();
			bonusLanguages = jsonParser.readValue(languages.get("Bonus Languages"), HashMap.class).values();
			HashMap<String,String> special = jsonParser.readValue(raceData.getString("special"), HashMap.class);
		} catch (JsonMappingException e) {
			System.err.println("Error mapping race info");
			e.printStackTrace(System.err);
			return;
		} catch (JsonParseException e) {
			System.err.println("Error parsing race info");
			e.printStackTrace(System.err);
			return;
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}

		// Iterate through abilityBonuses
		for(String key : abilityBonus.keySet()){
			AbilityID abilityID = AbilityID.fromString(key);
			Integer bonus = Integer.parseInt(abilityBonus.get(key));
			this.abilityScores.get(abilityID).setBonus("racial",bonus);
			this.abilityScores.get(abilityID).update();
		}

		// Iterate through abilityBonuses

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

	// Levels the character up in the given class (given by class ID)
	public void levelUp(int classID) {
		// TODO: Check prerequisites if multiclassing
		if(this.classID == null) this.classID = new HashMap<Integer,Integer>();
		this.classID.compute(classID, (k, v) -> (v == null) ? 1 : v + 1);
		OfflineResultSet currentClass = find.playerClass(classID+"/exact");
		currentClass.first();
		int baseSkillPoints = currentClass.getInt("skill_points")*(this.totalClassLevel==1?4:1);
		if(this.abilityScores == null) this.resetAbilities();
		AbilityID skillAbilityID = AbilityID.fromString(currentClass.getString("skill_points_ability"));
		int bonusSkillPoints = this.abilityScores.get(skillAbilityID).totalValue*(this.totalClassLevel==1?4:1);
		this.bean.setAvailableSkillPoints(baseSkillPoints + bonusSkillPoints);
		this.update();
		if(this.totalClassLevel%4 == 0 && this.totalClassLevel>0){
			this.bean.setAvailableAbilityPoints(this.bean.getAvailableSkillPoints()+1);
		}
		if(this.totalClassLevel%3 == 0 && this.totalClassLevel>0){
			this.bean.setAvailableFeats(this.bean.getAvailableFeats() + 1);
		}
	}

	/*
	 * Functions to update calculated fields
	 */

	public void update() {
		this.abilityScores.values().stream().forEach(v -> v.update());
		//this.savingThrows.values().stream().forEach(v -> v.update(this));
		//this.skills.values().stream().forEach(v -> v.update());
		this.updateBAB();
		this.updateTotalLevel();
	}

	public void updateTotalLevel(){
		this.totalClassLevel = 0;
		for(Integer level : classID.values()){
			totalClassLevel += level;
		}
	}

	public Integer updateBAB() {
		int BAB = 0;

		for (int i : this.classID.keySet()) {
			OfflineResultSet advancement = this.find.advTableByClassID(i,this.classID.get(i));
			advancement.first();
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



