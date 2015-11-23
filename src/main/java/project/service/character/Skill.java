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

	public Skill(CharacterSheet character, OfflineResultSet skillInfo) throws SQLException {
		this.id = skillInfo.getInt("id");
		this.name = skillInfo.getString("name");
		this.baseSkill = character.abilityScores.get(AbilityID.fromString(skillInfo.getString("base_skill")));
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

		this.totalValue = this.bonuses.values().stream().reduce(0, (a, b) -> a + b);
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
