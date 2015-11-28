package project.service.character;

import project.service.dbLookup.OfflineResultSet;
import project.service.enums.AbilityID;
import project.service.enums.SavingThrowID;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by whiskeyjack on 10/28/15.
 */

public class SavingThrow {
	String name;
	String shortName;
	AbilityScore baseSkill;
	Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
	public int totalValue;

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
		for (int c : character.classLevels.keySet()) {
			// TODO: This probably needs optimizing, i.e. minimizing number of times the table is retrieved from db
			OfflineResultSet advancement = character.find.advTableByClassID(c,character.classLevels.get(c));
			advancement.first();
			Integer ClassSave = Integer.valueOf(advancement.getString(this.shortName.toLowerCase() + "_save")); // Get the Save for this level

			BaseSave += ClassSave;
		}
		this.bonuses.put("Base Save", BaseSave);
		this.bonuses.put("Ability Modifier", this.baseSkill.modifier);

		this.totalValue = 0;
		for (int v : this.bonuses.values()) {
			this.totalValue += v;
		}
	}

	public void recalculate(){
		this.totalValue = 0;
		for (int v : this.bonuses.values()) {
			this.totalValue += v;
		}
	}

	public boolean setBonus(String key, int value){
		boolean existingBonus = this.bonuses.containsKey(key);
		this.bonuses.put(key,value);
		this.recalculate();
		return existingBonus;
	}

	public boolean incrementBonus(String key){
		boolean existingBonus = this.bonuses.containsKey(key);
		int value = 1;
		if(existingBonus) value = this.bonuses.get(key)+1;
		this.bonuses.put(key,value);
		this.recalculate();
		return existingBonus;
	}

	public boolean decrementBonus(String key){
		boolean validBonus = this.bonuses.containsKey(key);
		if(validBonus) {
			int value = this.bonuses.get(key)-1;
			this.bonuses.put(key, value);
			this.recalculate();
		}
		return validBonus;
	}

	public boolean removeBonus(String key){
		if(this.bonuses.containsKey(key)){
			this.bonuses.remove(key);
			this.recalculate();
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		final String[] details = {""};
		this.bonuses.forEach((k, v) -> details[0] += k + ":" + v + ",");
		return details[0];
	}
}

