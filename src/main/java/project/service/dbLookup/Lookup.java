package project.service.dbLookup;

import org.json.JSONObject;
import project.service.globals.AdvancementTable;

import java.sql.*;
import java.util.Scanner;

public class Lookup {

    private Connection c;

    public Lookup(String dbUrl) {
        this.c = connect(dbUrl);
    }

    private Connection connect(String dbUrl){
        Connection c = null;
        try {
            System.out.println("Establishing connection to "+dbUrl+"...");
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+dbUrl);
            System.out.println("Connection established!");
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println("Error in connect: "+e.getClass().getName() + ": " + e.getMessage() );
        }
        return c;
    }

    // TODO: These are not final!
    public ResultSet playerClass(String searchTerm){
        String[] field = {"class_id","name","description","class_features"}; // Priority order of search fields
        String query_template = "SELECT * FROM dnd_characterclass WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public AdvancementTable advTableByClassID(int classID){
        ResultSet rs = playerClass(classID+"/exact");
        String table_html = "";
        try {
            table_html = rs.getString("advancement_html");
        } catch (SQLException e){
            e.printStackTrace();
            return null;
        }
        return new AdvancementTable(table_html);
    }

    public ResultSet skill(String searchTerm){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"name","description","check","action","special","synergy","untrained"};
        String query_template = "SELECT * FROM dnd_skill AS a LEFT JOIN dnd_skillvariant AS b ON a.id =" +
                " b.skill_id WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet spell(String searchTerm){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"name","description"};
        String query_template = "SELECT * FROM spells WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet spellList(int classId, int level){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"name","description"};
        String query= "SELECT * FROM dnd_spellclasslevel WHERE" +
                " character_class_id = "+classId+" AND \"level\" = "+level+";";
        return searchRaw(query);
    }

    public ResultSet feat(String searchTerm){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"name","benefit","description","normal"};
        String query_template = "SELECT * FROM dnd_feat WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet item(String searchTerm){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"name","description"};
        String query_template = "SELECT * FROM dnd_item WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    // Search the database using s query template where %2$s will be replaced with the field and %1$s will
    // be replaced with the search term
    // if the search term ends with a recognized /[command] it will be implemented, otherwize discarded.
    public ResultSet searchByTemplate(String query_template, String[] field, String searchTerm){
        ResultSet rs = null;
        String term = "";
        if(searchTerm.toLowerCase().endsWith("/exact")) term = searchTerm.replace('*','%').split("/")[0];
        else term = "%"+searchTerm.replace('*','%').split("/")[0]+"%"; // % is the SQL wildcard
        int i = 0;
        while(rs == null && i<field.length) {
            String query = String.format(query_template, term, field[i]);
            rs = searchRaw(query);
            i++;
        }
        return rs;
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

    public int updateCharacterJSON(String userID, String charName, String json) {
        return updateRaw("UPDATE Characters SET characterJSON = '"+json+"' WHERE characterName=\""+charName+"\" AND UserID = \""+userID+"\";");

    }

    // Takes a resultset from a sqlite query and converts it into a human readable abbreviated string.
    // Intended for development purposes ONLY!
    private String rs2string(ResultSet rs){
        String retString = "";
        try {
            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();
            while (rs.next()) {
                System.out.println("while loop iteration");
                for (int i = 1; i <= columnsNumber; i++) {
                    String columnValue = rs.getString(i);
                    if(columnValue == null) columnValue = "          ";
                    if(columnValue.length() > 10) columnValue = columnValue.substring(0,7)+"...";
                    else{
                        while(columnValue.length() < 10) columnValue+=" ";
                    }
                    if (i > 1) columnValue = ",  "+columnValue;
                    retString+=columnValue;
                }
                retString+="\n<br>===============================================================<br>\n";
            }
        } catch (Exception e) {
            System.err.println("Error in printResultSet: "+e.getClass().getName() + ": " + e.getMessage());
        }
        return retString;
    }

//Working on creating our own "result-set" type object
// 
//    public class SearchResult{
//        private final Object[] items;
//
//        public SearchResult(){
//            items = null;
//        }
//
//        public class Skill{
//            public final int ID;
//            public final String name;
//        }
//
//        public class Spell{
//            public final int ID;
//            public final String name;
//        }
//
//        public class Class{
//            public final int ID;
//            public final String name;
//        }
//    }

    public static void main(String[] args){
        Lookup find = new Lookup("data/dnd.sqlite");
        Scanner scan = new Scanner(System.in);
        Boolean b = false;
        Lookup find2 = new Lookup("data/userAccounts.sqlite");
        System.out.println(find2.searchCharacter("Nyx", "andrea"));
        System.out.println(find2.searchCharacter("Nylon", "bjorn"));
        System.out.println(find2.searchCharacter("Nyk", "bjorn"));
        while(b){
            System.out.println("\n\nSearch for class: ");
            String searchString = scan.nextLine();
            if(searchString.equalsIgnoreCase("exit")) b = false;
            else {
                ResultSet rs = find.playerClass(searchString);
                if(rs==null) System.out.println("the result is null :S");
                else {
                    try {
                        System.out.println("\nTop hit: ");
                        if (!rs.next()) System.exit(0);
                        int id = Integer.parseInt(rs.getString("class_id"));
                        System.out.println(rs.getString("name") + " (id: " + id + ")");
                        System.out.println(find.advTableByClassID(id).toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
