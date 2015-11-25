package project.service.character;

import com.fasterxml.jackson.databind.ObjectMapper;
import project.service.dbLookup.AccountStorage;

/**
 * Created by andrea on 27.10.2015.
 */
public class CharacterBean {

    // combat vars
    private int maxHp, currHp, tempHP, ac, touchAc, flatAc, initiative, speed, grapple, spellRes, arcSpellFail, bab;
    // currency vars
    private int gp, sp, cp, pp; //gold, silver, copper, platinum
    // save vars
    private int fort, reflex, will;
    // stat vars
    private int STR, DEX, CON, INT, WIS, CHA;
    private int availableAbilityPoints;
    // fluff vars



    private String name, className, level, race, alignment, deity, size, age, gender, height, weight, eyes, hair, skin;
    // spells
    private String[] spells;
    // special Abilities
    private String specialAbs, languages;
    // skills
    private int availableSkillPoints;
    //private Skill[] skills;
    // feats
    private int availableFeats;


    ObjectMapper mapper;
    AccountStorage db;

    public int getDatabaseID() {
        return databaseID;
    }

    public void setDatabaseID(int databaseID) {
        this.databaseID = databaseID;
    }

    private int databaseID;
    public CharacterBean() { // This function should eventually take in some session ID
        mapper = new ObjectMapper();
        db = new AccountStorage("data/userAccounts.sqlite");
    }

    public void saveAsJson(String user) throws com.fasterxml.jackson.core.JsonProcessingException {

        this.databaseID = db.getNextID();
        String charAsJSON = mapper.writeValueAsString(this);

        System.out.println(charAsJSON);
        System.out.println(user);
        System.out.println("INSIDE BEAN "+this.databaseID);
        System.out.println(db.addCharacterJSON(user, charAsJSON));
    }

    public void updateJson(String user) throws com.fasterxml.jackson.core.JsonProcessingException {
        String charAsJSON = mapper.writeValueAsString(this);
        System.out.println(charAsJSON);
        System.out.println(user);
        System.out.println("WHat is the name we will insert???");
        System.out.println(this.name);
        System.out.println(db.updateCharacterJSON(user, this.databaseID, charAsJSON, this.name));
    }


    public int getMaxHp() {
        return maxHp;
    }

    public void setMaxHp(int maxHp) {
        this.maxHp = maxHp;
    }

    public int getCurrHp() {
        return currHp;
    }


    public void setCurrHp(int currHp) {
        this.currHp = currHp;
    }

    public int getAc() {
        return ac;
    }

    public void setAc(int ac) {
        this.ac = ac;
    }

    public int getTouchAc() {
        return touchAc;
    }

    public void setTouchAc(int touchAc) {
        this.touchAc = touchAc;
    }

    public int getFlatAc() {
        return flatAc;
    }

    public void setFlatAc(int flatAc) {
        this.flatAc = flatAc;
    }

    public int getInitiative() {
        return initiative;
    }

    public void setInitiative(int Initiative) {
        this.initiative = Initiative;
    }

    public int getSpeed() {
        return speed;
    }

    public void setSpeed(int speed) {
        this.speed = speed;
    }

    public int getGrapple() {
        return grapple;
    }

    public void setGrapple(int grapple) {
        this.grapple = grapple;
    }

    public int getSpellRes() {
        return spellRes;
    }

    public void setSpellRes(int spellRes) {
        this.spellRes = spellRes;
    }

    public int getArcSpellFail() {
        return arcSpellFail;
    }

    public void setArcSpellFail(int arcSpellFail) {
        this.arcSpellFail = arcSpellFail;
    }

    public int getGp() {
        return gp;
    }

    public void setGp(int gp) {
        this.gp = gp;
    }

    public int getSp() {
        return sp;
    }

    public void setSp(int sp) {
        this.sp = sp;
    }

    public int getCp() {
        return cp;
    }

    public void setCp(int cp) {
        this.cp = cp;
    }

    public int getPp() {
        return pp;
    }

    public void setPp(int pp) {
        this.pp = pp;
    }

    public int getFort() {
        return fort;
    }

    public void setFort(int fort) {
        this.fort = fort;
    }

    public int getReflex() {
        return reflex;
    }

    public void setReflex(int reflex) {
        this.reflex = reflex;
    }

    public int getWill() {
        return will;
    }

    public void setWill(int will) {
        this.will = will;
    }

    public int getSTR() {
        return STR;
    }

    public void setSTR(int STR) {
        this.STR = STR;
    }

    public int getDEX() {
        return DEX;
    }

    public void setDEX(int DEX) {
        this.DEX = DEX;
    }

    public int getCON() {
        return CON;
    }

    public void setCON(int CON) {
        this.CON = CON;
    }

    public int getINT() {
        return INT;
    }

    public void setINT(int INT) {
        this.INT = INT;
    }

    public int getWIS() {
        return WIS;
    }

    public void setWIS(int WIS) {
        this.WIS = WIS;
    }

    public int getCHA() {
        return CHA;
    }

    public void setCHA(int CHA) {
        this.CHA = CHA;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }

    public String getDeity() {
        return deity;
    }

    public void setDeity(String deity) {
        this.deity = deity;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getEyes() {
        return eyes;
    }

    public void setEyes(String eyes) {
        this.eyes = eyes;
    }

    public String getHair() {
        return hair;
    }

    public void setHair(String hair) {
        this.hair = hair;
    }

    public String getSkin() {
        return skin;
    }

    public void setSkin(String skin) {
        this.skin = skin;
    }

    public String[] getSpells() {
        return spells;
    }

    public void setSpells(String[] spells) {
        this.spells = spells;
    }

    public String getSpecialAbs() {
        return specialAbs;
    }

    public void setSpecialAbs(String specialAbs) {
        this.specialAbs = specialAbs;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) {
        this.languages = languages;
    }

    public int getBab() {
        return bab;
    }

    public void setBab(int bab) {
        this.bab = bab;
    }

/*
    public Skill[] getSkills() {
        return skills;
    }

    public void setSkills(Skill[] skills) {
        this.skills = skills;
    }

    public Feat[] getFeats() {
        return feats;
    }

    public void setFeats(Feat[] feats) {
        this.feats = feats;
    }

    public Item[] getItems() {
        return items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }
    */
    public static void main(String[] args) {
        CharacterBean cb = new CharacterBean();
        // fluff
        cb.setAge("23");
        cb.setAlignment("neutral");
        cb.setName("Amanda");
        cb.setRace("Human");
        cb.setDeity("God");
        cb.setEyes("Blue");
        cb.setGender("Female");
        cb.setHair("Blonde");
        cb.setHeight("1.70m");
        cb.setWeight("Too heavy");
        cb.setClassName("Rogue");
        cb.setSize("Big");
        cb.setSkin("Fair");

        // other
        cb.setSpells(new String[]{"Abra", "Kadabra", "Alakazam"});
        cb.setLanguages("English");
        cb.setSpecialAbs("6-pack");

        // ac
        cb.setAc(10);
        cb.setFlatAc(14);
        cb.setTouchAc(13);

        // saves
        cb.setFort(23);
        cb.setWill(22);
        cb.setReflex(21);
        cb.setSpellRes(0);


        // money
        cb.setGp(400);
        cb.setSp(32);
        cb.setCp(9);
        cb.setPp(5);

        // main
        cb.setLevel("7");
        cb.setMaxHp(32);
        cb.setCurrHp(17);
        cb.setInitiative(5);
        cb.setSpeed(30);
        cb.setGrapple(5);
        cb.setArcSpellFail(1);
        cb.setBab(6);

        // stats
        cb.setCON(16);
        cb.setCHA(14);
        cb.setDEX(15);
        cb.setSTR(17);
        cb.setINT(18);
        cb.setWIS(22);

        ObjectMapper mapper = new ObjectMapper();
        AccountStorage db = new AccountStorage("data/userAccounts.sqlite");
        try{
            String charAsJSON = mapper.writeValueAsString(cb);
            System.out.println("Did it work!?");
            //System.out.println(db.updateCharacterJSON("andrea", "Nyx", charAsJSON));
            System.out.println(mapper.writeValueAsString(cb));
        } catch(Exception e) {
            System.out.println("Man something went wrong :(");
        }
    }

    public int getTempHP() {
        return tempHP;
    }

    public void setTempHP(int tempHP) {
        this.tempHP = tempHP;
    }

    public int getAvailableAbilityPoints() {
        return availableAbilityPoints;
    }

    public void setAvailableAbilityPoints(int availableAbilityPoints) {
        this.availableAbilityPoints = availableAbilityPoints;
    }

    public int getAvailableSkillPoints() {
        return availableSkillPoints;
    }

    public void setAvailableSkillPoints(int availableSkillPoints) {
        this.availableSkillPoints = availableSkillPoints;
    }

    public int getAvailableFeats() {
        return availableFeats;
    }

    public void setAvailableFeats(int availableFeats) {
        this.availableFeats = availableFeats;
    }






    // THESE ARE FOR SAVING/LOADING PURPOSES ONLY AND WILL NOT BE SHOWN!
    //------------------------------------------------------------------
    private String level_details,ability_details,save_details,HD_details,AC_details,initiative_details,grapple_details,knownSpells_details;

    public String getLevel_details() {
        return level_details;
    }

    public void setLevel_details(String level_details) {
        this.level_details = level_details;
    }

    public String getAbility_details() {
        return ability_details;
    }

    public void setAbility_details(String ability_details) {
        this.ability_details = ability_details;
    }

    public String getSave_details() {
        return save_details;
    }

    public void setSave_details(String save_details) {
        this.save_details = save_details;
    }

    public String getHD_details() {
        return HD_details;
    }

    public void setHD_details(String HD_details) {
        this.HD_details = HD_details;
    }

    public String getAC_details() {
        return AC_details;
    }

    public void setAC_details(String AC_details) {
        this.AC_details = AC_details;
    }

    public String getInitiative_details() {
        return initiative_details;
    }

    public void setInitiative_details(String initiative_details) {
        this.initiative_details = initiative_details;
    }

    public String getGrapple_details() {
        return grapple_details;
    }

    public void setGrapple_details(String grapple_details) {
        this.grapple_details = grapple_details;
    }

    public String getKnownSpells_details() {
        return knownSpells_details;
    }

    public void setKnownSpells_details(String knownSpells_details) {
        this.knownSpells_details = knownSpells_details;
    }

    //------------------------------------------------------------------
}
