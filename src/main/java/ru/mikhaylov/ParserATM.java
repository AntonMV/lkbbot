package ru.mikhaylov;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import sun.reflect.generics.tree.Tree;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by Anton on 13.11.2016.
 */
public class ParserATM {

    private static float longitude2;
    private static float latitude2;
    private static String name;
    private static String description;
    private static TreeMap<Integer, String[]> set;

    public static TreeMap<Integer, String[]> getSet() {
        return set;
    }


    public static void ParserATM(float lat, float lon) {

      File input = new File("coordinates ATMs.xml"); //System.getProperty("user.dir")+"\\src\\main\\resources\\coordinates ATMs.xml"
        try {
            int result;
            Document doc = Jsoup.parse(input, "UTF-8", "http://example.com/");
            Elements links = doc.getElementsByTag("atm");
            String[] array;
            set = new TreeMap<>();
            for (Element link: links) {

                latitude2 = Float.parseFloat(link.select("latitude").text());
                longitude2 = Float.parseFloat(link.select("longitude").text());
                name = link.select("name").text();
                description = link.select("description").text();
                result = Integer.valueOf((int) distFrom(lat,lon,latitude2,longitude2));
                array = new String[]{name, String.valueOf(latitude2), String.valueOf(longitude2), description};
                set.put(result, array);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private static float distFrom(float lat1, float lng1, float lat2, float lng2) {
        double earthRadius = 6371000; //meters
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        float dist = (float) (earthRadius * c);

        return dist;
    }


}
