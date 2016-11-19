package ru.mikhaylov;

import com.sun.org.apache.xpath.internal.SourceTree;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.awt.*;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

/**
 * Created by mikhaylov_av on 10.11.2016.
 */
public class ParserURL {
    public static String ParsingURLcurrencyCBR() throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("Курсы ЦБ РФ.");
        sb.append("\n\n");

        Date date = new Date();
        Calendar instance = Calendar.getInstance();
        instance.setTime(date);
        instance.add(Calendar.DAY_OF_MONTH, -1);
        Date datebefor = instance.getTime();

        String curbeforedate = new SimpleDateFormat("dd.MM.yyyy").format(datebefor) ;
        String curdate = new SimpleDateFormat("dd.MM.yyyy").format(date);

        Document doccurr = Jsoup.connect("http://www.cbr.ru/scripts/XML_daily.asp?date_req="+curdate).get();
        Document doccold = Jsoup.connect("http://www.cbr.ru/scripts/XML_daily.asp?date_req="+curbeforedate).get();

        String titlecurr = doccurr.select("ValCurs").attr("Date");
        sb.append("Действуют с "+ titlecurr);
        sb.append("\n\n");

//        if (curdate != titlecurr) {
//            curdate = curbeforedate;
//            instance.add(Calendar.DAY_OF_MONTH, -1);
//            datebefor = instance.getTime();
//            curbeforedate = new SimpleDateFormat("dd.MM.yyyy").format(datebefor) ;
//            doccurr = Jsoup.connect("http://www.cbr.ru/scripts/XML_daily.asp?date_req="+curdate).get();
//            doccold = Jsoup.connect("http://www.cbr.ru/scripts/XML_daily.asp?date_req="+curbeforedate).get();
//        }

        Elements code = doccurr.select("Valute");
        Elements codeold = doccold.select("Valute");
        for (Element desc: code) {
            for (Element descold: codeold) {
                if ((desc.select("NumCode").text().contains("840") && descold.select("NumCode").text().contains("840")) ||
                        (desc.select("NumCode").text().contains("978") && descold.select("NumCode").text().contains("978")) ||
                        (desc.select("NumCode").text().contains("756") && descold.select("NumCode").text().contains("756")) ||
                        (desc.select("NumCode").text().contains("826") && descold.select("NumCode").text().contains("826")) ) {

                    double indicator = Double.parseDouble(desc.select("Value").text().replace(",","."))-Double.parseDouble(descold.select("Value").text().replace(",","."));
                    if (indicator > 0) {
                        if (desc.select("CharCode").text().contains("USD")) {
                            sb.append(String.format("%-4s%-3s%-9.4f%-3s%.2f%n", desc.select("CharCode").text(),"$",Float.valueOf(desc.select("Value").text().replace(",",".")),"\uD83D\uDCC8",Math.rint(indicator * 100) / 100));
                        }else if (desc.select("CharCode").text().contains("EUR")){
                            sb.append(String.format("%-4s%-3s%-9.4f%-3s%.2f%n", desc.select("CharCode").text(),"€",Float.valueOf(desc.select("Value").text().replace(",",".")),"\uD83D\uDCC8",Math.rint(indicator * 100) / 100));
                        }else if (desc.select("CharCode").text().contains("CHF")){
                            sb.append(String.format("%-4s%-3s%-9.4f%-3s%.2f%n", desc.select("CharCode").text(),"₣",Float.valueOf(desc.select("Value").text().replace(",",".")),"\uD83D\uDCC8",Math.rint(indicator * 100) / 100));
                        }else if (desc.select("CharCode").text().contains("GBP")){
                            sb.append(String.format("%-4s%-3s%-9.4f%-3s%.2f%n", desc.select("CharCode").text(),"£",Float.valueOf(desc.select("Value").text().replace(",",".")),"\uD83D\uDCC8",Math.rint(indicator * 100) / 100));
                        }
                    }else {
                        if (desc.select("CharCode").text().contains("USD")) {
                            sb.append(String.format("%-4s%-3s%-9.4f%-3s%.2f%n", desc.select("CharCode").text(),"$",Float.valueOf(desc.select("Value").text().replace(",",".")),"\uD83D\uDCC8",Math.rint(indicator * -100) / 100));
                        }else if (desc.select("CharCode").text().contains("EUR")){
                            sb.append(String.format("%-4s%-3s%-9.4f%-3s%.2f%n", desc.select("CharCode").text(),"€",Float.valueOf(desc.select("Value").text().replace(",",".")),"\uD83D\uDCC8",Math.rint(indicator * -100) / 100));
                        }else if (desc.select("CharCode").text().contains("CHF")){
                            sb.append(String.format("%-4s%-3s%-9.4f%-3s%.2f%n", desc.select("CharCode").text(),"₣",Float.valueOf(desc.select("Value").text().replace(",",".")),"\uD83D\uDCC8",Math.rint(indicator * -100) / 100));
                        }else if (desc.select("CharCode").text().contains("GBP")){
                            sb.append(String.format("%-4s%-3s%-9.4f%-3s%.2f%n", desc.select("CharCode").text(),"£",Float.valueOf(desc.select("Value").text().replace(",",".")),"\uD83D\uDCC8",Math.rint(indicator * -100) / 100));
                        }
                    }
                }
            }
        }

        return String.valueOf(sb);
    }

    public static String ParsingURLcurrencyLKB() throws IOException {
        String text = "\n";
        Document doc = Jsoup.connect("http://kombank.ru/").get();
        Elements title = doc.select("div .curs2 .course_block p");
        Elements paragraf = doc.select("div .cursosnv tr").first().children();
        Element table = doc.select("div .cursosnv table[class=course_tbl]").first();
        Iterator<Element> ite = table.select("tbody tr").iterator();
        ite.next();
        text = text + title.text()+"\n       "+"\n"+paragraf.text();
        for (int i = 0; i < 6; i++) {
            text = text + ite.next().text();
        }
        text = text.replace("£","\n   £");
        text = text.replace("$","\n   $");
        text = text.replace("€","\n   €");

        return text;
    }
}
