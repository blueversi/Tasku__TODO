package pl.bluepito.goalertodolist.Models;

public class Category {

    private int id;
    private String name;
    private String insertDate;

    //----- Constructors -----

    public Category(){}

    public Category(String name){
        this.name=name;
    }

    public Category(int id, String name){
        this.id = id;
        this.name = name;
    }

    public Category(int id, String name, String insertDate){
        this.id = id;
        this.name = name;
        this.insertDate = insertDate;
    }

    //----- Methods -----

    // -getters-
    public int getId() {
        return this.id;
    }

    public String getName(){
        return this.name;
    }

    public String getInsertDate() {
        return this.insertDate;
    }

    // -setters-
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name){
        this.name=name;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }

    @Override
    public String toString(){
        return this.name + "      " + " Created At: " +  insertDate;
    }

}



