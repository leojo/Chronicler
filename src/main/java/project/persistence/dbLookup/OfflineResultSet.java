package project.persistence.dbLookup;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by leo on 23.11.2015.
 *
 * Java ResultSets are live connections to a database. This was
 * inconvenient for us, so this class mines all the info from a ResultSet
 * and stores it in memory
 *
 */
public class OfflineResultSet {
    private List<HashMap<String,Object>> resultSet;
    private HashMap<Integer,String> colNum;
    private int currentIndex;

    public OfflineResultSet(ResultSet rs){
        this.colNum = getColumnNumbers(rs);
        this.resultSet = resultSetToArrayList(rs);
        this.currentIndex = -1;
    }

    private HashMap<Integer,String> getColumnNumbers (ResultSet rs){
        try {
            ResultSetMetaData md = rs.getMetaData();
            int colCount = md.getColumnCount();
            HashMap<Integer,String> colNum = new HashMap<Integer,String>();
            for(int i=1; i<=colCount; i++){
                colNum.put(i,md.getColumnName(i));
            }
            return colNum;
        }catch (SQLException e){
            e.printStackTrace(System.err);
            return null;
        }
    }

    // Convert a resultset into an arraylist of hashmaps
    private ArrayList<HashMap<String,Object>> resultSetToArrayList(ResultSet rs){
        try {
            ResultSetMetaData md = rs.getMetaData();
            int columns = md.getColumnCount();
            ArrayList<HashMap<String,Object>> list = new ArrayList<HashMap<String,Object>>();
            do {
                HashMap<String,Object> row = new HashMap<String,Object>();
                for (int i = 1; i <= columns; ++i) {
                    row.put(md.getColumnName(i), rs.getObject(i));
                }
                list.add(row);
            } while (rs.next());
            rs.close();
            return list;
        }catch (SQLException e){
            e.printStackTrace(System.err);
            return null;
        }
    }

    // for debugging
    public void printCurrentRowKeys(){
        HashMap<String,Object> row = resultSet.get(currentIndex);
        Object[] keys = row.keySet().toArray();
        System.err.print("Keys: ");
        for(int i=0; i<keys.length; i++){
            System.err.print(keys[i].toString() + ", ");
        }
        System.err.println();

    }
    public void printCurrentRowValues(){
        HashMap<String,Object> row = resultSet.get(currentIndex);
        Object[] keys = row.keySet().toArray();
        System.err.print("Values: ");
        for(int i=0; i<keys.length; i++){
            System.err.print((row.get(keys[i])==null?"NULL":row.get(keys[i])).toString() + ", ");
        }
        System.err.println();

    }

    public int size(){
        return this.resultSet.size();
    }

    //Cursor movements

    public boolean next(){
        this.currentIndex++;
        if(this.currentIndex >= this.resultSet.size()){
            return false;
        }
        return true;
    }

    public boolean previous(){
        this.currentIndex--;
        if(this.currentIndex < 0){
            return false;
        }
        return true;
    }

    public boolean first(){
        this.currentIndex = 0;
        return true;
    }

    public boolean beforeFirst(){
        this.currentIndex = -1;
        return true;
    }

    // get's

    public Integer getInt(int index){
        String key = colNum.get(index);
        return getInt(key);
    }

    public Integer getInt(String key){
        try {
            return Integer.parseInt(getObject(key).toString().replaceAll("[^\\d.]", ""));
        } catch (NumberFormatException e){
            e.printStackTrace(System.err);
            return null;
        } catch (NullPointerException e){
            return null;
        }
    }

    public Double getDouble(int index){
        String key = colNum.get(index);
        return getDouble(key);
    }

    public Double getDouble(String key){
        try {
            return Double.parseDouble(getObject(key).toString());
        } catch (NumberFormatException e){
            e.printStackTrace(System.err);
            return null;
        }
    }

    public Boolean getBoolean(int index){
        String key = colNum.get(index);
        return getBoolean(key);
    }

    public Boolean getBoolean(String key){
        return Boolean.parseBoolean(getObject(key).toString());
    }

    public String getString(int index){
        String key = colNum.get(index);
        return getString(key);
    }

    public String getString(String key){
        Object o = getObject(key);
        return(o==null?null:o.toString());
    }

    public Object getObject(int index){
        String key = colNum.get(index);
        return getObject(key);
    }

    public Object getObject(String key){
        HashMap<String,Object> row = resultSet.get(currentIndex);
        Object cell = row.get(key);
        return cell;
    }

    public void append(OfflineResultSet another){
        if(another == null) return;
        this.resultSet.addAll(another.resultSet);
        this.colNum.putAll(another.colNum);
    }
}
