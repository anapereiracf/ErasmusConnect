package com.example.erasmushelp.ui;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.erasmushelp.R;
import com.example.erasmushelp.data.Consts;
import com.example.erasmushelp.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class UserProfile extends Fragment implements Consts {

    private ImageButton goBackButton;
    private Button goSettingsBtn;
    private DatabaseReference database;
    private TextView nameView, ageView, birthView, homeCountryView, nativeLangView, homeUniView, usernameView;
    private ImageView profilepic;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_user_profile, container, false);

        initializeElements(root);
        initializeDatabase();
        clickListeners(root);

        return root;
    }

    private void initializeDatabase() {
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance(DATABASE_PATH).getReference().child(PATH).child(fbuser.getUid());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    User user = snapshot.getValue(User.class);
                    assert user != null;
                    fillInformation(user);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read failed");
            }
        });
    }

    private void fillInformation(User user) {
        nameView.setText(new StringBuilder().append("Name: ").append(user.getName()).toString());
        ageView.setText(new StringBuilder().append("Age: ").append(user.getAge()).toString());
        nativeLangView.setText(new StringBuilder().append("Mother Tongue: ").append(user.getMotherTongue()).toString());
        homeUniView.setText(new StringBuilder().append("Home University: ").append(user.getHomeUniversity()).toString());
        homeCountryView.setText(new StringBuilder().append("Home Country: ").append(user.getHomeCountry()).toString());
        birthView.setText(new StringBuilder().append("Birthdate: ").append(user.getBirthdate()).toString());
        usernameView.setText(new StringBuilder().append("User ").append(user.getName()).toString());

        if(!user.getImage().isEmpty()){
            Glide.with(this).load(user.getImage()).placeholder(R.drawable.avatarpfp).fitCenter().into(profilepic);
        }
    }


    private void initializeElements(View root) {

        nameView = root.findViewById(R.id.name);
        ageView = root.findViewById(R.id.age);
        birthView = root.findViewById(R.id.birth);
        homeCountryView = root.findViewById(R.id.homeCountry);
        nativeLangView = root.findViewById(R.id.motherTongue);
        homeUniView = root.findViewById(R.id.homeUni);
        goBackButton = root.findViewById(R.id.backHomeBtn);
        goSettingsBtn = root.findViewById(R.id.goToSetts);
        usernameView = root.findViewById(R.id.userName_Pf);
        profilepic = root.findViewById(R.id.pfpicture);
    }

    private void clickListeners(final View root) {
        goBackButton.setOnClickListener(ev -> {
            Navigation.findNavController(root).navigate((R.id.action_userProfile_to_home2));
        });

        goSettingsBtn.setOnClickListener(ev -> {
            Navigation.findNavController(root).navigate((R.id.action_userProfile_to_settings));
        });
    }
}