package com.example.integratation.InsertAndUpdateActivityUserInterfaceAndLogic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;

import com.example.integratation.Constants;
import com.example.integratation.DB.LangTestDBElements.LangTestDataStructure;
import com.example.integratation.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

import static com.example.integratation.Constants.CATEGORY_NAME;
import static com.example.integratation.Constants.ID;
import static com.example.integratation.Constants.Foreign_Text;
import static com.example.integratation.Constants.YOUR_Text;

public class InsertAndUpdateActivity extends AppCompatActivity {

    private AutoCompleteTextView lessonText;
    private EditText deutschEditText, englishEditText;
    private Button submitButton;
    private InsertAndUpdateActivityViewModel viewModel;
    private ArrayAdapter adap;
    private ArrayList<String> list;
    private ClipboardManager clipboardManager;
    private long primaryLastTime;
    private ArrayList<String> newDictionaryEntry;
    boolean submitTrueClipboardFalse =false;
    int id=-1;
    private WebView webView;
    private WebView webView2;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_and_update);




        //init view model
        viewModel = new ViewModelProvider(this).get(InsertAndUpdateActivityViewModel.class);

        //init auto complete list
        lessonText = findViewById(R.id.edit_text_lesson);

lessonText.setText(getIntent().getStringExtra(CATEGORY_NAME));



      try {
            viewModel.getAllCategoriesItemNames().observe(this, new Observer<List<String>>() {
               @Override
               public void onChanged(List<String> strings) {
                   ArrayList<String>  list12 = new ArrayList<>();
                   list12.addAll(strings);

                    lessonText.setAdapter(new ArrayAdapter<>(
                              InsertAndUpdateActivity.this,R.layout.support_simple_spinner_dropdown_item,strings));
               }
           });
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


        //init edit texts
        deutschEditText= findViewById(R.id.editText_deutsch);
        englishEditText= findViewById(R.id.editText_english);

        deutschEditText.setText(getIntent().getStringExtra(Foreign_Text));
        englishEditText.setText(getIntent().getStringExtra(YOUR_Text));
        id= getIntent().getIntExtra(ID,-1);

        newDictionaryEntry = new ArrayList<>();
        clipboardManager = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);


        if (clipboardManager != null) {
            clipboardManager.
                    setPrimaryClip(ClipData.newPlainText(Constants.Foreign_Text, deutschEditText.getText().toString().toLowerCase()));
        }

        if (clipboardManager != null) {


            //install clipBoard change listener
            clipboardManager.addPrimaryClipChangedListener(new ClipboardManager.OnPrimaryClipChangedListener() {
                @Override
                public void onPrimaryClipChanged() {
                    long primaryCurrentTime = System.currentTimeMillis();

                    //doubleClick prevention
                    if (primaryCurrentTime - primaryLastTime > 300) {
                        ClipData.Item copiedData = clipboardManager.
                                getPrimaryClip().getItemAt(0);
                        if (copiedData != null)
                            newDictionaryEntry.add(copiedData.getText().toString());
                    }


                    if (newDictionaryEntry.size() == 1){
                        deutschEditText.setText(newDictionaryEntry.get(0));
                        try {
                            submitTrueClipboardFalse =false;
                            viewModel.findALangTestItem(newDictionaryEntry.get(0));
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }

                    }

                    else if (newDictionaryEntry.size() == 2) {

                        try {
                            submitTrueClipboardFalse =false;
                            viewModel.findALangTestItem(newDictionaryEntry.get(0));
                        } catch (ExecutionException | InterruptedException e) {
                            e.printStackTrace();
                        }
                    }

                    primaryLastTime = primaryCurrentTime;

                }

            });
        }


viewModel.findALangTestItem().observe(this, new Observer<LangTestDataStructure>() {
    @Override
    public void onChanged(LangTestDataStructure langTestDataStructure) {


        //handling clipboard invocations
        if (!submitTrueClipboardFalse){

        if (langTestDataStructure != null & newDictionaryEntry.size() == 1){
            //if it's in database then set the english text to database text
            englishEditText.setText(langTestDataStructure.getYourText());
        }

        else if (newDictionaryEntry.size() ==2){

            englishEditText.setText(newDictionaryEntry.get(1));
            newDictionaryEntry.clear();

        }


        }

        //handling submit invocations and not update
        else if (id == -1){
            LangTestDataStructure dic = new LangTestDataStructure(deutschEditText.getText().toString().toLowerCase().trim(),
                    englishEditText.getText().toString().toLowerCase().trim());

            dic.setCategoryName(lessonText.getText().toString().trim());


            if (langTestDataStructure != null){

                viewModel.deleteLangTestItem(langTestDataStructure);

            }

            viewModel.addNewCategory(lessonText.getText().toString().trim());
            viewModel.insertLangTestItem(dic);

            deutschEditText.setText("");
            englishEditText.setText("");

        }

        // submit and update
        else {

           if (langTestDataStructure != null)
               viewModel.deleteLangTestItem(langTestDataStructure);


            LangTestDataStructure dic = new LangTestDataStructure(deutschEditText.getText().toString().toLowerCase().trim(),
                    englishEditText.getText().toString().toLowerCase().trim());

            dic.setCategoryName(lessonText.getText().toString().trim());
            dic.setId(id);

            viewModel.updateLangTestItem(dic);
            id=-1;


            deutschEditText.setText("");
            englishEditText.setText("");
        }





    }
});

        webView = findViewById(R.id.webview);

        //init webView
        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        webView.setWebViewClient(new WebViewClient() {
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.contains("https://www.deepl.com/en/translator#de/en/")) {
                    view.loadUrl(url);
                }
                return true;
            }
        });
        webView.loadUrl("https://www.deepl.com/en/translator#de/en/");




    }

    public void submit(View view) throws ExecutionException, InterruptedException {

        if (englishEditText.getText().toString().trim().isEmpty()&&
                !deutschEditText.getText().toString().trim().isEmpty()){

            String[] ss = viewModel.cambridgeTranslation(deutschEditText.getText().toString());

            englishEditText.setText(ss[0]);

            if (!ss[1].isEmpty())
            deutschEditText.setText(ss[1]);

        }
        else {

    if (!deutschEditText.getText().toString().trim().isEmpty() && !englishEditText.getText().toString().trim().isEmpty())

        submitTrueClipboardFalse =true;
        viewModel.findALangTestItem(deutschEditText.getText().toString().toLowerCase().trim());

    }

    }
}
