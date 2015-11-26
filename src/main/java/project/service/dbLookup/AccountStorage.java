package project.service.dbLookup;

import project.service.campaign.Campaign;

import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;


/**
 * Created by andrea on 28.10.2015.
 */
public class AccountStorage {


    private final String URL;

    public AccountStorage(String dbUrl) {
        this.URL = dbUrl;
    }

    private Connection connect(String dbUrl){
        Connection c = null;
        try {
            System.out.println("Establishing connection to "+dbUrl+"...");
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);
            System.out.println("Connection established!");
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println("Error in connect: "+e.getClass().getName() + ": " + e.getMessage() );
        }
        return c;
    }

    public int getNextID(String tableName) {
        OfflineResultSet ors = searchRaw("SELECT max(characterID) FROM "+tableName+";");
        if(ors == null) return 1;
        ors.first();
        Integer currentID = ors.getInt(1);
        if (currentID == null) currentID = 0;
        return currentID+1;
    }

    public HashMap<Integer, String> listCharacters(String user){
        String query = "SELECT characterID, characterName FROM \"Characters\" WHERE userID=\""+user+"\";";
        OfflineResultSet rs = searchRaw(query);
        return returnIntegerNames(rs, "characterID", "characterName");
    }

    public HashMap<Integer, String> listCharactersCampaign(int campaign){
        String query = "SELECT characterID, characterName FROM \"Characters\" WHERE campaignID=\""+campaign+"\";";
        OfflineResultSet rs = searchRaw(query);
        return returnIntegerNames(rs, "characterID", "characterName");
    }



    private HashMap<Integer, String> returnIntegerNames(OfflineResultSet rs, String intName, String stringName){
        if(rs == null) return null;
        HashMap<Integer, String> pairs = new HashMap<Integer, String>();
        while(rs.next()){
            String name = rs.getString(stringName);
            Integer id = rs.getInt(intName);
            pairs.put(id,name);
        }
        return pairs;
    }

    public HashMap<Integer, String> getCampaigns(String user) {
        OfflineResultSet rs = null;
        String query = "SELECT campaignID, campaignName FROM Campaigns WHERE ownerID=\""+user+"\"";
        rs = searchRaw(query);
        return returnIntegerNames(rs, "campaignID", "campaignName");
    }

    public int deleteCampaign(int ID) {
        return updateRaw("DELETE FROM Campaigns WHERE campaignID="+ID+";");
    }


    public int deleteCharacter(int ID) {
        return updateRaw("DELETE FROM Characters WHERE characterID="+ID+";");
    }


    public OfflineResultSet searchUser(String userID) {
        String query = "SELECT * FROM Users WHERE UserID = \""+userID+"\";";
        return searchRaw(query);
    }

    public String searchCharacter(int charID, String userID) {
        OfflineResultSet rs = null;
        String query = "SELECT * FROM Characters WHERE characterID=\""+charID+"\" AND UserID = \""+userID+"\";";
        rs = searchRaw(query);
        if(rs != null) {
            rs.first();
            return rs.getString("characterJSON");
        } {
            return "{empty}";
        }
    }


    public int updateCharacterJSON(String userID, int charID, String json, String charName) {
        return updateRaw("UPDATE Characters SET characterJSON = '" + json + "', characterName= '" + charName + "' WHERE characterID=\"" + charID + "\" AND UserID = \"" + userID + "\";");
    }
    public OfflineResultSet getCampaignPlayers(String campaignID) {
        OfflineResultSet rs = null;
        String query = "SELECT * FROM Characters WHERE campaignID=\""+campaignID+"\"";
        rs = searchRaw(query);
        if(rs != null) {
            return rs;
        } else {
            return null;
        }
    }



    public int insertCampaign(String user, String campaignName) {
        return updateRaw("INSERT INTO Campaigns (campaignName,ownerID) VALUES ('"+campaignName+"','"+user+"');");
    }

    public int addCharacterJSON(String userID, String json, String charName) {
        return updateRaw("INSERT INTO Characters(UserID, characterJSON, characterName) VALUES('"+userID+"', '"+json+"', '"+charName+"');");

    }

    public int putInCampaign(int charID, int campID) {
        return updateRaw("UPDATE Characters SET campaignID='"+campID+"' WHERE characterID='"+charID+"';");
    }

    // General search function, that query's the database with any select statement and gives back the resultset
    public OfflineResultSet searchRaw(String query){
        try{
            Connection c = connect(this.URL);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()){
                rs.close();
                c.close();
                return null; // return null if the ResultSet is empty
            }
            OfflineResultSet ors = new OfflineResultSet(rs);
            rs.close();
            c.close();
            System.err.println("Connection closed");
            return ors;
        } catch (Exception e) {
            System.err.println("Error in searchClass: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    // General update function
    public int updateRaw(String query){
        int res = 0;
        try{
            Connection c = connect(this.URL);
            Statement stmt = c.createStatement();
            res = stmt.executeUpdate(query);
            stmt.close();
            c.commit();
            c.close();
            System.err.println("Connection closed");
        } catch (Exception e) {
            System.err.println("Error in searchClass: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

}
