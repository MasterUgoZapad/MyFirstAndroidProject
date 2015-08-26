package com.example.biokm_000.scino_library;

/**
 * Created by biokm_000 on 17.08.2015.
 */
public class MyGroup {
    private int id;
    private String name;

    public MyGroup(){};
    public MyGroup(int _id, String _name) {
        this.id = _id;
        this.name = _name;
    }
    public void Set(int _id, String _name){
        this.id = _id;
        this.name = _name;
    }
    public MyGroup( String _name){
        this.name = _name;
    }
    public String GetString(){
        return this.name;
    }
    public Integer GetID(){ return this.id; }
}
