package project.service.dbLookup;

import java.sql.*;

/**
 * Created by andrea on 28.10.2015.
 */
public class AccountStorage {


    private Connection c;

    public AccountStorage(String dbUrl) {
        this.c = connect(dbUrl);
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



    public ResultSet searchUser(String userID) {
        ResultSet rs = null;
        String query = "SELECT * FROM Users WHERE UserID = \""+userID+"\";";
        return searchRaw(query);
    }

    public String searchCharacter(String charName, String userID) {
        ResultSet rs = null;
        String query = "SELECT * FROM Characters WHERE characterName=\""+charName+"\" AND UserID = \""+userID+"\";";
        try {
            rs = searchRaw(query);
            if(rs != null) {
                return rs.getString("characterJSON");
            } {
                return "{empty}";
            }
        } catch (SQLException e) {
            return "Something went wrong with our sql request.";
        }
    }

    public int updateCharacterJSON(String userID, String charName, String json) {
        return updateRaw("UPDATE Characters SET characterJSON = '"+json+"' WHERE characterName=\""+charName+"\" AND UserID = \""+userID+"\";");

    }

    // General search function, that query's the database with any select statement and gives back the resultset
    public ResultSet searchRaw(String query){
        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()) return null; // This advances the cursor forward
            // So we must redo the query in order not to miss the first line
            // (There is a function that should do this, but it's not supported for sqlite :[ )
            return stmt.executeQuery(query);
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
            Statement stmt = c.createStatement();
            res = stmt.executeUpdate(query);
            stmt.close();
            c.commit();
        } catch (Exception e) {
            System.err.println("Error in searchClass: " + e.getClass().getName() + ": " + e.getMessage());
            e.printStackTrace();
        }
        return res;
    }


}
