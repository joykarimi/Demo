package com.example.project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FAQsFragment extends Fragment {

    SearchView searchView;
    ListView listView;

    ArrayList<String> list;
    ArrayAdapter<String> adapter;
    DatabaseReference FAQsDB;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_faqs_fragment, container, false);
        searchView=(SearchView) view.findViewById(R.id.searchView);
        listView=(ListView)view.findViewById(R.id.listView);
        FAQsDB = FirebaseDatabase.getInstance().getReference("FAQs");


        FAQsDB.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                list=new ArrayList<String>();
                for(DataSnapshot i: dataSnapshot.getChildren()) {
                    list.add(i.getValue(String.class));
                }
                adapter=new ArrayAdapter<String>(getActivity(),android.R.layout.simple_list_item_1,list);
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
        return view;
    }
}
