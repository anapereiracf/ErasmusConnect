package com.example.erasmushelp.ui.users;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.example.erasmushelp.R;
import com.example.erasmushelp.data.Consts;
import com.example.erasmushelp.data.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterUser extends Fragment implements Consts {
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    //edited fields
    private EditText et_name, et_email, et_pass, et_passConfirm;
    private Button btn_register;
    private ImageButton btn_back;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_register_user, container, false);

        firebaseAuth = FirebaseAuth.getInstance();

        initializeElements(root);

        clickListeners(root);

        return root;
    }

    private void initializeElements(View root) {
        et_name = root.findViewById(R.id.registerName);
        et_email = root.findViewById(R.id.registerEmail);
        et_pass = root.findViewById(R.id.registerPass);
        et_passConfirm = root.findViewById(R.id.registerPassConfirm);

        btn_back=root.findViewById(R.id.btnBackRegister);
        btn_register=root.findViewById(R.id.registerbtn);
    }

    private void clickListeners(final View root) {
        btn_register.setOnClickListener(v -> verifyData());
        btn_back.setOnClickListener(v -> {Navigation.findNavController(getView()).navigate(R.id.action_registerUser_to_logIn);});
    }

    private void verifyData(){
        String name = et_name.getText().toString().trim();
        String email = et_email.getText().toString().trim();
        String password = et_pass.getText().toString().trim();
        String passwordConf = et_passConfirm.getText().toString().trim();
        boolean error = false;

        if(name.isEmpty()){
            et_name.setError(getString(R.string.name_error));
            et_name.requestFocus();
            error = true;
        }

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

        if(!password.matches(passwordConf) || password.length()<=6){
            et_passConfirm.setError(getString(R.string.passDif_error));
            et_passConfirm.requestFocus();
            error = true;
        }

        if(error){
            return;
        }

        registerUser(name,email,password);
    }

    private void registerUser(String name, String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()){
                        firebaseUser = firebaseAuth.getCurrentUser();
                        Toast.makeText(getContext(),getString(R.string.registration_message), Toast.LENGTH_LONG).show();
                        firebaseUser.sendEmailVerification();
                        registerInFirebase(name,email,password,firebaseUser.getUid());
                    }
                    else {
                        Toast.makeText(getContext(),getString(R.string.registration_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                });
    }
    private void registerInFirebase(String name, String email, String password, String userID) {
        Object newuser = new User(name, email, password);

        FirebaseDatabase.getInstance(DATABASE_PATH).getReference(PATH_USERS).child(userID).setValue(newuser).addOnCompleteListener(task -> {
            if(task.isSuccessful()){
                Log.d("result sucesso", userID);
                Toast.makeText(getContext(),getResources().getString(R.string.registration_sucess_mensage), Toast.LENGTH_LONG).show();

                FirebaseAuth.getInstance().signOut();

                Navigation.findNavController(getView()).navigate(R.id.action_registerUser_to_logIn);

            }
            else {
                Log.d("result",task.toString());
                Toast.makeText(getContext(),getResources().getString(R.string.registration_error), Toast.LENGTH_LONG).show();

            }
        });
    }
}