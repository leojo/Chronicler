package project.service.character;

import java.sql.ResultSet;

/**
 * Created by andrea on 27.10.2015.
 */
public class sheetBean {

    // combat vars
    private int maxHp, currHp, ac, touchAc, flatAc, initative, speed, grapple, spellRes, arcSpellFail;
    // currency vars
    private int gp, sp, cp, pp; //gold, silver, copper, platinum
    // save vars
    private int fort, reflex, will;
    // stat vars
    private int str, dex, con, intel, wis, cha;
    // fluff vars
    private String name, className, level, race, alignment, deity, size, age, gender, height, weight, eyes, hair, skin;
    // spells
    private ResultSet[] spells;
    // special Abilities
    private String specialAbs, languages;
    // skills
    //private Skill[] skills;
    // feats
    //private Feat[] feats;
    // items/inventory
    //private Item[] items;


    public sheetBean() { // This function should eventually take in some session ID


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

    public int getInitative() {
        return initative;
    }

    public void setInitative(int initative) {
        this.initative = initative;
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

    public int getStr() {
        return str;
    }

    public void setStr(int str) {
        this.str = str;
    }

    public int getDex() {
        return dex;
    }

    public void setDex(int dex) {
        this.dex = dex;
    }

    public int getCon() {
        return con;
    }

    public void setCon(int con) {
        this.con = con;
    }

    public int getIntel() {
        return intel;
    }

    public void setIntel(int intel) {
        this.intel = intel;
    }

    public int getWis() {
        return wis;
    }

    public void setWis(int wis) {
        this.wis = wis;
    }

    public int getCha() {
        return cha;
    }

    public void setCha(int cha) {
        this.cha = cha;
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

    public ResultSet[] getSpells() {
        return spells;
    }

    public void setSpells(ResultSet[] spells) {
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
}
