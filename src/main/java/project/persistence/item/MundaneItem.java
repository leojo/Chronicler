package project.persistence.item;

/**
 * Created by leo on 25.2.2016.
 *
 * The simplest of items, such as some rope, a candle, a ladder... etc.
 */
public class MundaneItem extends Item{
    private String description, shortDesc;

    //<editor-fold desc="Getters and Setters">
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }
    //</editor-fold>
}
