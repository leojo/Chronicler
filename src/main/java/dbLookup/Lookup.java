package dbLookup;

import java.sql.*;

public class Lookup {

    private final ResultSet content;
    private Connection c;

    public Lookup(String searchString, String dbUrl) {
        this.c = connect(dbUrl);
        ResultSet sr = skill(searchString);
        //if(sr == null) sr = "Sorry, your search didn't match any skill";
        this.content = sr;
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

    public String searchClass(String className){
        // TODO: Needs more work, this is just a prototype!
        String query = "SELECT * FROM dnd_characterclass AS a JOIN dnd_characterclassvariant AS b " +
                "ON a.id=b.character_class_id WHERE name='"+className+"';";
        System.out.println("Searching for class: "+className+"\n Using SQL query :"+query);
        ResultSet rs = search(query);
        if(rs != null){
            String rs_string = rs2string(rs);
            return rs_string;
        }
        return null;
    }

    public ResultSet skill(String searchTerm){
        // TODO: Needs more work, this is just a prototype!
        String query_template = "SELECT * FROM dnd_skill AS a JOIN dnd_skillvariant AS b ON a.id = b.skill_id WHERE  " +
                "\"name\" LIKE '%1$s' OR " +
                "\"description\" LIKE '%1$s' OR " +
                "\"check\" LIKE '%1$s' OR " +
                "\"special\" LIKE '%1$s' OR " +
                "\"synergy\" LIKE '%1$s';";
        String query = String.format(query_template,"%"+searchTerm+"%");
        System.out.println("query = " + query);
        ResultSet rs = search(query);
        return rs;
    }

    // General search function, that query's the database with any select statement and gives back the resultset
    private ResultSet search(String query){
        try{
            Statement stmt = c.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            return rs;
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
                    System.out.println("columnValue = " + columnValue);
                    if(columnValue.length() > 10) columnValue = columnValue.substring(0,7)+"...";
                    else{
                        while(columnValue.length() < 10) columnValue+=" ";
                    }
                    System.out.println("columnValue = " + columnValue.replace(' ','_'));
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


    public ResultSet getContent() {
        return content;
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
}
