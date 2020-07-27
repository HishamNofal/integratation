package com.example.integratation.DB.TestCategoriesDBElements;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "testCategoriesDataStructure")
public class TestCategoriesDataStructure {
    @PrimaryKey(autoGenerate = true)

    public int id;

  public     int wins=0, losses =0;

   public String categoryName;

    public TestCategoriesDataStructure(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getWins() {
        return wins;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public int getLosses() {
        return losses;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public String getCategoryName() {
        return categoryName;
    }

}