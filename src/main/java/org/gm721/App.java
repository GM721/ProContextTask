package org.gm721;

import io.restassured.path.xml.XmlPath;

import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class App
{
    public static void main( String[] args ) throws Exception {

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            ArrayList<Valutes> valutesInDay = new ArrayList<>();
            Date date = new Date();
            for (int i = 0; i < 90; i++){
                String url = "http://www.cbr.ru/scripts/XML_daily_eng.asp?date_req=" + dateFormat.format(date);
                Valutes valutes = parseXML(url);
                valutes.setDate(date);
                valutesInDay.add(valutes);
                date = getYesterday(date);
            }

            printMax(valutesInDay);
            printMin(valutesInDay);
            printMiddle(valutesInDay);
        }catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void printMax(ArrayList<Valutes> valutesInDay) {
        Valute maxValute = null;
        Date date = new Date();
        float maxValue = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Valutes valutes:valutesInDay
             ) {
            for (Valute valute:valutes.getValuteList()
                 ) {
                if(valute.getValue()/valute.getNominal() > maxValue){
                    maxValue = valute.getValue()/valute.getNominal();
                    maxValute = valute;
                    date = valutes.getDate();
                }
            }
        }
        System.out.println("Максимальный курс");
        System.out.println(dateFormat.format(date));
        System.out.println(maxValute.getName());
        System.out.println(maxValute.getValue()/maxValute.getNominal());
        System.out.println();
    }

    public static void printMin(ArrayList<Valutes> valutesInDay) {
        Valute minValute = null;
        Date date = new Date();
        float minValue = 1000000000;
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        for (Valutes valutes:valutesInDay
        ) {
            for (Valute valute:valutes.getValuteList()
            ) {
                if(valute.getValue()/valute.getNominal() < minValue){
                    minValue = valute.getValue()/valute.getNominal();
                    minValute = valute;
                    date = valutes.getDate();
                }
            }
        }
        System.out.println("Минимальный курс");
        System.out.println(dateFormat.format(date));
        System.out.println(minValute.getName());
        System.out.println(minValute.getValue()/ minValute.getNominal());
        System.out.println();
    }

    public static void printMiddle(ArrayList<Valutes> valutesInDay) {
        System.out.println("Среднее значение по каждой валюте");
        for(int i = 0; i < valutesInDay.get(0).getValuteList().size(); i++){
            float middleValue = 0;
            for (int j = 0; j < valutesInDay.size(); j++) {
                middleValue = middleValue + valutesInDay.get(j).getValuteList().get(i).getValue()/valutesInDay.get(j).getValuteList().get(i).getNominal();
            }
            middleValue = middleValue/90;
            System.out.println(valutesInDay.get(0).getValuteList().get(i).getName() + " : " + middleValue);
        }

    }

    public static Valutes parseXML(String stringXML) throws Exception {

        XmlPath xmlPath = new XmlPath(new URI(stringXML));
        Valutes valutes = new Valutes();
        int length = (int)xmlPath.get("ValCurs.Valute.size()");
        for(int i = 0; i < length; i++ ){

            Valute valute = new Valute();
            valute.setName((String) xmlPath.get("ValCurs.Valute[" + i + "].Name"));
            valute.setNominal(Integer.parseInt((String) xmlPath.get("ValCurs.Valute[" + i + "].Nominal")));
            valute.setValue(Float.parseFloat(((String) xmlPath.get("ValCurs.Valute[" + i + "].Value")).replace(',', '.')));
            valutes.getValuteList().add(valute);
        }
        return valutes;
    }

    public static Date getYesterday(Date date){
        return new Date(date.getTime() - 24*60*60*1000);
    }
}
