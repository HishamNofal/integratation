package com.example.integratation.DB.TestCategoriesDBElements;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;


@Dao
public interface TestCategoriesDao {


    @Query("INSERT INTO TestCategoriesDataStructure(categoryName, wins, losses) " +
            "SELECT :categoryName, 0, 0 WHERE NOT EXISTS(SELECT 1 FROM TestCategoriesDataStructure WHERE categoryName = :categoryName);")
    void addNewCategory(String categoryName);


    @Query("SELECT * FROM TestCategoriesDataStructure")
    LiveData<List<TestCategoriesDataStructure>> getAllCategoryItems();


     @Query("DELETE FROM TestCategoriesDataStructure where categoryName = :categoryName")
     void deleteTestCategoryItem(String categoryName);


    @Update
    void updateTestCategoryParams(TestCategoriesDataStructure spinnerElement);


    @Query("SELECT categoryName FROM TestCategoriesDataStructure")
    LiveData<List<String>> getAllCategoriesItemNames();


    @Query("UPDATE TestCategoriesDataStructure SET wins = 0, losses = 0 where categoryName in ( select categoryName from TestCategoriesDataStructure where categoryName== :categoryName limit 1) ")
    void resetCategoryScore(String categoryName);


}