package project.service.character;

import project.service.enums.AbilityID;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by whiskeyjack on 10/28/15.
 */

public class AbilityScore {
	String name;
	String shortName;

	public Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
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
		this.totalValue = 10;
		this.modifier = 0;
		this.bonuses = new HashMap<String, Integer>();
		this.bonuses.put("Base Score", 10);
	}

	public void update() {
		this.totalValue = this.bonuses.values().stream().reduce(0, (a, b) -> a + b);
		this.modifier = (this.totalValue / 2) - 5;
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
		final String[] details = {""};
		this.bonuses.forEach((k, v) -> details[0] += k + ":" + v + ",");
		return details[0];
	}
}
