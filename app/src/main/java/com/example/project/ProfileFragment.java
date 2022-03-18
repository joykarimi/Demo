package com.example.project;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfileFragment extends Fragment {

    TextView name, userName, email, contact, address;
    ImageView proflePhoto;
    String uid;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_profile_fragment, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        Bundle bundle = getArguments();
        UserModel model = (UserModel) bundle.getSerializable("user");
        uid = bundle.getString("uid");
        viewDisplay(model);
    }

    void viewDisplay(UserModel user){
        name = getView().findViewById(R.id.name);
        userName = getView().findViewById(R.id.textview2);
        contact = getView().findViewById(R.id.textview4);
        email = getView().findViewById(R.id.textview6);
        address = getView().findViewById(R.id.textview8);
        proflePhoto = getView().findViewById(R.id.imageview2);

        name.setText(user.getName());
        userName.setText(user.getName());
        contact.setText(user.getPno());
        email.setText(user.getEmail());
        address.setText("House no "+user.getHouse()+", Street no "+user.getStreet()+
                ", Phase no "+ user.getSector()+", "+user.getArea()+", "+user.getCity()+ " Pakistan");
        loadImageFromDB();
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

}
