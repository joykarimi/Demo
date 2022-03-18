package com.example.project;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class QuestionAnswer extends AppCompatActivity {

    SearchView searchView;
    ListView listView;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question_answer);

        searchView=(SearchView)findViewById(R.id.searchView);
        listView=(ListView)findViewById(R.id.listView);

        list=new ArrayList<String>();
        list.add("How much does a carpenter cost?");
        list.add("What time does plumber provides services?");
//        list.add("How can I hire maid on monthly basis?");
//        list.add("How much does a carpenter cost?");
//        list.add("What time does plumber provides services?");
//        list.add("How can I hire maid on monthly basis?");
//        list.add("How can I hire maid on monthly basis?");
//        list.add("How can I hire maid on monthly basis?");
//        list.add("How can I hire maid on monthly basis?");
//        list.add("How can I hire maid on monthly basis?/nthis is how you will be served");
//        list.add("How can I hire maid on monthly basis?");

        adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }
}
