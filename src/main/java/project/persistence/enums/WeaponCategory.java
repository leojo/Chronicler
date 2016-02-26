package project.persistence.enums;

/**
 * Created by leo on 23.2.2016.
 */
public enum WeaponCategory {
    Simple,Martial,Exotic;

    public static WeaponCategory fromString(String s){
        if(s.toLowerCase().startsWith("simple"))
            return WeaponCategory.Simple;
        if(s.toLowerCase().startsWith("martial"))
            return WeaponCategory.Martial;
        if(s.toLowerCase().startsWith("exotic"))
            return WeaponCategory.Exotic;
        return null;
    }
}
