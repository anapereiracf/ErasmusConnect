package com.example.erasmushelp.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.erasmushelp.R;
import com.example.erasmushelp.data.Consts;

public class Links extends Fragment implements Consts {

    private CardView card_ESN, card_FG;
    private ImageButton returnBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_links, container, false);

        initializeElements(root);
        clickListeners(root);

        return root;
    }

    private void initializeElements(View root){
        card_ESN = root.findViewById(R.id.ESN);
        card_FG = root.findViewById(R.id.Facebook);
        returnBtn = root.findViewById(R.id.returnBtn);
    }

    private void clickListeners(final View root){
        card_ESN.setOnClickListener(ev->{
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse(ESN_LINK));
            startActivity(browserInt);
        });

        card_FG.setOnClickListener(ev->{
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse(FACEBOOK_GROUP));
            startActivity(browserInt);
        });

        returnBtn.setOnClickListener(ev->{
            Navigation.findNavController(root).navigate(R.id.action_links_to_home2);
        });
    }
}