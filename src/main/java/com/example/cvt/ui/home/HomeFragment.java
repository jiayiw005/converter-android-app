package com.example.cvt.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.example.cvt.Homepage2Activity;
import com.example.cvt.IntroductionActivity;
import com.example.cvt.ModuleFragment;
import com.example.cvt.MySQLConnection;
import com.example.cvt.R;
import com.example.cvt.SharedPreference;
import com.example.cvt.contact.ContactFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class HomeFragment extends Fragment implements View.OnClickListener{

    View view;
    ImageButton mainBoard;
    ImageButton philBoard;
    ImageButton theaterBoard;
    ImageButton csBoard;

    FloatingActionButton newModule;

    public static Connection con = null;

    public View onCreateView(@NonNull LayoutInflater inflater,
            ViewGroup container, Bundle savedInstanceState) {

            //inflate view
            view = inflater.inflate(R.layout.fragment_home,container,false);

            //inflate widgets and set onClickListeners
            mainBoard = (ImageButton) view.findViewById(R.id.main_board);
            mainBoard.setOnClickListener(new View.OnClickListener() {
               @Override
                public void onClick(View v) {
                   Navigation.findNavController(view)
                           .navigate(R.id.action_homepage_to_introduction);
                }
            });

            philBoard = (ImageButton) view.findViewById(R.id.board_philosophy);
            philBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean loginstatus = SharedPreference.getLoginStatus(getContext());
                    if(loginstatus == true){
                        SharedPreference.setCurrentModule(getContext(),1);
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_module);
                    }else {
                        Toast.makeText(getContext(), "You're not logged in.", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_signup);
                    }
                }
            });

            theaterBoard = (ImageButton) view.findViewById(R.id.board_theater);
            theaterBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean loginstatus = SharedPreference.getLoginStatus(getContext());
                    if(loginstatus == true){
                        SharedPreference.setCurrentModule(getContext(),2);
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_module);
                    }else {
                        Toast.makeText(getContext(), "You're not logged in.", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_signup);
                    }
                }
            });

            csBoard = (ImageButton) view.findViewById(R.id.board_cs);
            csBoard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean loginstatus = SharedPreference.getLoginStatus(getContext());
                    if(loginstatus == true){
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_module);
                        SharedPreference.setCurrentModule(getContext(),3);
                    }else {
                        Toast.makeText(getContext(), "You're not logged in.", Toast.LENGTH_SHORT).show();
                        Navigation.findNavController(view).navigate(R.id.action_homepage_to_signup);
                    }
                }
            });

            newModule = (FloatingActionButton) view.findViewById(R.id.new_module);
            newModule.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_homepage_to_new_module);
                }
            });

            return view;
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onClick(View v) {
    }

}