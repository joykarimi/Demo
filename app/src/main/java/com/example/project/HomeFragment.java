package com.example.project;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

public class HomeFragment extends Fragment implements View.OnClickListener{

    private CardView maid,electrician,carpnter,painter,plumber,technician;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.activity_home_fragment, container, false);
        maid=(CardView)view.findViewById(R.id.card1);
        electrician=(CardView)view.findViewById(R.id.card2);
        carpnter=(CardView)view.findViewById(R.id.card3);
        painter=(CardView)view.findViewById(R.id.card4);
        plumber=(CardView)view.findViewById(R.id.card5);
        technician=(CardView)view.findViewById(R.id.card6);

        maid.setOnClickListener(this);
        electrician.setOnClickListener(this);
        carpnter.setOnClickListener(this);
        painter.setOnClickListener(this);
        plumber.setOnClickListener(this);
        technician.setOnClickListener(this);
        return view;
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.card1: intent=new Intent(getActivity(),TDP.class);
            intent.putExtra("type", "Maid");
            startActivity(intent);break;
            case R.id.card2: intent=new Intent(getActivity(),TDP.class);
            intent.putExtra("type", "Electrician");
            startActivity(intent);break;
            case R.id.card3: intent=new Intent(getActivity(),TDP.class);
            intent.putExtra("type", "Carpenter");
            startActivity(intent);break;
            case R.id.card4: intent=new Intent(getActivity(),TDP.class);
            intent.putExtra("type", "Painter");
            startActivity(intent);break;
            case R.id.card5: intent=new Intent(getActivity(), TDP.class);
            intent.putExtra("type", "Plumber");
            startActivity(intent);break;
            case R.id.card6: intent=new Intent(getActivity(), TDP.class);
            intent.putExtra("type", "Technician");
            startActivity(intent);break;
            default:break;
        }
    }
}
