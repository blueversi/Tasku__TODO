package pl.bluepito.goalertodolist.Models;

public class Task {

    private int id;
    private String taskName;
    private int status;
    private String insertDate;

    //----- Constructors -----

    public Task(){}

    public Task(String taskName, int status){
        this.taskName = taskName;
        this.status=status;
    }

    public Task(int id, String taskName, int status){
        this.id=id;
        this.taskName = taskName;
        this.status=status;
    }

    //----- Methods -----

    // -getters-
    public int getId() {
        return this.id;
    }

    public String getTaskName(){
        return this.taskName;
    }

    public int getStatus() {
        return this.status;
    }


    public String getInsertDate(){
        return this.insertDate;
    }

    // -setters-
    public void setId(int id) {
        this.id = id;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public void setInsertDate(String insertDate) {
        this.insertDate = insertDate;
    }
}

