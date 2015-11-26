package project.service.character;

import project.service.dbLookup.Lookup;
import project.service.dbLookup.OfflineResultSet;
import project.service.enums.AbilityID;
import project.service.enums.SavingThrowID;
import project.service.spell.SpellList;
import project.service.spell.SpellSlotArray;

import java.util.*;

/**
 * Created by BjornBjarnsteins on 10/2/15.
 */
public class CharacterSheet {
	// Git why you no find me?

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
	public Map<Integer, Integer> hitDice; // < 3 , 6 > = "6d3"
	public Map<String, Integer> AC;
	public Map<String, Integer> initiative;
	public Map<String, Integer> grapple;
	public Vector<OfflineResultSet> inventory; // TODO: Change this to use an Item class
	public Vector<OfflineResultSet> equipped;

	// Spells
	public SpellList knownSpells;
	public SpellSlotArray spellSlots;


	// TODO: Make compatible with bean
	public Map<Integer, Skill> skills;
	public Map<Integer, OfflineResultSet> feats; // TODO: Refactor to map for consistency
	// items

	// bio, appearance??? Do something with this?


    public CharacterSheet(CharacterBean bean, boolean fresh) {
		this.find = new Lookup();
		if(fresh) initializeBean(bean);
		else this.loadFromBean(bean);
    }

	private void loadFromBean(CharacterBean bean){
		this.bean = bean;
		this.loadClassIDs();
		this.loadAbilityScores();
		this.loadSavingThrows();

		String knownSpellString = this.bean.getKnownSpells_details();
		if(knownSpellString == null) knownSpellString = "168;101;294;268;176;300;380;360;694";
		String spellSlotString = this.bean.getSpellSlots_details();
		if(spellSlotString == null) spellSlotString = "Wizard:1:168;Wizard:2:101;Wizard:3:294;Wizard:4:268;Wizard:5:176;Wizard:6:300;Wizard:7:380;Wizard:8:360;Wizard:9:694;";

		this.knownSpells = new SpellList(knownSpellString);
		this.spellSlots = new SpellSlotArray(spellSlotString);
		this.update();
	}

	private void loadClassIDs() {
		this.resetClassIDs();
		String level_details = this.bean.getLevel_details();
		if(level_details == null || level_details.equals("")) return;
		String[] classID_level = level_details.split(";");
		// The string should be on the form: "1:3;4:2..." and should be read as
		// the character having 3 levels in class with id 1, 2 levels in class with id 4 etc.
		for(String pair : classID_level){
			if(pair.length()==0) continue; //This should only skip the last line (in the case the string ends with a ;)
			String[] details = pair.split(":");
			Integer id = Integer.parseInt(details[0]);
			Integer level = Integer.parseInt(details[1]);
			this.classID.put(id,level);
		}
	}
	private void storeClassIDs(){
		final String[] level_details = {""};
		this.classID.forEach((k, v) -> level_details[0] += k + ":" + v + ";");
		this.bean.setLevel_details(level_details[0]);
	}

	private void loadAbilityScores() {
		this.resetAbilities();
		String ability_details = this.bean.getAbility_details();
		if(ability_details == null || ability_details.equals("")) return;
		String[] ability_info = ability_details.split(";");
		// The string should be on the form: "XXXkey1:value1,key2:value2,;YYYkey3:value3,;" and should be read as
		// The ability XXX has bonuses key1 and key2 with values value1 and value2 respectively. The ability YYY has bonus key3 with value value3.
		for(String info : ability_info){
			if(info.length()==0)continue;
			AbilityID id = AbilityID.fromString(info.substring(0, 3));
			String[] bonuses = info.substring(3).split(",");
			AbilityScore score = new AbilityScore(id);
			for(String bonus : bonuses){
				if(bonus.length()==0)continue;
				String key = bonus.split(":")[0];
				Integer value = Integer.parseInt(bonus.split(":")[1]);
				score.setBonus(key,value);
			}
			this.abilityScores.put(id,score);
		}
	}

	private void storeAbilityScores(){
		final String[] details = {""};
		this.abilityScores.forEach((k, v) -> details[0] += k.toString() + v.toString() + ";");
		this.bean.setAbility_details(details[0]);
	}

	private void loadSavingThrows() {
		this.resetAbilities();
		String save_details = this.bean.getSave_details();
		if(save_details == null || save_details.equals("")) return;
		String[] save_info = save_details.split(";");
		// The string should be on the form: "XXX)key1:value1,key2:value2,;YYY)key3:value3,;" and should be read as
		// The Save XXX has bonuses key1 and key2 with values value1 and value2 respectively. The save YYY has bonus key3 with value value3.
		for(String info : save_info){
			if(info.length()==0)continue;
			SavingThrowID id = SavingThrowID.valueOf(info.split("[)]")[0]);
			String[] bonuses = info.split("[)]")[1].split(",");
			SavingThrow save = new SavingThrow(this,id);
			for(String bonus : bonuses){
				if(bonus.length()==0)continue;
				String key = bonus.split(":")[0];
				Integer value = Integer.parseInt(bonus.split(":")[1]);
				save.setBonus(key,value);
			}
			this.savingThrows.put(id,save);
		}
	}

	private void loadKnownSpells(){
		String knownSpells = this.bean.getKnownSpells_details();

	}

	private void storeSavingThrows(){
		final String[] details = {""};
		this.abilityScores.forEach((k, v) -> details[0] += k.toString() + ")" + v.toString() + ";");
		this.bean.setAbility_details(details[0]);
	}

	private void initializeBean(CharacterBean bean) {
		this.bean = bean;
		this.totalClassLevel = 1;

		OfflineResultSet ors = find.playerClass(bean.getClassName());
		ors.first();
		Integer classID = ors.getInt("id");
		String race = bean.getRace();
		System.err.println(race);
		this.setRacialMods(race);
		this.classID = new HashMap<>();
		this.levelUp(classID);

		String knownSpellString = this.bean.getKnownSpells_details();
		if(knownSpellString == null) knownSpellString = "168;101;294;268;176;300;380;360;694";
		String spellSlotString = this.bean.getSpellSlots_details();
		if(spellSlotString == null) spellSlotString = "Wizard:1:168;Wizard:2:101;Wizard:3:294;Wizard:4:268;Wizard:5:176;Wizard:6:300;Wizard:7:380;Wizard:8:360;Wizard:9:694;";

		this.knownSpells = new SpellList(knownSpellString);
		this.spellSlots = new SpellSlotArray(spellSlotString);

		this.updateBean();
	}

	private void setRacialMods(String race) {
		// TODO: This is broken - low-ish priority
		OfflineResultSet raceData = find.race(race+"/exact");
		raceData.first();
		Integer speed = raceData.getInt("speed");
		if(speed == null) speed = 30; //TODO: FIX THIS BUG PROPERLY!!!
		this.bean.setSpeed(speed);
		this.applyRacialAbilityBonuses(raceData);
	}

	private void applyRacialAbilityBonuses(OfflineResultSet raceData){
		this.resetAbilities();
		String raw = raceData.getString("ability_bonus");
		if(raw==null) return;
		String[] pairs = raw.split("\n");
		for(String abilityBonusPair : pairs){
			String[] info = abilityBonusPair.split(":");
			AbilityID id = AbilityID.fromString(info[0]);
			Integer bonus = Integer.parseInt(info[1]);
			System.out.println("Applying bonus "+bonus+" to ability "+info[0]);
			this.abilityScores.get(id).setBonus("Racial",bonus);
			this.updateAbilityScores();
		}
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
		this.bean.setInitiative(this.getInitiative());

		this.storeAbilityScores();
		this.storeClassIDs();
		this.storeSavingThrows();

		this.bean.setKnownSpells_details((knownSpells != null ? knownSpells.toString() : ""));
		this.bean.setSpellSlots_details((spellSlots != null ? spellSlots.toString() : ""));
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
		if(this.hitDice == null) this.hitDice = new HashMap<Integer, Integer>();
		Integer hdType = Integer.parseInt(currentClass.getString("hit_die").substring(1));
		Integer hdNum = 1;
		if(this.hitDice.containsKey(hdType)) hdNum+=this.hitDice.get(hdType);
		this.hitDice.put(hdType,hdNum);
		Integer conMod = this.abilityScores.get(AbilityID.CON).modifier;
		if(this.bean.getMaxHp()==0){
			this.bean.setMaxHp(conMod+hdType);
		}else{
			this.bean.setMaxHp(this.bean.getMaxHp()+conMod+hdType/2+1);
		}
		updateBean();
	}

	/*
	 * Functions to update calculated fields
	 */

	public void update() {
		this.updateAbilityScores();
		this.updateSavingThrows();
		this.updateSkills();
		this.updateBAB();
		this.updateAC();
		this.updateInitiative();
		this.updateTotalLevel();
	}

	private Integer getInitiative(){
		this.updateInitiative();
		return this.initiative.values().stream().reduce(0, (a, b) -> a + b);
	}
	private void updateInitiative() {
		if(this.initiative == null) this.initiative = new HashMap<String, Integer>();
	}

	private void updateAbilityScores() {
		if(this.abilityScores==null) this.resetAbilities();
		this.abilityScores.values().stream().forEach(v -> v.update());
	}

	private void updateSavingThrows() {
		if(this.savingThrows == null) this.resetSavingThrows();
		this.savingThrows.values().stream().forEach(v -> v.update(this));
	}

	private void updateSkills() {
		if(this.skills==null) this.resetSkills();
		this.skills.values().stream().forEach(v -> v.update());
	}


	private void updateAC() {
		if(this.AC == null) this.resetAC();
		Integer dex = this.abilityScores.get(AbilityID.DEX).modifier;
		if(dex != null)this.AC.put("DEX",dex);
		this.AC.put("Base",10);
	}

	private void resetAC(){
		this.AC = new HashMap<String,Integer>();
	}

	private void resetClassIDs(){
		this.classID = new HashMap<Integer,Integer>();
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
		if(this.grapple == null) this.resetGrapple();
		return this.grapple.values().stream().reduce(0, (a, b) -> a + b);
	}

	private void resetGrapple() {
		this.grapple = new HashMap<String, Integer>();
		this.grapple.put("TESTING",7);
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
		this.initSavingThrows();
		for (SavingThrowID id: SavingThrowID.values()) {
			this.savingThrows.put(id, new SavingThrow(this, id));
		}
	}

	public void initSavingThrows(){
		this.savingThrows = new HashMap<>();
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

	public void resetSkills(){
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



