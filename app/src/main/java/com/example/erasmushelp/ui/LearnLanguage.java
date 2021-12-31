package com.example.erasmushelp.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.erasmushelp.R;

public class LearnLanguage extends Fragment {

    private ImageButton btn_back;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_learn_language, container, false);

        initializeElements(root);
        clickListeners(root);
        return root;
    }

    private void initializeElements(View root) {
        btn_back = root.findViewById(R.id.Home_button);
    }

    private void clickListeners(final View root) {
        btn_back.setOnClickListener(ev -> Navigation.findNavController(root).navigate(R.id.action_learnLanguage_to_home2));
    }
}