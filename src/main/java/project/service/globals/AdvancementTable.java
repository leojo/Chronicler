package project.service.globals;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonCreator;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.json.*;
import org.jsoup.select.Elements;

/**
 * Created by leo on 17.10.2015.
 */
public class AdvancementTable {
    private final JSONObject table;

    public AdvancementTable(String advancement_html){
        table = parseTable(advancement_html);
    }

    public JSONObject getJSON(){
        return table;
    }

    private JSONObject parseTable(String table_html){
        try{
            JSONObject table = new JSONObject();
            Document doc = Jsoup.parse(table_html);
            Element wholeTable = doc.getElementsByTag("table").get(0);
            Element tableBody = wholeTable.children().get(0);
            Elements rows = tableBody.children();
            // remove all rows above the header row:
            while(rows.size()>0 && !rows.get(0).text().toLowerCase().startsWith("level")) {
                rows.remove(0);
            }
            // create a header list
            List<String> headers = new ArrayList<String>();
            for(int i=0; i<rows.size(); i++){
                Element row = rows.get(i);
                Elements cells = row.children();
                JSONObject tableRow = new JSONObject();
                for(int j=1; j<cells.size(); j++){
                    Element cell = cells.get(j);
                    if(i==0){
                        // This is the header row
                        headers.add(cell.text().replaceAll("\\p{Pd}", "-")); // the replaceAll replaces all dashes with a regular one
                    } else {
                        tableRow.put(headers.get(j-1),cell.text().replaceAll("\\p{Pd}", "-"));
                    }
                }
                if(i>0) table.put(cells.get(0).text().replaceAll("[^0-9]", ""), tableRow);
            }
            return table;
        } catch (Exception e){
            System.err.println("Error in parseTable!");
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString(){
        return table.toString(2);
    }

    public static void main(String[] args) {
    }
}
