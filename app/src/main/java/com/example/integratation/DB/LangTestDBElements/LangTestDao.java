package com.example.integratation.DB.LangTestDBElements;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface LangTestDao {

    @Update
    void updateLangTestItem(LangTestDataStructure langTestItem);

    @Insert
    void insertLangTestItem(LangTestDataStructure langTestItem);

    @Delete
    void deleteLangTestItem(LangTestDataStructure langTestItem);

    @Query("SELECT * FROM LangTestDataStructure WHERE isAnswered == 0 and categoryName == :categoryName  ORDER BY mistakes Asc , RANDOM()")
    List<LangTestDataStructure> getAllCategoryLangTestItemsInRandOrder(String categoryName);

    @Query("DELETE FROM LangTestDataStructure where categoryName == :categoryName")
    void deleteCategorysLangTestItems(String categoryName);

    @Query("UPDATE LangTestDataStructure SET isAnswered = 0 where isAnswered == 1 and categoryName == :categoryName ")
    void resetLangTestCategorysItems(String categoryName);

    @Query("SELECT * FROM LangTestDataStructure WHERE  foreignText == :foreignText limit 1")
    LangTestDataStructure findALangTestItem(String foreignText);

}