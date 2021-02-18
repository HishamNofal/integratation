package com.example.integratation.MainActivityUserInterfaceAndLogic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.integratation.DB.TestCategoriesDBElements.TestCategoriesDataStructure;
import com.example.integratation.DB.IntegrationRepository;
import com.example.integratation.DB.LangTestDBElements.LangTestDataStructure;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainActivityViewModel extends AndroidViewModel {

    private final IntegrationRepository repo;
    private MutableLiveData<List<LangTestDataStructure>> categoryListMutableLiveData;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);

        repo = new IntegrationRepository(application);



    }



    void getAllCategoryLangTestItemsInRandOrder(String categoryName) throws ExecutionException, InterruptedException {


        getAllCategoryLangTestItemsInRandOrder().setValue(repo.getAllCategoryLangTestItemsInRandOrder(categoryName));


    }

    MutableLiveData<List<LangTestDataStructure>> getAllCategoryLangTestItemsInRandOrder(){

        if (categoryListMutableLiveData == null ){

            categoryListMutableLiveData = new MutableLiveData<>();
        }

        return categoryListMutableLiveData;

    }

    void updateTestCategoryParams(TestCategoriesDataStructure categoryItem){

        repo.updateTestCategoryParams(categoryItem);

    }

    LiveData<List<TestCategoriesDataStructure>> getAllCategoryItems() throws ExecutionException, InterruptedException {

        return repo.getAllCategoryItems();

    }




    void deleteLangTestItem(LangTestDataStructure langTestItem){

        repo.deleteLangTestItem(langTestItem);

    }

    void updateLangTestItem(LangTestDataStructure langTestItem){

        repo.updateLangTestItem(langTestItem);

    }

    void deleteCategory(String categoryName){

        repo.deleteCategory(categoryName);

    }
    void resetCategory(String categoryName) {
    repo.resetCategory(categoryName);

    }


   /* void firebase(){

        repo.Firebase();
    }


    */


    void insertLangTestItem(LangTestDataStructure langTestDataStructure){

        repo.insertLangTestItem(langTestDataStructure);

    }

    void addNewCategory(String spinnerName){

        repo.addNewCategory(spinnerName);

    }

    }
