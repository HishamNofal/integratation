package com.example.integratation.InsertAndUpdateActivityUserInterfaceAndLogic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.integratation.DB.IntegrationRepository;
import com.example.integratation.DB.LangTestDBElements.LangTestDataStructure;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class InsertAndUpdateActivityViewModel extends AndroidViewModel {


    private final IntegrationRepository repo;
    private MutableLiveData<LangTestDataStructure> searchForALangTestItemMutable;

    public InsertAndUpdateActivityViewModel(@NonNull Application application) {
        super(application);

        repo = new IntegrationRepository(application);


    }

    void insertLangTestItem(LangTestDataStructure langTestDataStructure){

        repo.insertLangTestItem(langTestDataStructure);

    }

    void addNewCategory(String spinnerName){

        repo.addNewCategory(spinnerName);

    }


    LiveData<List<String>> getAllCategoriesItemNames() throws ExecutionException, InterruptedException {

        return repo.getAllCategoriesItemNames();

    }

    void findALangTestItem(String foreignText) throws ExecutionException, InterruptedException {

        findALangTestItem().setValue(repo.findALangTestItem(foreignText));

    }



    synchronized MutableLiveData<LangTestDataStructure> findALangTestItem(){

        if (searchForALangTestItemMutable == null )
            searchForALangTestItemMutable = new MutableLiveData<>();

        return searchForALangTestItemMutable;

    }

    void deleteLangTestItem(LangTestDataStructure testItem){

        repo.deleteLangTestItem(testItem);

    }

    void updateLangTestItem(LangTestDataStructure langTestItem){

        repo.updateLangTestItem(langTestItem);

    }


    String[] cambridgeTranslation(String germanWord) throws ExecutionException, InterruptedException {

        return repo.cambridgeTranslation(germanWord);

    }

    }

