package com.example.exkost;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

public class LogSecondFragment extends Fragment {

    View view;
    Button secondButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

// Inflate the layout for this fragment
        view = inflater.inflate(R.layout.login_fragment_second, container, false);

// get the reference of Button
        secondButton = (Button) view.findViewById(R.id.buttonDaftar);

// perform setOnClickListener on second Button
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
