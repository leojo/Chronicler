package project.service.spell;

import project.service.dbLookup.OfflineResultSet;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by leo on 25.11.2015.
 */
public class SpellList {
    private final ArrayList<Collection<Spell>> spells;

    public SpellList(OfflineResultSet ors){
        ors.beforeFirst();
        while(ors.next()){
            Spell s =
        }
    }
}
