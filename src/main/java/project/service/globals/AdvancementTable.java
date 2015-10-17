package project.service.globals;

import java.util.Map;
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

        private JSONObject parseTable(String table_html){
                try{
                        JSONObject jObj = new JSONObject();
                        JSONArray list = new JSONArray();
                        Document doc = Jsoup.parse(table_html);
                        Element table = doc.getElementsByTag("table").get(1);
                        Elements rows = table.getElementsByTag("tr");
                } catch (Exception e){

                }
                return null;
        }

        public static void main(String[] args) {
                String table = "\t<table>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<th colspan=\"6\"></th>\n" +
                        "\t\t\t<th colspan=\"10\">Spells per Day </th>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<th>Level </th>\n" +
                        "\t\t\t<th><span class=\"caps\">BAB</span> </th>\n" +
                        "\t\t\t<th>Fort </th>\n" +
                        "\t\t\t<th>Ref </th>\n" +
                        "\t\t\t<th>Will </th>\n" +
                        "\t\t\t<th>Special </th>\n" +
                        "\t\t\t<th>0th </th>\n" +
                        "\t\t\t<th>1st </th>\n" +
                        "\t\t\t<th>2nd </th>\n" +
                        "\t\t\t<th>3rd </th>\n" +
                        "\t\t\t<th>4th </th>\n" +
                        "\t\t\t<th>5th </th>\n" +
                        "\t\t\t<th>6th </th>\n" +
                        "\t\t\t<th>7th </th>\n" +
                        "\t\t\t<th>8th </th>\n" +
                        "\t\t\t<th>9th </th>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 1st </td>\n" +
                        "\t\t\t<td> +0 </td>\n" +
                        "\t\t\t<td> +0 </td>\n" +
                        "\t\t\t<td> +0 </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> Summon familiar, Scribe scroll </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 2nd </td>\n" +
                        "\t\t\t<td> +1 </td>\n" +
                        "\t\t\t<td> +0 </td>\n" +
                        "\t\t\t<td> +0 </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 3rd </td>\n" +
                        "\t\t\t<td> +1 </td>\n" +
                        "\t\t\t<td> +1 </td>\n" +
                        "\t\t\t<td> +1 </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 4th </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> +1 </td>\n" +
                        "\t\t\t<td> +1 </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 5th </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> +1 </td>\n" +
                        "\t\t\t<td> +1 </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> Bonus feat </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 6th </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 7th </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 8th </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> +2 </td>\n" +
                        "\t\t\t<td> +6 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 9th </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> +6 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 10th </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> +7 </td>\n" +
                        "\t\t\t<td> Bonus feat </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 11th </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> +3 </td>\n" +
                        "\t\t\t<td> +7 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 12th </td>\n" +
                        "\t\t\t<td> +6/+1 </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> +8 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 13th </td>\n" +
                        "\t\t\t<td> +6/+1 </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> +8 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 14th </td>\n" +
                        "\t\t\t<td> +7/+2 </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> +4 </td>\n" +
                        "\t\t\t<td> +9 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 15th </td>\n" +
                        "\t\t\t<td> +7/+2 </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> +9 </td>\n" +
                        "\t\t\t<td> Bonus feat </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 16th </td>\n" +
                        "\t\t\t<td> +8/+3 </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> +10 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> &#8212; </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 17th </td>\n" +
                        "\t\t\t<td> +8/+3 </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> +5 </td>\n" +
                        "\t\t\t<td> +10 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t\t<td> 1 </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 18th </td>\n" +
                        "\t\t\t<td> +9/+4 </td>\n" +
                        "\t\t\t<td> +6 </td>\n" +
                        "\t\t\t<td> +6 </td>\n" +
                        "\t\t\t<td> +11 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 2 </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 19th </td>\n" +
                        "\t\t\t<td> +9/+4 </td>\n" +
                        "\t\t\t<td> +6 </td>\n" +
                        "\t\t\t<td> +6 </td>\n" +
                        "\t\t\t<td> +11 </td>\n" +
                        "\t\t\t<td> </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t\t<td> 3 </td>\n" +
                        "\t\t</tr>\n" +
                        "\t\t<tr>\n" +
                        "\t\t\t<td> 20th </td>\n" +
                        "\t\t\t<td> +10/+5 </td>\n" +
                        "\t\t\t<td> +6 </td>\n" +
                        "\t\t\t<td> +6 </td>\n" +
                        "\t\t\t<td> +12 </td>\n" +
                        "\t\t\t<td> Bonus feat </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t\t<td> 4 </td>\n" +
                        "\t\t</tr>\n" +
                        "\t</table>\n";
                AdvancementTable at = new AdvancementTable(table);
        }
}
