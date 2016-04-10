package project.persistence.dbLookup;

import org.json.JSONArray;

import java.sql.*;
import java.text.ParseException;
import java.util.HashMap;
import java.util.UUID;


/**
 * A class to interact with the sqlite userAccounts database.
 *
 * It has functions to execute raw update and search statements,
 * as well as more specific functions to get a list of user's characters,
 * campaigns, add a character or campaign, login, set invite, etc.
 *
 * None of these functions need much explaining, their names should be enough.
 *
 * Created by andrea on 28.10.2015.
 */
public class AccountStorage {


    private final String URL;

    public AccountStorage() {this.URL = "data/userAccounts.sqlite";}
    public AccountStorage(String dbUrl) {
        this.URL = dbUrl;
    }

    private Connection connect(String dbUrl){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + dbUrl);
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

    public HashMap<Integer, String> getDMedCampaigns(String user) {
        OfflineResultSet rs = null;
        String query = "SELECT campaignID, campaignName FROM Campaigns WHERE ownerID=\""+user+"\"";
        rs = searchRaw(query);
        return returnIntegerNames(rs, "campaignID", "campaignName");
    }

    public HashMap<Integer, String> getPlayerCampaigns(String user) {
        OfflineResultSet rs = null;
        //TODO: Insert real user id, this is just to make sure things work
        String query = "SELECT Campaigns.campaignID, Campaigns.campaignName FROM Campaigns\n" +
                               "INNER JOIN Characters\n" +
                               "ON Campaigns.campaignID=Characters.campaignID\n" +
                               "WHERE Campaigns.ownerID=\"user\"";
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

    public int addUser(String userID, String pw_encrypted) {
        return updateRaw("INSERT INTO Users (UserID,Password) VALUES ('"+userID+"','"+pw_encrypted+"');");
    }

    public int updateUserCookie(String userID) {
        UUID cookieID = UUID.randomUUID();
        String query = "UPDATE Users SET Cookie=\""+ cookieID+"\" WHERE UserID = \""+userID+"\";";
        return updateRaw(query);
    }

    public String getUserCookie(String userID) {
        String query = "SELECT Cookie FROM Users WHERE UserID = \""+userID+"\";";
        OfflineResultSet rs = searchRaw(query);
        if(rs != null) {
            rs.first();
            return rs.getString("Cookie");
        } else {
            return null;
        }
    }

    public String matchUserByCookie(String cookieID) {
        String query = "SELECT UserID FROM Users WHERE Cookie = \""+cookieID+"\";";
        OfflineResultSet rs = searchRaw(query);
        if(rs != null) {
            rs.first();
            return rs.getString("UserID");
        } else {
            return null;
        }
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
        return updateRaw("UPDATE Characters SET characterJSON = ?, characterName= '" + charName + "' WHERE characterID=\"" + charID + "\" AND UserID = \"" + userID + "\";",json);
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
        Integer uuid = UUID.randomUUID().hashCode();
        return updateRaw("INSERT INTO Campaigns (campaignName,campaignID,ownerID) VALUES ('"+campaignName+"','"+uuid+"','"+user+"');");
    }

    public int addCharacterJSON(String userID, String json, String charName) {
        return updateRaw("INSERT INTO Characters(userID, characterJSON, characterName) VALUES('"+userID+"',?, '"+charName+"');",json);

    }

    public OfflineResultSet getInviteList(String user) {
        return searchRaw("SELECT * FROM users WHERE UserID=\""+user+"\";");
    }

    public int inviteToCampaign(String campaign, String user) {
        OfflineResultSet result = getInviteList(user);
        JSONArray invites;
        if (result == null) {
            return -1;
        } else {
            try {
                result.first();
                String resultJSON = result.getString("Invites");
                if (resultJSON == null) {
                   invites = new JSONArray();
                } else {
                    invites = new JSONArray(resultJSON);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        }
        invites.put(campaign);
        int res = updateRaw("UPDATE Users SET invites='"+invites.toString()+"' WHERE UserID='"+user+"';");

        return res;
    }

    public int acceptInvite(String user, String character, int index) {
        OfflineResultSet result = getInviteList(user);
        JSONArray invites;
        if (result == null) {
            return -1;
        } else {
            try {
                result.first();
                String resultJSON = result.getString("Invites");
                if (resultJSON == null) {
                   invites = new JSONArray();
                } else {
                    invites = new JSONArray(resultJSON);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        }
        String campaignName = invites.getString(index);
        int campaignID = getCampaignID(campaignName);
        removeInvite(user, index);
        return insertIntoCampaign(character, campaignID);
    }

    public int insertIntoCampaign(String character, int campaignID) {
        return updateRaw("UPDATE Characters SET campaignID='"+campaignID+"' WHERE characterName='"+character+"';");
    }

    public int getCampaignID(String name) {
        OfflineResultSet rs = searchRaw("SELECT campaignID FROM Campaigns WHERE campaignName='"+name+"';");
        if (rs == null) return -1;
        rs.first();
        return rs.getInt("campaignID");
    }

    public int removeInvite(String user, int index) {
        OfflineResultSet result = getInviteList(user);
        JSONArray invites;
        if (result == null) {
            return -1;
        } else {
            try {
                result.first();
                String resultJSON = result.getString("Invites");
                if (resultJSON == null) {
                   invites = new JSONArray();
                } else {
                    invites = new JSONArray(resultJSON);
                }
            } catch (ParseException e) {
                e.printStackTrace();
                return -1;
            }
        }
        JSONArray newInvList = new JSONArray();
        for (int i=0; i<invites.length(); i++) {
            if (i != index) newInvList.put(invites.getString(i));
        }
        int res = updateRaw("UPDATE Users SET invites='"+newInvList.toString()+"' WHERE UserID='"+user+"';");

        return res;
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

    // General update function
    public int updateRaw(String query, String JSON){
        int res = 0;
        try{
            Connection c = connect(this.URL);
            PreparedStatement stmt = c.prepareStatement(query);
            stmt.setString(1,JSON);
            res = stmt.executeUpdate();
            stmt.close();
            c.commit();
            c.close();
        } catch (Exception e) {
            System.err.println("Error in searchClass: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) {
        AccountStorage find = new AccountStorage();
        //find.inviteToCampaign("Minnie", "andrea");
        int inv_res = find.acceptInvite("andrea", "sdfs", 0);
        System.out.println("Accept completed with result code: "+inv_res);
        find.removeInvite("andrea", 0);
        OfflineResultSet rs = find.getInviteList("andrea");
        rs.first();
        System.out.println(rs.getString("Invites"));
    }

}
