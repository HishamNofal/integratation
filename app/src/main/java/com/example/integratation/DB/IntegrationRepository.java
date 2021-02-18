package com.example.integratation.DB;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.example.integratation.DB.TestCategoriesDBElements.TestCategoriesDao;
import com.example.integratation.DB.TestCategoriesDBElements.TestCategoriesDataStructure;
import com.example.integratation.DB.LangTestDBElements.LangTestDao;
import com.example.integratation.DB.LangTestDBElements.LangTestDataStructure;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

public class IntegrationRepository {

    private final TestCategoriesDao testCategoriesDao;
   // private final DatabaseReference myRef;
    private LangTestDao langTestDao;


     public IntegrationRepository(Application app){

        IntegrationDatabase database =
                IntegrationDatabase.
                        getInstance(app);

        langTestDao = database.langTestDao();
        testCategoriesDao = database.categoriesDao();




    }

    public String[] cambridgeTranslation(String germanWord) throws ExecutionException, InterruptedException {


         return new CambridgeTranslationAsyncTask().execute(germanWord).get();

    }

   public  LangTestDataStructure findALangTestItem(String foreignText) throws ExecutionException, InterruptedException {

         return new FindALangTestItemAsyncTask(langTestDao).execute(foreignText).get();
    }


   public LiveData<List<String>> getAllCategoriesItemNames() throws ExecutionException, InterruptedException {
          return new GetAllCategoriesItemNamesAsyncTask(testCategoriesDao).execute().get();
    }

   public void updateTestCategoryParams(TestCategoriesDataStructure categoryItem){
         new UpdateTestCategoryParamsAsyncTask(testCategoriesDao).execute(categoryItem);
    }


   public void resetCategory(String categoryName){
         new ResetCategoryAsyncTask(langTestDao, testCategoriesDao).execute(categoryName);
    }



  public  void deleteCategory(String categoryName){

         new DeleteCategoryAsyncTask(langTestDao, testCategoriesDao).execute(categoryName);

    }

   public void updateLangTestItem(LangTestDataStructure langTestItem){

         new UpdateLangTestItemAsyncTask(langTestDao).execute(langTestItem);

    }

    public void deleteLangTestItem(LangTestDataStructure langTestItem){

         new DeleteLangTestItemAsyncTask(langTestDao).execute(langTestItem);

    }

    public void addNewCategory(String categoryName){

        new AddNewCategoryAsyncTask(testCategoriesDao).execute(categoryName);
    }





   public  List<LangTestDataStructure> getAllCategoryLangTestItemsInRandOrder(String categoryName)
            throws ExecutionException, InterruptedException {

         return new GetAllCategoryLangTestItemsInRandOrderAsyncTask(langTestDao).execute(categoryName).get();

    }

  public  LiveData<List<TestCategoriesDataStructure>> getAllCategoryItems() throws ExecutionException, InterruptedException {

      return new GetAllCategoryItemsAsyncTask(testCategoriesDao).execute().get();

    }


    public void insertLangTestItem(LangTestDataStructure langTestItem) {
        new InsertLangTestItemAsyncTask(langTestDao).execute(langTestItem);
    }

    private static class InsertLangTestItemAsyncTask extends AsyncTask<LangTestDataStructure, Void, Void>{
        private LangTestDao langTestDao;

        private InsertLangTestItemAsyncTask(LangTestDao langTestDao){
            this.langTestDao = langTestDao;

        }

        @Override
        protected Void doInBackground(LangTestDataStructure... dictionaries) {

            langTestDao.insertLangTestItem(dictionaries[0]);
            return null;
        }
    }

    private static class GetAllCategoryLangTestItemsInRandOrderAsyncTask extends AsyncTask<String, Void, List<LangTestDataStructure>>{
        private LangTestDao langTestDao;

        private GetAllCategoryLangTestItemsInRandOrderAsyncTask(LangTestDao langTestDao){
            this.langTestDao = langTestDao;

        }

        @Override
        protected List<LangTestDataStructure> doInBackground(String... strings) {


            return langTestDao.getAllCategoryLangTestItemsInRandOrder(strings[0]);
        }
    }





    private static class GetAllCategoryItemsAsyncTask extends AsyncTask<Void, Void, LiveData<List<TestCategoriesDataStructure>>>{
        private TestCategoriesDao testCategoriesDao;

        private GetAllCategoryItemsAsyncTask(TestCategoriesDao testCategoriesDao){
            this.testCategoriesDao = testCategoriesDao;
        }

        @Override
        protected LiveData<List<TestCategoriesDataStructure>> doInBackground(Void... voids) {

            return testCategoriesDao.getAllCategoryItems();
        }
    }



    private static class AddNewCategoryAsyncTask extends AsyncTask<String, Void, Void>{
        private TestCategoriesDao testCategoriesDao;

        private AddNewCategoryAsyncTask(TestCategoriesDao testCategoriesDao){
            this.testCategoriesDao = testCategoriesDao;
        }

        @Override
        protected Void doInBackground(String... strings) {

            testCategoriesDao.addNewCategory(strings[0]);

            return null;
        }
    }

    private static class DeleteLangTestItemAsyncTask extends AsyncTask<LangTestDataStructure, Void, Void>{

         LangTestDao langTestDao;

        public DeleteLangTestItemAsyncTask(LangTestDao langTestDao) {
            this.langTestDao = langTestDao;
        }

        @Override
        protected Void doInBackground(LangTestDataStructure... langTestDataStructures) {

            langTestDao.deleteLangTestItem(langTestDataStructures[0]);
            return null;
        }
    }

    private static class UpdateLangTestItemAsyncTask extends AsyncTask<LangTestDataStructure, Void, Void>{

        LangTestDao langTestDao;

        public UpdateLangTestItemAsyncTask(LangTestDao langTestDao) {
            this.langTestDao = langTestDao;
        }

        @Override
        protected Void doInBackground(LangTestDataStructure... langTestDataStructures) {

            langTestDao.updateLangTestItem(langTestDataStructures[0]);
            return null;
        }
    }

    private static class DeleteCategoryAsyncTask extends AsyncTask<String, Void, Void>{

        LangTestDao langTestDao;
        TestCategoriesDao testCategoriesDao;

        public DeleteCategoryAsyncTask(LangTestDao langTestDao, TestCategoriesDao testCategoriesDao) {
            this.langTestDao = langTestDao;
            this.testCategoriesDao = testCategoriesDao;

        }

        @Override
        protected Void doInBackground(String... strings) {

            langTestDao.deleteCategorysLangTestItems(strings[0]);
            testCategoriesDao.deleteTestCategoryItem(strings[0]);

            return null;
        }
    }

    private static class ResetCategoryAsyncTask extends AsyncTask<String, Void, Void>{

        LangTestDao langTestDao;
        TestCategoriesDao testCategoriesDao;

        public ResetCategoryAsyncTask(LangTestDao langTestDao, TestCategoriesDao testCategoriesDao) {

            this.langTestDao = langTestDao;
            this.testCategoriesDao = testCategoriesDao;

        }

        @Override
        protected Void doInBackground(String... strings) {

            langTestDao.resetLangTestCategorysItems(strings[0]);
            testCategoriesDao.resetCategoryScore(strings[0]);
            return null;
        }
    }


    private static class UpdateTestCategoryParamsAsyncTask extends AsyncTask<TestCategoriesDataStructure, Void, Void> {

        TestCategoriesDao testCategoriesDao;

        public UpdateTestCategoryParamsAsyncTask(TestCategoriesDao testCategoriesDao) {
            this.testCategoriesDao = testCategoriesDao;
        }

        @Override
        protected Void doInBackground(TestCategoriesDataStructure... testCategoriesDataStructure) {

            testCategoriesDao.updateTestCategoryParams(testCategoriesDataStructure[0]);

            return null;
        }
    }


    private static class GetAllCategoriesItemNamesAsyncTask extends AsyncTask<Void, Void, LiveData<List<String>>> {

        TestCategoriesDao testCategoriesDao;

        public GetAllCategoriesItemNamesAsyncTask(TestCategoriesDao testCategoriesDao) {
            this.testCategoriesDao = testCategoriesDao;
        }

        @Override
        protected LiveData<List<String>> doInBackground(Void... voids) {

           return testCategoriesDao.getAllCategoriesItemNames();

         }
    }


    static class FindALangTestItemAsyncTask extends AsyncTask<String, Void, LangTestDataStructure>{

        private LangTestDao langTestDao;


        public FindALangTestItemAsyncTask(LangTestDao langTestDao) {
            this.langTestDao = langTestDao;

        }


        @Override
        protected LangTestDataStructure doInBackground(String... strings) {
            return langTestDao.findALangTestItem(strings[0]);

        }
    }

    static class CambridgeTranslationAsyncTask extends AsyncTask<String, Void, String[]> {

        CambridgeTranslationAsyncTask(){}


        @Override
          protected String[] doInBackground(String... string) {

            return new CambridgeTranslate(string[0]).theWords();
        }


    }


}
