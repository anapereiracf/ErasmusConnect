package com.example.erasmushelp.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.example.erasmushelp.R;
import com.example.erasmushelp.data.InternalStorage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class LogIn extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //edited fields
    private EditText et_email, et_pass;
    private Button btn_registerHere, btn_login;
    private CheckBox cb_Remember;
    String[] permissions = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.MANAGE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_log_in, container, false);
        checkPermissions();

        initializeElements(root);

        clickListeners(root);

        if (getActivity().getIntent().hasExtra("LoginAgain") && savedInstanceState==null){
            savedInstanceState = getActivity().getIntent().getExtras().getBundle("LoginAgain");

            String user_email = savedInstanceState.getString("UserEmail");
            String user_password = savedInstanceState.getString("UserPassoword");

            if(user_email != null && user_password != null){
                if(!user_email.isEmpty() && !user_password.isEmpty()){
                    login(user_email,user_password);
                    savedInstanceState.putString("UserEmail",null);
                    savedInstanceState.putString("UserPassoword",null);
                }
            }

        }
        getRemeberData();
        return root;
    }
    private void initializeElements(View root) {
        btn_registerHere=root.findViewById(R.id.loginRegister);
        btn_login = root.findViewById(R.id.loginbtn);
        et_email = root.findViewById(R.id.loginEmail);
        et_pass= root.findViewById(R.id.loginPass);
        cb_Remember = root.findViewById(R.id.checkBoxLogin);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    private void clickListeners(final View root) {
        btn_registerHere.setOnClickListener(v -> {
            Navigation.findNavController(root).navigate(R.id.action_logIn_to_registerUser);
        });

        btn_login.setOnClickListener(v -> verifyData());
    }
    private void verifyData(){
        String email = et_email.getText().toString().trim();
        String password = et_pass.getText().toString().trim();
        boolean error = false;

        if(email.isEmpty()){
            et_email.setError(getString(R.string.email_error));
            et_email.requestFocus();
            error = true;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            et_email.setError(getString(R.string.email_valid_error));
            et_email.requestFocus();
            error = true;
        }

        if(password.isEmpty()){
            et_pass.setError(getString(R.string.passEmpty_error));
            et_pass.requestFocus();
            error = true;
        }

        if(error){
            return;
        }

        login(email,password);
    }

    private void login(String email, String password) {

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                btn_login.setEnabled(false);

                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                if(cb_Remember.isChecked()){
                    saveRemeberData(email,password);
                }
                else{
                    saveRemeberData("","");
                }

                if(user.isEmailVerified()){
                    Toast.makeText(getContext(),"Login Successful",Toast.LENGTH_LONG).show();
                    Navigation.findNavController(getView()).navigate(R.id.action_logIn_to_home2);
                }
                else{
                    user.sendEmailVerification();
                    Toast.makeText(getContext(),getString(R.string.login_verify_error), Toast.LENGTH_LONG).show();
                    btn_login.setEnabled(true);
                }
            }
            else {
                Toast.makeText(getContext(),getString(R.string.login_verify_error),Toast.LENGTH_LONG).show();
                btn_login.setEnabled(true);
            }
        });
    }
    private void saveRemeberData(String email, String password){
        try {
            InternalStorage.writeObject(getContext(), "Email", email);
            InternalStorage.writeObject(getContext(), "Password", password);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void getRemeberData() {
        try {
            String email = (String) InternalStorage.readObject(getContext(), "Email");
            String password = (String) InternalStorage.readObject(getContext(), "Password");

            if(!email.isEmpty() && !password.isEmpty()){
                et_email.setText(email);
                et_pass.setText(password);

                cb_Remember.setChecked(true);

                /*if(autoLogin){
                    bt_Login.performClick();
                }*/
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();

        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getContext(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(getActivity(), listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), 124);
            return false;
        }
        return true;
    }





}