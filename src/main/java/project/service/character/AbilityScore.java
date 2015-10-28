package project.service.character;

import project.service.globals.AbilityID;

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
		this.totalValue = 0;
		this.modifier = 0;
		this.bonuses = new HashMap<>();
		this.bonuses.put("Base Score", 10);
	}

	public void update(CharacterSheet character) {
		this.totalValue = this.bonuses.values().stream().reduce(0, (a, b) -> a + b);

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
