package com.example.biokm_000.scino_library;

/**
 * Created by biokm_000 on 17.08.2015.
 */
public class MyBook {
    private int id;
    private String name;
    private String genre;
    private String autor;
    private int group;
    private int read;
    public MyBook(){};
    public MyBook(int _id, String _name,String _genre,String _autor, int _read, int _group ){
        this.id = _id;
        this.name = _name;
        this.autor = _autor;
        this.genre = _genre;
        this.group = _group;
        this.read = _read;

    }
    public void Set(int _id, String _name,String _genre,String _autor, int _group){
        this.id = _id;
        this.name = _name;
        this.autor = _autor;
        this.genre = _genre;
        this.group = _group;
    }
    public MyBook( String _name,String _genre,String _autor, int _group){
        this.name = _name;
        this.autor = _autor;
        this.genre = _genre;
        this.group = _group;
    }
    public String GetString(){
        return this.name + " " + this.autor + " " + this.genre;
    }
    public int GetId(){
        return this.id;
    }
    public int GetRead(){
        return this.read;
    }
    public String GetName(){
        return this.name;
    }
    public String GetAutor(){
        return this.autor;
    }
    public String GetJenre(){
        return this.genre;
    }
}
