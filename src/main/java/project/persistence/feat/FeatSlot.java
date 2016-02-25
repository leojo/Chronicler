package project.persistence.feat;

import project.persistence.dbLookup.Lookup;
import project.persistence.dbLookup.OfflineResultSet;

import java.util.ArrayList;

/**
 * Created by leo on 28.11.2015.
 */
public class FeatSlot {
    private String type;
    private Feat feat;

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
