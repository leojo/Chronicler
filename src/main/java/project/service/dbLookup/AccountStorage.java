package project.service.dbLookup;

import project.service.campaign.Campaign;

import java.io.IOException;
import java.sql.*;
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

    public String getCampaignPlayers(String campaignID) {
        ResultSet rs = null;
        String query = "SELECT * FROM Characters WHERE campaignID=\""+campaignID+"\"";
        try {
            rs = searchRaw(query);
            if(rs != null) {
                return rs.getString("characterName");
            } else {
                return null;
            }
        } catch (SQLException e) {
            return null;
        }
    }

    public OfflineResultSet getCampaigns(String user) {
        ResultSet rs = null;
        String query = "SELECT * FROM Campaigns WHERE Owner=\""+user+"\"";
        rs = searchRaw(query);
        if(rs != null) {
            return new OfflineResultSet(rs);
        } else {
            return null;
        }
    }

    public int insertCampaign(String user, String campaignName) {
        return updateRaw("INSERT INTO Campaigns (CampaignID,Owner) VALUES ('"+campaignName+"','"+user+"');");
    }

    public int updateCharacterJSON(String userID, String charName, String json) {
        return updateRaw("UPDATE Characters SET characterJSON = '"+json+"' WHERE characterName=\""+charName+"\" AND UserID = \""+userID+"\";");

    }

    public int addCharacterJSON(String userID, String charName, String json) {
        return updateRaw("INSERT INTO Characters(UserID, characterName, characterJSON) VALUES('"+userID+"', '"+charName+"', '"+json+"')");

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
