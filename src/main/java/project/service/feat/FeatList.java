package project.service.feat;

import java.util.ArrayList;

/**
 * Created by leo on 28.11.2015.
 */
public class FeatList {
    private final ArrayList<FeatSlot> feats;

    public FeatList(){
        feats = new ArrayList<FeatSlot>();
    }

    public FeatList(ArrayList<FeatSlot> feats){
        this.feats = feats;
    }

    public FeatList(String featString){
        this();
        String[] featDescriptors = featString.split(";");
        for(String desc : featDescriptors){
            if(desc.equals(""))continue;
            feats.add(new FeatSlot(desc));
        }
    }

    public ArrayList<FeatSlot> getFeats() {
        return feats;
    }

    public void add(FeatSlot feat){
        feats.add(feat);
    }

    @Override
    public String toString() {
        String featString = "";
        for(FeatSlot feat : feats){
            featString += feat.toString()+";";
        }
        return featString;
    }
}
