package project.service.character;

import project.service.dbLookup.OfflineResultSet;
import project.service.globals.AbilityID;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by whiskeyjack on 10/28/15.
 */

class Skill {
	String name;
	AbilityScore baseSkill;
	int id;
	boolean trainedOnly;
	boolean armorPenalty;
	int ranks;

	Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
	int totalValue;

	public Skill(CharacterSheet character, OfflineResultSet skillInfo){
		this.id = skillInfo.getInt("id");
		this.name = skillInfo.getString("name");
		if(character.abilityScores == null) character.resetAbilities();
		String skillName = skillInfo.getString("key_ability");
		this.baseSkill = character.abilityScores.get(AbilityID.fromString(skillName));
		this.trainedOnly = skillInfo.getBoolean("trained");
		this.armorPenalty = skillInfo.getBoolean("armor_check");

		this.ranks = 0;
		this.totalValue = 0;
		this.bonuses = new HashMap<>();
		this.update();
	}

	public void update() {
		this.bonuses.put("Ranks", this.ranks);
		if (this.baseSkill != null) this.bonuses.put("Ability Modifier", this.baseSkill.totalValue);

		this.totalValue = this.bonuses.values().stream().reduce(0, (a, b) -> a + b);
	}

	public void setRanks(int ranks){
		this.ranks = ranks;
	}

	public void incrementRanks(){
		this.ranks++;
	}

	public boolean setBonus(String key, int value){
		boolean existingBonus = this.bonuses.containsKey(key);
		this.bonuses.put(key,value);
		this.update();
		return existingBonus;
	}

	public boolean incrementBonus(String key){
		boolean existingBonus = this.bonuses.containsKey(key);
		int value = 1;
		if(existingBonus) value = this.bonuses.get(key)+1;
		this.bonuses.put(key,value);
		this.update();
		return existingBonus;
	}

	public boolean decrementBonus(String key){
		boolean validBonus = this.bonuses.containsKey(key);
		if(validBonus) {
			int value = this.bonuses.get(key)-1;
			this.bonuses.put(key, value);
			this.update();
		}
		return validBonus;
	}

	public boolean removeBonus(String key){
		if(this.bonuses.containsKey(key)){
			this.bonuses.remove(key);
			this.update();
			return true;
		}
		return false;
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
