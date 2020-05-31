package com.example.exkost;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;

public class LogSecondFragment extends Fragment {

    View view;
    CardView secondButton;
    TextView secondFragment;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.login_fragment_two, container, false);

        secondFragment = (TextView) view.findViewById(R.id.secondFragment);
        secondFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Activity activity = getActivity();
                if(activity instanceof MainActivity){
                    MainActivity myactivity = (MainActivity) activity;
                    myactivity.loadFragment(new LogFirstFragment());
                }
            }
        });

        secondButton = (CardView) view.findViewById(R.id.buttonDaftar);
        secondButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

// display a message by using a Toast
                Toast.makeText(getActivity(), "Anda Mendaftar", Toast.LENGTH_LONG).show();
// display a message by using a Toast
                Intent intent = new Intent(getActivity(), MainActivity.class);
                startActivity(intent);

            }
        });

        return view;

    }
}
