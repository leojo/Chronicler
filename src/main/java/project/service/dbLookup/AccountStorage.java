package project.service.dbLookup;

import project.service.campaign.Campaign;

import java.io.IOException;
import java.sql.*;

import java.util.ArrayList;

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

    public int getNextID() {
        OfflineResultSet ors = searchRaw("SELECT max(CharacterID) FROM Characters;");
        ors.first();
        return ors.getInt(1)+1;
    }

    public ArrayList<String> listCharacters(String user){
        String query = "SELECT characterName FROM \"Characters\" WHERE userID=\""+user+"\";";
        OfflineResultSet rs = searchRaw(query);
        return returnNames(rs);
    }

    private ArrayList<String> returnNames(OfflineResultSet rs){
        ArrayList<String> names = new ArrayList<String>();
        while(rs.next()){
            String name = rs.getString("characterName");
            names.add(name);
            System.out.println("ADDING NAME "+name);
        }
        return names;
    }

    public OfflineResultSet searchUser(String userID) {
        String query = "SELECT * FROM Users WHERE UserID = \""+userID+"\";";
        return searchRaw(query);
    }

    public String searchCharacter(String charName, String userID) {
        OfflineResultSet rs = null;
        String query = "SELECT * FROM Characters WHERE characterName=\""+charName+"\" AND UserID = \""+userID+"\";";
        rs = searchRaw(query);
        if(rs != null) {
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

    public OfflineResultSet getCampaigns(String user) {
        OfflineResultSet rs = null;
        String query = "SELECT * FROM Campaigns WHERE Owner=\""+user+"\"";
        rs = searchRaw(query);
        if(rs != null) {
            return rs;
        } else {
            return null;
        }
    }

    public int insertCampaign(String user, String campaignName) {
        return updateRaw("INSERT INTO Campaigns (CampaignID,Owner) VALUES ('"+campaignName+"','"+user+"');");
    }

    public int addCharacterJSON(String userID, String json) {
        return updateRaw("INSERT INTO Characters(UserID, characterJSON) VALUES('"+userID+"', '"+json+"');");

    }

    // General search function, that query's the database with any select statement and gives back the resultset
    public OfflineResultSet searchRaw(String query){
        try{
            Connection c = connect(this.URL);
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()) return null; // return null if the ResultSet is empty
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
        } catch (Exception e) {
            System.err.println("Error in searchClass: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

}
