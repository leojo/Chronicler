package project.service.dbLookup;

import java.sql.*;
import java.util.Scanner;

public class Lookup {

    private Connection c;

    public Lookup() {this.c = connect("data/dnd_srd.db");}

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
        String[] field = {"id","name","full_text"}; // Priority order of search fields
        String query_template = "SELECT * FROM class WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet advTableByClassID(int classID, int level){
        String className = null;
        try {
            className = playerClass(classID+"").getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return advTable(className,level);
    }

    public ResultSet advTable(String className, int level){
        String query= "select * from \"class_table\" WHERE name LIKE \""+className+"\" AND \"level\" = "+level+";";
        return searchRaw(query);
    }

    public ResultSet skill(String searchTerm){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"name","full_text","skill_check","action","special","synergy","untrained"};
        String query_template = "SELECT * FROM skill WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet spell(String searchTerm){
        // This is the order in which we want to search through the fields of the spells
        String[] field = {"name","full_text","school","subschool","descriptor","effect"};
        String query_template = "SELECT * FROM spell WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet spellList(String castingName, int spellLevel){
        String query= "SELECT * FROM spell WHERE \"level\" LIKE \"%"+castingName+"%"+spellLevel+"%\";";
        return searchRaw(query);
    }

    public ResultSet feat(String searchTerm){
        // This is the order in which we want to search through the fields of the feats
        String[] field = {"name","benefit","full_text","normal","special"};
        String query_template = "SELECT * FROM feat WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet specialItem(String searchTerm){
        // This is the order in which we want to search through the fields of the items
        String[] field = {"name","full_text","category"};
        String query_template = "SELECT * FROM item WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet mundaneItem(String searchTerm){
        // This is the order in which we want to search through the fields of the items
        String[] field = {"name","full_text","family","category","cost"};
        String query_template = "SELECT * FROM item WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet race(String searchTerm){
        // This is the order in which we want to search through the fields of the items
        String[] field = {"name","full_text","family","category","cost"};
        String query_template = "SELECT * FROM item WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    // Search the database using s query template where %2$s will be replaced with the field and %1$s will
    // be replaced with the search term
    // if the search term ends with a recognized /[command] it will be implemented, otherwize discarded.
    private ResultSet searchByTemplate(String query_template, String[] field, String searchTerm){
        ResultSet rs = null;
        String term = "";
        if(searchTerm.toLowerCase().endsWith("/exact")) term = searchTerm.replace('*','%').substring(0,searchTerm.lastIndexOf("/"));
        else term = "%"+searchTerm.replace('*','%')+"%"; // % is the SQL wildcard
        int i = 0;
        while(rs == null && i<field.length) {
            String query = String.format(query_template, term, field[i]);
            rs = searchRaw(query);
            i++;
        }
        return rs;
    }

    // General search function, that query's the database with any select statement and gives back the resultset
    private ResultSet searchRaw(String query){
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

    public static void main(String[] args){
        Lookup find = new Lookup();
        Scanner scan = new Scanner(System.in);
    }
}
