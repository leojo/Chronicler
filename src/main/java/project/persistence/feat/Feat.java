package project.persistence.feat;

import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;

/**
 * Created by leo on 28.11.2015.
 *
 * NOTE: This has yet to be connected to the front end
 */
public class Feat {
    private String id;
    private String name;
    private String type;
    private String multiple;
    private String stack;
    private String choice;
    private String prerequisite;
    private String benefit;
    private String normal;
    private String special;
    private String fullText;
    private String reference;
    private String selectedChoice = "Please select";


    // This constructor assumes an offline resultset with the pointer on the feat itself.
    public Feat(OfflineResultSet feat) {
        this.id = feat.getString("id");
        this.name = feat.getString("name");
        this.type = feat.getString("type");
        this.multiple = feat.getString("multiple");
        this.stack = feat.getString("stack");
        this.choice = feat.getString("choice");
        this.prerequisite = feat.getString("prerequisite");
        this.benefit = feat.getString("benefit");
        this.normal = feat.getString("normal");
        this.special = feat.getString("special");
        this.fullText = feat.getString("full_text");
        this.reference = feat.getString("reference");
    }

    //<editor-fold desc="Getters and Setters">
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMultiple() {
        return multiple;
    }

    public void setMultiple(String multiple) {
        this.multiple = multiple;
    }

    public String getStack() {
        return stack;
    }

    public void setStack(String stack) {
        this.stack = stack;
    }

    public String getChoice() {
        return choice;
    }

    public void setChoice(String choice) {
        this.choice = choice;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public void setPrerequisite(String prerequisite) {
        this.prerequisite = prerequisite;
    }

    public String getBenefit() {
        return benefit;
    }

    public void setBenefit(String benefit) {
        this.benefit = benefit;
    }

    public String getNormal() {
        return normal;
    }

    public void setNormal(String normal) {
        this.normal = normal;
    }

    public String getSpecial() {
        return special;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public String getFullText() {
        return fullText;
    }

    public void setFullText(String fullText) {
        this.fullText = fullText;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public String getSelectedChoice() {
        return selectedChoice;
    }

    public void setSelectedChoice(String selectedChoice) {
        this.selectedChoice = selectedChoice;
    }
    //</editor-fold>

    @Override
    public String toString() {
        if(this.choice.equalsIgnoreCase("None")) return this.id;
        return this.id+":"+this.selectedChoice+"";
    }
}
