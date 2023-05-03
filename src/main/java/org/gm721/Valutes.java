package org.gm721;

import java.util.ArrayList;
import java.util.Date;

public class Valutes {

    private ArrayList<Valute> valuteList;

    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Valutes() {
        this.valuteList = new ArrayList<>();
    }

    public ArrayList<Valute> getValuteList() {
        return valuteList;
    }
}
