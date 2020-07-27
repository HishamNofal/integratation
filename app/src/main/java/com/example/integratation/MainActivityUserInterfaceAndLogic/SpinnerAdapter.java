package com.example.integratation.MainActivityUserInterfaceAndLogic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.integratation.DB.TestCategoriesDBElements.TestCategoriesDataStructure;
import com.example.integratation.R;

import java.util.ArrayList;

public class SpinnerAdapter extends ArrayAdapter<TestCategoriesDataStructure> {

Context cont;
    ArrayList<TestCategoriesDataStructure> list ;
    public SpinnerAdapter(@NonNull Context context, ArrayList<TestCategoriesDataStructure> list) {
        super(context, 0, list);
        this.cont = context;
        this.list =list;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(cont).inflate(R.layout.spinner_adap,parent,false);

        ( (TextView)listItem.findViewById(R.id.item_textView)).setText(list.get(position).getCategoryName());

        return listItem;

    }


    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(cont).inflate(R.layout.spinner_adap,parent,false);

        ( (TextView)listItem.findViewById(R.id.item_textView)).setText(list.get(position).getCategoryName());

        return listItem;    }
}
