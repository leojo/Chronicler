package service.dbLookup;

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
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println("Error in connect: "+e.getClass().getName() + ": " + e.getMessage() );
        }
        System.out.println("Connection established!");
        return c;
    }

    // TODO: These are not final!
    public ResultSet playerClass(String searchTerm){
        String[] field = {"name","short_description","class_features"}; // Priority order of search fields
        String query_template = "SELECT * FROM dnd_characterclass AS a JOIN dnd_characterclassvariant AS b ON a.id=b.character_class_id " +
                "WHERE advancement<>\"\" AND \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet skill(String searchTerm){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"name","description","check","action","special","synergy","untrained"};
        String query_template = "SELECT * FROM dnd_skill AS a JOIN dnd_skillvariant AS b ON a.id = b.skill_id WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public ResultSet spell(String searchTerm){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"name","description"};
        String query_template = "SELECT * FROM dnd_spell WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
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

    // Search the database using s query template where %2$s will be replaced with the field and %1$s will be replaced with the search term
    // if the search term ends with a recognized /[command] it will be implemented, otherwize discarded.
    private ResultSet searchByTemplate(String query_template, String[] field, String searchTerm){
        ResultSet rs = null;
        String term = "";
        if(searchTerm.toLowerCase().endsWith("/exact")) term = searchTerm.replace('*','%').split("/")[0];
        else term = "%"+searchTerm.replace('*','%').split("/")[0]+"%"; // % is the SQL wildcard
        System.out.println("Searching for term "+term);
        int i = 0;
        while(rs == null && i<field.length) {
            String query = String.format(query_template, term, field[i]);
            rs = search(query);
            i++;
        }
        return rs;
    }

    // General search function, that query's the database with any select statement and gives back the resultset
    private ResultSet search(String query){
        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            if(!rs.next()) return null; // This advances the cursor forward
            rs = stmt.executeQuery(query); // So we must redo the query in order not to miss the first line
            return rs; // (There is a function that should do this, but it's not supported for sqlite :[ )
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

    public static void main(String[] args) {
        Lookup find = new Lookup("data/dnd.sqlite");
        Scanner scan = new Scanner(System.in);
        Boolean b = true;
        while(b){
            System.out.println("\n\nSearch for class: ");
            String searchString = scan.nextLine();
            if(searchString.equalsIgnoreCase("exit")) b = false;
            else {
                ResultSet sr = find.playerClass(searchString);
                try {
                    System.out.println("\nFound class(es): ");
                    while (sr.next()) {
                        System.out.println(sr.getString("name"));
                    }
                } catch (Exception e) {
                    // Suppress exceptions
                }
            }
        }
    }
}
