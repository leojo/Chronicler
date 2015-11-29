package project.persistence.feat;

import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;

/**
 * Created by leo on 28.11.2015.
 */
public class Feat {
    private final String id;
    private String name;
    private final String type;
    private final String multiple;
    private final String stack;
    private final String choice;
    private final String prerequisite;
    private final String benefit;
    private final String normal;
    private final String special;
    private final String fullText;
    private final String reference;
    private String selectedChoice = "Please select";

    public Feat(String featDetails) {
        String id = "";
        if(featDetails.contains(":")){
            // Loading a feat
            id = featDetails.split(":")[0];
            selectedChoice = featDetails.split(":")[1];
        }else{
            // creating a new feat
            id = featDetails;
        }
        Lookup find = new Lookup();
        OfflineResultSet feat = find.feat(id + "/exact");
        feat.first();
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

    public Feat(int ID){this(ID+"");}

    public String getId() {
        return id;
    }

    public String getName() {
        if(choice.equalsIgnoreCase("none")) return name;
        return name+" ["+selectedChoice+"]";
    }

    public void setName(String name) {
        if(this.choice.equalsIgnoreCase("none")) return;
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public String getMultiple() {
        return multiple;
    }

    public String getStack() {
        return stack;
    }

    public String getChoice() {
        return choice;
    }

    public String getPrerequisite() {
        return prerequisite;
    }

    public String getBenefit() {
        return benefit;
    }

    public String getNormal() {
        return normal;
    }

    public String getSpecial() {
        return special;
    }

    public String getFullText() {
        return fullText;
    }

    public String getReference() {
        return reference;
    }

    @Override
    public String toString() {
        if(this.choice.equalsIgnoreCase("None")) return this.id;
        return this.id+":"+this.selectedChoice+"";
    }
}
