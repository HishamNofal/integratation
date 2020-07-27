package com.example.integratation.DB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.integratation.DB.TestCategoriesDBElements.TestCategoriesDao;
import com.example.integratation.DB.TestCategoriesDBElements.TestCategoriesDataStructure;
import com.example.integratation.DB.LangTestDBElements.LangTestDao;
import com.example.integratation.DB.LangTestDBElements.LangTestDataStructure;

@Database(entities = {LangTestDataStructure.class, TestCategoriesDataStructure.class}, version = 6)
public abstract class IntegrationDatabase extends RoomDatabase {

    private static IntegrationDatabase instance;

    public abstract LangTestDao langTestDao();
    public abstract TestCategoriesDao categoriesDao();

    public static synchronized IntegrationDatabase getInstance(Context context){

        if (instance == null){

            instance = Room.
                    databaseBuilder(context.getApplicationContext(),
                            IntegrationDatabase.class,
                            "langTest_categories_database").
                    fallbackToDestructiveMigration().build();

        }
        return instance;
    }





}
