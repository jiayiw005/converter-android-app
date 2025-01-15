package com.example.cvt.introduction;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.cvt.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IntroductionFragment extends Fragment implements View.OnClickListener{

    View view;

    public static IntroductionFragment newInstance() {
        return new IntroductionFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        //inflate fragment layout
        view = inflater.inflate(R.layout.fragment_introduction, container, false);

        //set widget's onClickListener and navigation
        FloatingActionButton goContact = (FloatingActionButton) view.findViewById(R.id.go_contact);
        goContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.action_introduction_to_contact);
            }
        });

        return view;
    }

    @Override
    public void onClick(View v) {
    }
}