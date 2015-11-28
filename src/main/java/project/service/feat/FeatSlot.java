package project.service.feat;

import java.util.ArrayList;

/**
 * Created by leo on 28.11.2015.
 */
public class FeatSlot {
    private final String type;
    private Feat feat;

    public FeatSlot(String slotDescriptor){
        if(slotDescriptor.contains("|")) {
            // Loading a filled feat slot from string.
            String[] info = slotDescriptor.split("|");
            if(info.length != 2){
                System.err.println("Tried to load feat with broken string");
                this.type = "Failed to load";
            }
            else{
                this.feat = new Feat(info[0]);
                this.type = info[1];
            }
        } else {
            // loading or creating an empty feat slot.
            this.type = slotDescriptor;
        }
    }

    public ArrayList<Feat> getPossibleFeats(ArrayList<Feat> feats){
        // TODO: implement this.
        return null;
    }

    @Override
    public String toString() {
        if(feat == null) return type;
        return feat.toString()+"|"+type;
    }
}
