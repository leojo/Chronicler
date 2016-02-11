package project.persistence.dbRestUtils;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import project.persistence.enums.AbilityID;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Leo on 10.2.2016.
 *
 */

public class Skill {
	private String name;
    private String description;
    private String action;
    private String try_again;
    private String special;
    private String synergy;
    private AbilityID abilityID;
	private boolean trainedOnly;
	private boolean armorPenalty;
	private int ranks;

	private Map<String, Integer> bonuses; // Map<source, value> of bonuses to this skill
	private int totalValue;

    public Skill(){ /* Empty constructor for JSON */ }

	public Skill(String name, AbilityID abilityID, boolean trainedOnly, boolean armorPenalty, String description, String synergy, String action, String try_again, String special){
		this.name = name;
        this.description = description;
        this.synergy = synergy;
        this.abilityID = abilityID;
		this.trainedOnly = trainedOnly;
		this.armorPenalty = armorPenalty;
        this.action = action;
        this.try_again = try_again;
        this.special = special;

		this.ranks = 0;
		this.totalValue = 0;
		this.bonuses = new HashMap<>();
	}

    //<editor-fold desc="Getters and Setters">
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public AbilityID getAbilityID() {
        return abilityID;
    }

    public void setAbilityID(AbilityID abilityID) {
        this.abilityID = abilityID;
    }

    public boolean isTrainedOnly() {
        return trainedOnly;
    }

    public void setTrainedOnly(boolean trainedOnly) {
        this.trainedOnly = trainedOnly;
    }

    public boolean isArmorPenalty() {
        return armorPenalty;
    }

    public void setArmorPenalty(boolean armorPenalty) {
        this.armorPenalty = armorPenalty;
    }

    public int getRanks() {
        return ranks;
    }

    public void setRanks(int ranks) {
        this.ranks = ranks;
    }

    public Map<String, Integer> getBonuses() {
        return bonuses;
    }

    public void setBonuses(Map<String, Integer> bonuses) {
        this.bonuses = bonuses;
    }

    public int getTotalValue() {
        return totalValue;
    }

    public void setTotalValue(int totalValue) {
        this.totalValue = totalValue;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getSynergy() {
        return synergy;
    }

    public void setSynergy(String synergy) {
        this.synergy = synergy;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTry_again() {
        return try_again;
    }

    public void setTry_again(String try_again) {
        this.try_again = try_again;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }
    //</editor-fold>
}
