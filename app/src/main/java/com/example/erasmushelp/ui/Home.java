package com.example.erasmushelp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class Home extends Fragment implements Consts {

    private CardView card_housing, card_extRes, card_ptnsInt, card_language, card_profile;
    private Button goCF_Btn;
    private DatabaseReference database;
    private TextView usernameView;
    private ImageView pfpicture;
    private User user;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

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
                    user = snapshot.getValue(User.class);
                    assert user != null;
                    usernameView.setText(new StringBuilder().append("User ").append(user.getName()).toString());
                    getUserPfp();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read failed");
            }
        });
    }

    private void getUserPfp() {
        if(!user.getImage().isEmpty()){
            Glide.with(this).load(user.getImage()).placeholder(R.drawable.avatarpfp).fitCenter().into(pfpicture);
        }
    }


    private void initializeElements(View root) {
        card_extRes = root.findViewById(R.id.ExtRes);
        card_profile = root.findViewById(R.id.pfpCardView);
        card_housing = root.findViewById(R.id.housing);
        card_language = root.findViewById(R.id.language);
        card_ptnsInt = root.findViewById(R.id.PntInterest);
        goCF_Btn = root.findViewById(R.id.button);
        usernameView = root.findViewById(R.id.userName_home);
        pfpicture = root.findViewById(R.id.pfpimage);
    }

    private void clickListeners(final View root) {
        card_ptnsInt.setOnClickListener(ev -> Navigation.findNavController(root).navigate(R.id.action_home2_to_pointsOfInterest));

        card_extRes.setOnClickListener(ev -> Navigation.findNavController(root).navigate(R.id.action_home2_to_links));

        card_profile.setOnClickListener(ev -> Navigation.findNavController(root).navigate(R.id.action_home2_to_userProfile));

        card_housing.setOnClickListener(ev -> Navigation.findNavController(root).navigate(R.id.action_home2_to_housing));

        card_language.setOnClickListener(ev -> {
            Navigation.findNavController(root).navigate(R.id.action_home2_to_learnLanguage);
        });

        goCF_Btn.setOnClickListener(ev -> {
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.unive.it/data/accesso"));
            startActivity(browserInt);
        });
    }

}