package project.persistence.dbLookup;

import project.persistence.spell.SpellList;

import java.sql.*;
import java.util.ArrayList;

/*
 * Various convenience functions for accessing the content database, i.e.
 * classes, items, races, etc.
 */
public class Lookup {

    private String URL;

    public Lookup() {this.URL = "data/dnd_srd.db";}

    private Connection connect(String dbUrl){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:"+dbUrl);
            c.setAutoCommit(false);
        } catch ( Exception e ) {
            System.err.println("Error in connect: "+e.getClass().getName() + ": " + e.getMessage() );
        }
        return c;
    }

    public ArrayList<String> listClasses(){
        String query = "SELECT \"name\" FROM \"class\" WHERE \"type\" NOT LIKE \"%prestige%\" AND \"type\" NOT LIKE \"%npc%\";";
        OfflineResultSet rs = searchRaw(query);
        return returnNames(rs);
    }

    public ArrayList<String> listPrestigeClasses(){
        String query = "SELECT \"name\" FROM \"class\" WHERE \"type\" LIKE \"%prestige%\" AND \"type\" NOT LIKE \"%npc%\" AND \"type\" NOT LIKE \"%epic%\";";
        OfflineResultSet rs = searchRaw(query);
        return returnNames(rs);
    }

    public ArrayList<String> listEpicClasses(){
        String query = "SELECT \"name\" FROM \"class\" WHERE \"type\" LIKE \"%epic%\";";
        OfflineResultSet rs = searchRaw(query);
        return returnNames(rs);
    }

    public ArrayList<String> listRaces(){
        String query = "select name from \"race\"";
        OfflineResultSet rs = searchRaw(query);
        return returnNames(rs);
    }

    private ArrayList<String> returnNames(OfflineResultSet rs){
        ArrayList<String> names = new ArrayList<String>();
        while(rs.next()){
            names.add(rs.getString("name"));
        }
        return names;
    }

    public OfflineResultSet playerClass(String searchTerm){
        String[] field = {"id","name","full_text"}; // Priority order of search fields
        String query_template = "SELECT * FROM class WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public OfflineResultSet advTableByClassID(int classID, int level){
        String className = null;
        OfflineResultSet theclass = playerClass(classID+"/exact");
        theclass.first();
        className = theclass.getString("name");
        return advTable(className,level);
    }

    public OfflineResultSet advTable(String className, int level){
        String query= "select * from \"class_table\" WHERE name LIKE \""+className+"\" AND \"level\" = "+level+";";
        return searchRaw(query);
    }

    public OfflineResultSet skill(String searchTerm){
        // This is the order in which we want to search through the fields of the skills
        String[] field = {"id","name","full_text","skill_check","action","special","synergy","untrained"};
        String query_template = "SELECT * FROM skill WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public OfflineResultSet spell(String searchTerm){
        // This is the order in which we want to search through the fields of the spells
        String[] field = {"id","name","full_text","school","subschool","descriptor","effect"};
        String query_template = "SELECT * FROM spell WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public SpellList spellList(String castingName){
        String query= "SELECT * FROM spell WHERE \"level\" LIKE \"%"+castingName+"%\";";
        OfflineResultSet spells = searchRaw(query);
        return new SpellList(spells);
    }

    public OfflineResultSet feat(String searchTerm){
        // This is the order in which we want to search through the fields of the feats
        String[] field = {"name","benefit","full_text","normal","special"};
        String query_template = "SELECT * FROM feat WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public OfflineResultSet specialItem(String searchTerm){
        // This is the order in which we want to search through the fields of the items
        String[] field = {"id","name","full_text","category"};
        String query_template = "SELECT * FROM item WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public OfflineResultSet mundaneItem(String searchTerm){
        // This is the order in which we want to search through the fields of the items
        String[] field = {"id","name","full_text","family","category","cost"};
        String query_template = "SELECT * FROM equipment WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    public OfflineResultSet race(String searchTerm){
        // This is the order in which we want to search through the fields of the items
        String[] field = {"name","full_text","family","category","cost"};
        String query_template = "SELECT * FROM race WHERE \"%2$s\" LIKE '%1$s';";
        return searchByTemplate(query_template, field, searchTerm);
    }

    // Search the database using s query template where %2$s will be replaced with the field and %1$s will
    // be replaced with the search term
    // if the search term ends with a recognized /[command] it will be implemented, otherwize discarded.
    private OfflineResultSet searchByTemplate(String query_template, String[] field, String searchTerm){
        OfflineResultSet rs = null;
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
    private OfflineResultSet searchRaw(String query){
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

    public static void main(String[] args){
        Lookup find = new Lookup();
        ArrayList<String> classes = find.listClasses();
        System.out.println("Available classes:");
        for(String s : classes){
            System.out.println(s);
        }

        ArrayList<String> races = find.listRaces();
        System.out.println("\n-----------------\n\nAvailable races:");
        for(String s : races){
            System.out.println(s);
        }


        System.out.println("\n-----------------\n\nClasses found with id '1':");
        OfflineResultSet ors = find.playerClass("1/exact");
        while(ors.next()){
            System.out.println(ors.getString("name"));
        }
    }
}
