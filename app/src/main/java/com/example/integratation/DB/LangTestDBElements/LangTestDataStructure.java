package com.example.integratation.DB.LangTestDBElements;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "langTestDataStructure")
public class LangTestDataStructure {


    // id auto generated
    @PrimaryKey(autoGenerate = true)
    private int id;

    // string of the language of the answer
     private String foreignText;

    // string of the language of the test
     private String yourText;

     //the category name in the data base
     private String categoryName ="";

     //whether its isAnswered or not
    private boolean isAnswered;

private int mistakes;

    public int getMistakes() {
        return mistakes;
    }

    public void setMistakes(int mistakes) {
        this.mistakes = mistakes;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    public void setYourText(String yourText) {
        this.yourText = yourText;
    }




    public LangTestDataStructure(String foreignText, String yourText) {
        this.foreignText = foreignText;
        this.yourText = yourText;
     }

    public int getId() {
        return id;
    }

    public String getForeignText() {
        return foreignText;
    }

    public String getYourText() {
        return yourText;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setIsAnswered(boolean answered) {
        this.isAnswered = answered;
    }

    public boolean isAnswered() {
        return isAnswered;
    }
}
