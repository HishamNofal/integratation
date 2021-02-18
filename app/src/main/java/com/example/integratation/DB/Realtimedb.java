package com.example.integratation.DB;

import java.util.HashMap;
import java.util.Map;

public class Realtimedb {

    public String d;
    public String e;
    public boolean h;
    public Map<String, Boolean> up = new HashMap<>();

    public Realtimedb() {
    }



    public Realtimedb(String d, String e, boolean h) {
        this.d = d;
        this.e = e;
        this.h = h;
    }

    public String getD() {
        return d;
    }

    public void setD(String d) {
        this.d = d;
    }

    public String getE() {
        return e;
    }

    public void setE(String e) {
        this.e = e;
    }

    public boolean isH() {
        return h;
    }

    public void setH(boolean h) {
        this.h = h;
    }

    public Map<String, Object >  toMap(){

        Map<String, Object> result= new HashMap<>();

        result.put("d",d);
        result.put("e",e);
        result.put("h",h);

        return result;

    }

}
