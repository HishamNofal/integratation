package com.example.integratation.MainActivityUserInterfaceAndLogic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.example.integratation.Constants;
import com.example.integratation.DB.Realtimedb;
import com.example.integratation.DB.TestCategoriesDBElements.TestCategoriesDataStructure;
import com.example.integratation.DB.LangTestDBElements.LangTestDataStructure;
import com.example.integratation.InsertAndUpdateActivityUserInterfaceAndLogic.InsertAndUpdateActivity;
import com.example.integratation.InsertAndUpdateActivityUserInterfaceAndLogic.InsertAndUpdateActivityViewModel;
import com.example.integratation.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutionException;

import static com.example.integratation.Constants.CATEGORY_NAME;

public class MainActivity extends AppCompatActivity {

    private RecyclerView langTestRecyclerView;
    private MainActivityViewModel mainActivityViewModel;
    private Spinner categorySpinner;
    private SpinnerAdapter categorySpinnerAdapter;
    private MainListAdapter langTestRecyclerViewAdapter;
    private ArrayList<LangTestDataStructure> AllCategoryLangTestItemsRandList;
    private int prevClickTag;
    private long prevClickTime;
    private EditText answeringEditText, lockEditText;
    private ImageButton sayItButton;
    private TextToSpeech textToSpeech;
    private TextView winsTextView, losesTextView, remainingQuestionsTextView, currentPositionTextView;
    private int wins, loses;
    private ArrayList<TestCategoriesDataStructure> list;
    private SharedPreferences sharedPreferences;
    private int last;
    private int lockRangeUp,lockRangeDown;
    private ToggleButton lockSwitch;
    private boolean lockScroll;
    private InsertAndUpdateActivityViewModel viewModel;
    private int count;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("Realtimedb");
         //Realtimedb rt = new Realtimedb("d","e",false);
         //myRef.push().setValue(rt);




        //shared pref
        sharedPreferences =  getSharedPreferences("spinner", Context.MODE_PRIVATE);
        last = sharedPreferences.getInt("last",0);
        count = sharedPreferences.getInt("count",0);

        lockSwitch = findViewById(R.id.lock_switch);



        //init wins and loses
       winsTextView = findViewById(R.id.textview_wins);
       losesTextView = findViewById(R.id.textview_losses);

        remainingQuestionsTextView = findViewById(R.id.remaining);


        currentPositionTextView = findViewById(R.id.textView_current_item_position);

        // init sayItButton
        sayItButton = findViewById(R.id.button_say_it);

        //init answer editText
        answeringEditText =  findViewById(R.id.edittext_answering);
        lockEditText = findViewById(R.id.lock_edit_text);


        lockEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                lockSwitch();
            }
        });

        //view model init


        mainActivityViewModel = new ViewModelProvider(this).get(MainActivityViewModel.class);


        myRef.orderByChild("h").equalTo(false).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


               int fast= -1;

               int counter=20;

              myRef.removeEventListener(this);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {


                    if (counter >= 20){
                        counter=0;
                        fast++;

                        mainActivityViewModel.addNewCategory(count+fast+"");


                    }
                    counter++;


                    Realtimedb rr = dataSnapshot.getValue(Realtimedb.class);

                    String key = (String) dataSnapshot.getKey();

                    rr.setH(true);

                    Map<String, Object> ays = rr.toMap();

                    Map<String, Object> childUpdates = new HashMap<>();

                    childUpdates.put(key, ays);


                    LangTestDataStructure xx = new LangTestDataStructure(rr.getD(), rr.getE());

                    xx.setCategoryName(count+fast+"");



                    mainActivityViewModel.insertLangTestItem(xx);


                    Log.d("Tag", "" + rr.getE());

                    myRef.updateChildren(childUpdates);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        // mainActivityViewModel.firebase();

        //spinner init
        categorySpinner = findViewById(R.id.spinner_lessons);

        list = new ArrayList<>();
        categorySpinnerAdapter =   new SpinnerAdapter(this, list);
        categorySpinner.setAdapter(categorySpinnerAdapter);

        //load spinner
        try {
            mainActivityViewModel.getAllCategoryItems().observe(this, new Observer<List<TestCategoriesDataStructure>>() {
                @Override
                public void onChanged(List<TestCategoriesDataStructure> strings) {




                    list.clear();
                    list.addAll(strings);
                    categorySpinnerAdapter.notifyDataSetChanged();

                    sharedPreferences.edit().putInt("count",list.size()).apply();

                    Log.v("MainActivity.Log","spinnerLiveDataAccessedLast:"+last);

                    if (last != -1){
                    categorySpinner.setSelection(last);
                    last=-1;
                    }

                }
            });
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }


        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                 try {

                     mainActivityViewModel.getAllCategoryLangTestItemsInRandOrder(((TestCategoriesDataStructure) categorySpinner.getSelectedItem()).getCategoryName());
                    wins =  ((TestCategoriesDataStructure) categorySpinner.getSelectedItem()).getWins();
                     loses =   ((TestCategoriesDataStructure) categorySpinner.getSelectedItem()).getLosses();
                     winsTextView.setText(String.valueOf(wins));
                    losesTextView.setText(String.valueOf(loses));
                     remainingQuestionsTextView.setText(String.valueOf(langTestRecyclerViewAdapter.getItemCount()));

                     answeringEditText.setText("");
                    sharedPreferences.edit().putInt("last",position).apply();




                } catch (ExecutionException | InterruptedException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {


            }
        });


        //recyclerView init
        langTestRecyclerView = findViewById(R.id.recycler_view_questions);
        langTestRecyclerView.setLayoutManager(
                new LinearLayoutManager(this,
                        LinearLayoutManager.HORIZONTAL,false));

        AllCategoryLangTestItemsRandList = new ArrayList<>();
        langTestRecyclerViewAdapter = new MainListAdapter(AllCategoryLangTestItemsRandList);
        langTestRecyclerView.setAdapter(langTestRecyclerViewAdapter);

        mainActivityViewModel.getAllCategoryLangTestItemsInRandOrder().observe(this, new Observer<List<LangTestDataStructure>>() {
            @Override
            public void onChanged(List<LangTestDataStructure> langTestDataStructures) {

                AllCategoryLangTestItemsRandList.clear();
                AllCategoryLangTestItemsRandList.addAll(langTestDataStructures);
                langTestRecyclerViewAdapter.notifyDataSetChanged();
                remainingQuestionsTextView.setText(String.valueOf(langTestRecyclerViewAdapter.getItemCount()));

            }
        });
        (new PagerSnapHelper()).attachToRecyclerView(langTestRecyclerView);




        //list sweeps
        touchHelper();

        //init voice
        initVoice();

        //onRecyclerViewScroll
        recyclerViewScroll ();







    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.mian_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {

        //stop the voice engine in case if its speaking.
        if (textToSpeech.isSpeaking()) textToSpeech.stop();

        //shutdown text to speech engine
        if (textToSpeech != null) textToSpeech.shutdown();
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        if (textToSpeech != null && textToSpeech.isSpeaking()) textToSpeech.stop();
        if (answeringEditText != null) answeringEditText.setText("");
        super.onPause();
    }




    public void addingQuestionButton(View view) {

        String lessonText =
                (categorySpinnerAdapter.getCount() > 0) ?
                        ((TestCategoriesDataStructure) categorySpinner.getSelectedItem()).getCategoryName():"";


        startActivityForResult((new Intent(MainActivity.this, InsertAndUpdateActivity.class)
                .putExtra(CATEGORY_NAME,lessonText)),1);

    }

    public void submitButton(View view) {
        //after click disable the button until further action
        //get the current question position in adapter
        int firstVisibleItem = ((LinearLayoutManager)
                Objects.requireNonNull(langTestRecyclerView.
                        getLayoutManager())).
                findFirstVisibleItemPosition();
        if (langTestRecyclerViewAdapter.getItemCount() > 0) {
            TestCategoriesDataStructure spin = ((TestCategoriesDataStructure) categorySpinner.getSelectedItem());

            LangTestDataStructure dic = langTestRecyclerViewAdapter.getItem(firstVisibleItem);

            //if answer is correct.. if answer in edit text equal to stored answer
            if (answeringEditText.getText().toString().toLowerCase().trim().equals(dic.getForeignText())) {

                //null the editText
                answeringEditText.setText("");
                dic.setIsAnswered(true);
                 langTestRecyclerViewAdapter.removeAt(firstVisibleItem);
                dic.setMistakes(dic.getMistakes()-1);
                remainingQuestionsTextView.setText(String.valueOf(langTestRecyclerViewAdapter.getItemCount()));
                mainActivityViewModel.updateLangTestItem(dic);
                 wins++;
                winsTextView.setText(String.valueOf(wins));
                spin.setWins(wins);
                mainActivityViewModel.updateTestCategoryParams(spin);
            }
            else {
                dic.setMistakes(dic.getMistakes()-2);
                mainActivityViewModel.updateLangTestItem(dic);
                loses++;
                mainActivityViewModel.updateLangTestItem(dic);
                losesTextView.setText(String.valueOf(loses));
                spin.setLosses(loses);
                mainActivityViewModel.updateTestCategoryParams(spin);
            }
        }
    }

    public void sayItButton(View view) {

        textToSpeech.speak(answeringEditText.getText().toString(),
                TextToSpeech.QUEUE_FLUSH,null,null);

    }

    public void onQuestionDoubleClicked(View view){

        //stop and resume
        if (langTestRecyclerView.isLayoutSuppressed()){

            langTestRecyclerView.suppressLayout(false);
        }
        else     langTestRecyclerView.suppressLayout(true);



        //capturing click time
        long clickTime = System.currentTimeMillis();

        //capture the tag e.i. resultsAdapterPosition of the question
        int clickTag = (int) view.getTag();

        // if double click then show the answer.
        long DOUBLE_TAP_MAX_DELAY = 300;
        if (clickTag == prevClickTag &&
                clickTime - prevClickTime < DOUBLE_TAP_MAX_DELAY) {

            //try to avoid a freak accident of null exception.
            try {

                //get the answer and update the editText.

                answeringEditText
                        .setText(String.valueOf(langTestRecyclerViewAdapter.getSolution(clickTag)));

            }

            //catch the potential null error
            catch (Exception e) {

                //print error message
                Log.e("MainActivity", e.toString());
            }
        }

        //storing click time and last question clicked
        prevClickTime = clickTime;
        prevClickTag = clickTag;


    }


    public void touchHelper(){

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.UP | ItemTouchHelper.DOWN) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int resultsAdapterPosition = viewHolder.getAdapterPosition();
                LangTestDataStructure dic = langTestRecyclerViewAdapter.getItem(resultsAdapterPosition);
                if (direction == ItemTouchHelper.DOWN) {

                    mainActivityViewModel.deleteLangTestItem(dic);

                    langTestRecyclerViewAdapter.removeAt(resultsAdapterPosition);

                    answeringEditText.setText("");
                    remainingQuestionsTextView.setText(String.valueOf(langTestRecyclerViewAdapter.getItemCount()));

                }

                else if (direction == ItemTouchHelper.UP) {
                    String lessonText = (categorySpinnerAdapter.getCount() > 0) ? ((TestCategoriesDataStructure) categorySpinner.getSelectedItem()).getCategoryName():"";



                    startActivityForResult(
                            new Intent(MainActivity.this,
                                    InsertAndUpdateActivity.class)
                                    .putExtra(CATEGORY_NAME,lessonText)
                                    .putExtra(Constants.ID, dic.getId())
                                    .putExtra(Constants.YOUR_Text, dic.getYourText())
                                    .putExtra(Constants.Foreign_Text, dic.getForeignText()),1);

                    answeringEditText.setText("");

                }

            }
        }).attachToRecyclerView(langTestRecyclerView);

    }


    public void resetLesson(MenuItem item) throws ExecutionException, InterruptedException {


        if (categorySpinnerAdapter.getCount()>0){

               winsTextView.setText("0"); // it should be live data
                losesTextView.setText("0");

            wins=0;
            loses=0;
             mainActivityViewModel.resetCategory(((TestCategoriesDataStructure) categorySpinner.getSelectedItem()).getCategoryName());
           mainActivityViewModel.getAllCategoryLangTestItemsInRandOrder(((TestCategoriesDataStructure) categorySpinner.getSelectedItem()).getCategoryName());
           answeringEditText.setText("");

           langTestRecyclerView.scrollToPosition(0);


        }
    }

    public void deleteCategory(MenuItem item) throws ExecutionException, InterruptedException {
if (list.size()>0 )
        {

            int pos= categorySpinner.getSelectedItemPosition();

            mainActivityViewModel.deleteCategory((list.get(pos)).getCategoryName());

            list.remove(pos);

            if (pos >= list.size() && !list.isEmpty())
                mainActivityViewModel.getAllCategoryLangTestItemsInRandOrder(list.get(pos-1).getCategoryName());
            else if (!list.isEmpty())
            mainActivityViewModel.getAllCategoryLangTestItemsInRandOrder(list.get(pos).getCategoryName());


            else {

                losesTextView.setText("0");
                winsTextView.setText("0");
                remainingQuestionsTextView.setText("0");

                AllCategoryLangTestItemsRandList.clear();
                langTestRecyclerViewAdapter.notifyDataSetChanged();
            }

            categorySpinnerAdapter.notifyDataSetChanged();

        }

}


    public void initVoice(){
        textToSpeech = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                if (i == TextToSpeech.SUCCESS){

                    int result = textToSpeech.setLanguage(Locale.GERMAN);

                    if (result == TextToSpeech.LANG_NOT_SUPPORTED ||
                            result == TextToSpeech.LANG_MISSING_DATA){

                    }
                    else {

                        sayItButton.setEnabled(true);

                    }

                }

            }
        });

    }

    public void recyclerViewScroll (){

        langTestRecyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {

                int firstVisibleItem = ((LinearLayoutManager)
                        Objects.requireNonNull(langTestRecyclerView.
                                getLayoutManager())).
                        findFirstVisibleItemPosition();

                currentPositionTextView.setText("/"+(firstVisibleItem+1));
                if (textToSpeech != null && textToSpeech.isSpeaking()) textToSpeech.stop();
                if (!answeringEditText.getText().toString().isEmpty()) answeringEditText.setText("");

                if (lockScroll && langTestRecyclerViewAdapter.getItemCount() > 0){
                if (firstVisibleItem > lockRangeUp ){

                         langTestRecyclerView.scrollToPosition(lockRangeDown);

                    }
                   else  if (firstVisibleItem < lockRangeDown){

                        if (langTestRecyclerViewAdapter.getItemCount() > lockRangeUp )
                      langTestRecyclerView.scrollToPosition(lockRangeUp);
                       else langTestRecyclerView.scrollToPosition(langTestRecyclerViewAdapter.getItemCount()-1);

                    }

                }






            }
        });




    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {

            String lessonText = (categorySpinnerAdapter.getCount() > 0) ? ((TestCategoriesDataStructure) categorySpinner.getSelectedItem()).getCategoryName():"";
            mainActivityViewModel.getAllCategoryLangTestItemsInRandOrder(lessonText);

            remainingQuestionsTextView.setText((String.valueOf(langTestRecyclerViewAdapter.getItemCount())));

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void switchLang(MenuItem item) throws ExecutionException, InterruptedException {

        langTestRecyclerViewAdapter.switchLang();
        resetLesson(item);

    }

    public void lockSwitch(View view) {


        lockSwitch();


    }

    public void lockSwitch() {
        ToggleButton switchButton =  lockSwitch;



        //here implement the lock
        if (switchButton.isChecked()) {
            String lockEditTextVal = lockEditText.getText().toString().trim();
            if (!lockEditTextVal.isEmpty()) {
                int lockVal = Integer.parseInt(lockEditTextVal);

                if (lockVal > 0) {

                    int firstVisibleItem = ((LinearLayoutManager)
                            Objects.requireNonNull(langTestRecyclerView.
                                    getLayoutManager())).
                            findFirstVisibleItemPosition();

                    lockRangeUp = firstVisibleItem + lockVal;
                    lockRangeDown = firstVisibleItem - lockVal;
                    if (lockRangeDown < 0) lockRangeDown = 0;
                    lockScroll = true;
                }
            }
        }
        //here unlock
        else {
            lockRangeDown=0;
            lockRangeUp=0;
            lockScroll=false;

        }




    }

}
