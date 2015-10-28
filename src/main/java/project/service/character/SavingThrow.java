package project.service.character;

import project.service.globals.AbilityID;
import project.service.globals.AdvancementTable;
import project.service.globals.SavingThrowID;

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

