package project.persistence.dbRestUtils;

/**
 * Created by leo on 25.2.2016.
 *
 * The simplest of items, such as some rope, a candle, a ladder... etc.
 */
public class MundaneItem extends Item{
    private String description, shortDesc;

    public void makeShortDesc() {
        if (description.length() > 40){
            shortDesc = description.substring(0, 37);
            shortDesc = shortDesc.substring(0,shortDesc.lastIndexOf(" "))+"...";
        }
        else
            shortDesc = description;
    }

    //<editor-fold desc="Getters and Setters">
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
        makeShortDesc();
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
    //</editor-fold>
}
