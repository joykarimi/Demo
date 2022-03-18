package com.example.project;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import static android.content.Context.MODE_PRIVATE;

public class ScheduleFragment extends Fragment {

    ListView listView;
    DatabaseReference taskTB, workerDB, usersDB;
    String uid;
    ImageView proflePhoto;
    TextView nameTxt;

    String[] name= new String[100];
    String[] job= new String[100];
    String[] date= new  String[100];
    String[] datedetails= new String[100];
    String[] time= new String[100];
    String[] timedetails= new String[100];
    String[] rate= new String[100];
    String[] ratedetails= new String[100];
    Integer [] imgid= new Integer[100];
    String [] taskUid = new String[100];
    String [] workersUid = new String[100];

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_schedule_fragment, container, false);
        listView=(ListView)view.findViewById(R.id.schedule_listview);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        nameTxt = getView().findViewById(R.id.name);
        proflePhoto  = getView().findViewById(R.id.imageview2);
        SharedPreferences preferences= getActivity().getSharedPreferences("UID",MODE_PRIVATE);
        uid=preferences.getString("uid","");
        taskTB = FirebaseDatabase.getInstance().getReference("Tasks");
        workerDB = FirebaseDatabase.getInstance().getReference("Workers");
        usersDB = FirebaseDatabase.getInstance().getReference("Users");
        displayUser();
        populateData();
        markScheduledTaskComplete();
    }


    void displayUser(){
        usersDB.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UserModel user = dataSnapshot.getValue(UserModel.class);
                nameTxt.setText(user.getName());
                loadImageFromDB();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void loadImageFromDB(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+uid);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytesPrm) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.length);
                proflePhoto.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                proflePhoto.setImageResource(R.drawable.profilepic);
            }
        });
    }

    void populateData(){

        taskTB.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                int index = 0;
                ScheduleTaskModel task;
                for(DataSnapshot i: dataSnapshot.getChildren()) {
                    task = i.getValue(ScheduleTaskModel.class);
                    if(task.getUserUid().equalsIgnoreCase(uid) && !task.getTaskDone()) {
                        name[index] = task.getWorkerName();
                        job[index] = task.getJobType();
                        date[index] = "Date: ";
                        datedetails[index] = task.getDayOfMonth()+ "/" + (task.getMonth()+1) + "/" + task.getYear();
                        time[index] = "Time: ";
                        if(task.getHourOfDay() > 12) {
                            timedetails[index] = (task.getHourOfDay()-12) + ":" + task.getMinute() + " PM";
                        }
                        else {
                            timedetails[index] = (task.getHourOfDay()) + ":" + task.getMinute() + " AM";
                        }
                        rate[index] = "Rate per hour: ";
                        ratedetails[index] = "800 RS";
                        imgid[index] = R.drawable.profilepic;
                        taskUid[index] = task.getTaskUid();
                        workersUid[index] = task.getWorkerUid();
                        index++;
                    }
                }

                String[] name1= new String[index];
                String[] job1= new String[index];
                String[] date1= new  String[index];
                String[] datedetails1= new String[index];
                String[] time1= new String[index];
                String[] timedetails1= new String[index];
                String[] rate1= new String[index];
                String[] ratedetails1= new String[index];
                Integer [] imgid1= new Integer[index];

                for(int i=0; i < index; i++) {

                    name1[i] = name[i];
                    job1[i] = job[i];
                    date1[i] = date[i];
                    datedetails1[i] = datedetails[i];
                    time1[i] = time[i];
                    timedetails1[i] = timedetails[i];
                    rate1[i] = rate[i];
                    ratedetails1[i] = ratedetails[i];
                    imgid1[i] = imgid[i];

                }

                CustomScheduleList customScheduleList=new CustomScheduleList(getActivity(),name1,job1,date1,datedetails1,time1,timedetails1,rate1,ratedetails1,imgid1);
                listView.setAdapter(customScheduleList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    void markScheduledTaskComplete(){

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                new AlertDialog.Builder(getContext()).setTitle("Task Completed")
                        .setMessage("Do you want to mark this job complete?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                taskTB.child(taskUid[position]).child("taskDone").setValue(true);
                                workerDB.child(workersUid[position]).child("acquired").setValue(false);
                                populateData();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });

    }
}
