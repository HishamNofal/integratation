package com.example.integratation.MainActivityUserInterfaceAndLogic;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.recyclerview.widget.RecyclerView;

import com.example.integratation.DB.LangTestDBElements.LangTestDataStructure;
import com.example.integratation.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainListAdapter extends RecyclerView.Adapter<MainListAdapter.MyViewHolder> {

    private List<LangTestDataStructure> mDataset;

    private boolean switchLock;
    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
     static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case

         EditText editText;

         MyViewHolder(View v) {
            super(v);

            editText = v.findViewById(R.id.textview_your_lang);

        }
    }

     LangTestDataStructure getItem(int pos){

        return mDataset.get(pos);

    }
    String getSolution(int pos){


        if (switchLock) return mDataset.get(pos).getYourText();

        else return mDataset.get(pos).getForeignText();

    }



    // Provide a suitable constructor (depends on the kind of dataset)
     MainListAdapter(ArrayList<LangTestDataStructure> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MainListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {

        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.dictionary_item, parent, false);
         MyViewHolder vh = new MyViewHolder(v);

        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        LangTestDataStructure current = mDataset.get(position);

        if (current.getYourText().contains(",")) {
            StringBuilder str = new StringBuilder();

            String[] array = current.getYourText().split(",");

            List<String> arr = Arrays.asList(array);

            Collections.shuffle(arr, new Random(System.nanoTime()));

            for (String str_ : arr)
                str.append(str_.trim()).append(", ");

            str.setLength(str.length()-2);

            current.setYourText(str.toString());
        }


        if (switchLock) holder.editText.setText(current.getForeignText());

        else holder.editText.setText(current.getYourText());

        holder.editText.setTag(position);

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }


    @Override
    public long getItemId(int position) {
        return mDataset.get(position).getId();
    }

    @Override
    public int getItemViewType(int position) {
        return mDataset.get(position).getId();
    }

    public void removeAt(int position) {
        if (mDataset.size()==1){
            mDataset.clear();
            notifyDataSetChanged();
        }

        else{
        mDataset.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mDataset.size());
        notifyDataSetChanged();}

    }


    public void switchLang(){
         switchLock = !switchLock;
         notifyDataSetChanged();
     }


}