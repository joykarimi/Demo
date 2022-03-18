package com.example.project;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.vansuita.pickimage.bean.PickResult;
import com.vansuita.pickimage.bundle.PickSetup;
import com.vansuita.pickimage.dialog.PickImageDialog;
import com.vansuita.pickimage.listeners.IPickResult;

import java.util.UUID;

public class Drawer extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,IPickResult {

    ImageView imgProfile;
    private DrawerLayout drawer;
    DatabaseReference usersDB, workersDB, FAQsDB;
    UserModel user;
    View nav_header;
    TextView name, email;
    String uid;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer);

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences preferences=getSharedPreferences("UID",MODE_PRIVATE);
        uid=preferences.getString("uid","");

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        nav_header = LayoutInflater.from(this).inflate(R.layout.nav_header, null);
        navigationView.addHeaderView(nav_header);


        usersDB = FirebaseDatabase.getInstance().getReference("Users");
        workersDB = FirebaseDatabase.getInstance().getReference("Workers");
        FAQsDB = FirebaseDatabase.getInstance().getReference("FAQs");
        loadUserData();
        //insertWorkers();
        //insertFAQs();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }


        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setTitle("");
        toolbar.setSubtitle("");

        imgProfile=nav_header.findViewById(R.id.img_profile);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PickImageDialog.build(new PickSetup()).show(Drawer.this);
            }
        });
    }

    private void loadUserData() {
        usersDB.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                user = dataSnapshot.getValue(UserModel.class);
                displayUserInfo();
                loadImageFromDB();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    void displayUserInfo(){
        name = nav_header.findViewById(R.id.nav_name);
        email = nav_header.findViewById(R.id.nav_email);
        name.setText(user.getName());
        email.setText(user.getEmail());
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;

            case R.id.nav_profile:
                Bundle bundle = new Bundle();
                bundle.putSerializable("user", user);
                bundle.putString("uid", uid);
                ProfileFragment f = new ProfileFragment();
                f.setArguments(bundle);
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        f).commit();
                break;

            case R.id.navigation_schedule:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new ScheduleFragment()).commit();
                break;

            case R.id.navigation_history:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HistoryFragment()).commit();
                break;

            case R.id.navigation_rate:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new Rate()).commit();
                break;

            case R.id.nav_faqs:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new FAQsFragment()).commit();
                break;

           case R.id.nav_logout:
               SharedPreferences preferences =(SharedPreferences) getSharedPreferences("checkbox", MODE_PRIVATE);
               SharedPreferences.Editor editor = preferences.edit();
               editor.putString("remember", "false");
               editor.apply();
               finish();
               Intent intent1=new Intent(Drawer.this, Login.class);
               startActivity(intent1);
               //getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
               //        new LogoutFragment()).commit();
              break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPickResult(PickResult r) {
        imgProfile.setImageBitmap(r.getBitmap());
        uploadImage(r.getUri(),r);
    }


    private void loadImageFromDB(){
        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/"+uid);
        final long ONE_MEGABYTE = 1024 * 1024;
        storageReference.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytesPrm) {
                Bitmap bmp = BitmapFactory.decodeByteArray(bytesPrm, 0, bytesPrm.length);
                imgProfile.setImageBitmap(bmp);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                imgProfile.setImageResource(R.drawable.profilepic);
            }
        });
    }

    private void uploadImage(final Uri filePath, final PickResult r) {

        if(filePath != null)
        {
            StorageReference ref = storageReference.child("images/"+uid);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            usersDB.child(uid).child("imageRef").setValue(r.getPath());
                            Toast.makeText(Drawer.this, "Image Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Drawer.this, "Failed to Upload Image"+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    void insertWorkers(){

        Workers w1 = new Workers("Ali", "Carpenter");
        Workers w2 = new Workers("Usman", "Painter");
        Workers w3 = new Workers("Awais", "Electrician");
        Workers w4 = new Workers("Riaz", "Plumber");
        Workers w5 = new Workers("Abdullah", "Technician");
        Workers w6 = new Workers("Khalil", "Carpenter");
        Workers w7 = new Workers("Baji", "Maid");
        Workers w8 = new Workers("Ramsha", "Maid");

        String uid;
        uid = UUID.randomUUID().toString();
        w1.setUid(uid);
        workersDB.child(uid).setValue(w1);

        uid = UUID.randomUUID().toString();
        w2.setUid(uid);
        workersDB.child(uid).setValue(w2);

        uid = UUID.randomUUID().toString();
        w3.setUid(uid);
        workersDB.child(uid).setValue(w3);

        uid = UUID.randomUUID().toString();
        w4.setUid(uid);
        workersDB.child(uid).setValue(w4);

        uid = UUID.randomUUID().toString();
        w5.setUid(uid);
        workersDB.child(uid).setValue(w5);

        uid = UUID.randomUUID().toString();
        w6.setUid(uid);
        workersDB.child(uid).setValue(w6);

        uid = UUID.randomUUID().toString();
        w7.setUid(uid);
        workersDB.child(uid).setValue(w7);

        uid = UUID.randomUUID().toString();
        w8.setUid(uid);
        workersDB.child(uid).setValue(w8);
    }

    void insertFAQs(){

        FAQsDB.child(UUID.randomUUID().toString()).setValue("How much does a carpenter cost?\n\nCarpenter cost 800 RS per hour");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("How much does a plumber cost?\n\nPlumber cost 1200 RS per hour");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("What time does plumber provides services?\n\nPlumber provides services according to situation");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("How can I hire maid on monthly basis?");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("How much does a maid cost?\n\nMaid cost 800 RS per hour");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("What does painter do?");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("What is the maximum amount a plumber can charge");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("Can we hire maid on yearly basis?");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("How much does a carpenter cost?");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("What kind of jobs technicians can handle?");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("How much electrician charge for 6 hours of work daily?");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("How much does a painter cost?\n\nPainter cost 1250 RS per hour");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("How much does a technician cost?\n\nTechnician cost 800 RS per hour");
        FAQsDB.child(UUID.randomUUID().toString()).setValue("How much does a carpenter cost?\n\nCarpenter cost 800 RS per hour");
    }

}
