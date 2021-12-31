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

public class Housing extends Fragment implements Consts {

    private CardView erasmusPlayCard, CFHousingCard, erasmusuCard, rentolaCard, housingAnyCard, uniPlacesCard;
    private ImageButton homeButton;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_housing, container, false);

        initializeElements(root);
        clickListeners(root);

        return root;
    }

    private void initializeElements(View root){
        erasmusPlayCard = root.findViewById(R.id.erasmusplaycard);
        erasmusuCard = root.findViewById(R.id.erasmusucard);
        rentolaCard = root.findViewById(R.id.rentolacard);
        housingAnyCard = root.findViewById(R.id.housinganycard);
        uniPlacesCard = root.findViewById(R.id.uniplacescard);
        CFHousingCard = root.findViewById(R.id.cafoscaricard);
        homeButton = root.findViewById(R.id.homebutton2);
    }

    private void clickListeners(final View root){
        homeButton.setOnClickListener(ev -> Navigation.findNavController(root).navigate(R.id.action_housing_to_home2));

        erasmusPlayCard.setOnClickListener(ev->{
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse(ERASMUS_PLAY));
            startActivity(browserInt);
        });

        erasmusuCard.setOnClickListener(ev->{
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse(ERASMUSU));
            startActivity(browserInt);
        });

        rentolaCard.setOnClickListener(ev->{
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse(RENTOLA));
            startActivity(browserInt);
        });

        housingAnyCard.setOnClickListener(ev->{
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse(HOUSING_ANYWHERE));
            startActivity(browserInt);
        });

        uniPlacesCard.setOnClickListener(ev->{
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse(UNIPLACES));
            startActivity(browserInt);
        });

        CFHousingCard.setOnClickListener(ev->{
            Intent browserInt = new Intent(Intent.ACTION_VIEW, Uri.parse(CF_HOUSING));
            startActivity(browserInt);
        });
    }
}