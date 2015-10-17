package service.globals;

/**
 * Created by whiskeyjack on 10/15/15.
 */
public enum AbilityID {
	STR, DEX, CON, INT, WIS, CHA;

	public static AbilityID fromString(String str) {
		str = str.toLowerCase();
		switch (str) {
			case "strength":
			case "str": return AbilityID.STR;
			case "dexterity":
			case "dex": return AbilityID.DEX;
			case "constitution":
			case "con": return AbilityID.CON;
			case "intelligence":
			case "int": return AbilityID.INT;
			case "wisdom":
			case "wis": return AbilityID.WIS;
			case "charisma":
			case "cha": return AbilityID.CHA;
			default: return null;
		}
	}
}


