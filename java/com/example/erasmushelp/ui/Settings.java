package com.example.erasmushelp.ui;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.erasmushelp.MainActivity;
import com.example.erasmushelp.R;
import com.example.erasmushelp.data.Consts;
import com.example.erasmushelp.data.User;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class Settings extends Fragment implements Consts {

    private ImageButton goBackButton;
    private Button saveChanges, changePFP, savePFP;

    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference database;
    private User user;
    private StorageReference storageReference;
    private StorageTask mUploadTask;

    private EditText et_Name, et_Age, et_birthdayDate;
    private EditText et_homeCountry, et_motherTongue, et_homeUniversity;
    private ImageView pfpView;
    private CardView imageView;

    private MaterialTextView birthday;
    private Date b_date;

    private String name, age, bd, homeCountry, motherTongue, homeUni;
    private Uri pfpUri; // to identify the pfp

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        initializeElements(root);
        clickListeners(root);
        getUser();

        return root;
    }


    private void initializeElements(View root) {
        goBackButton = root.findViewById(R.id.btn_goBack);
        saveChanges = root.findViewById(R.id.saveChanges);

        //initialize Ui elements
        et_Name = root.findViewById(R.id.insertName);
        et_Age = root.findViewById(R.id.Age);
        birthday = root.findViewById(R.id.birthdate);
        et_homeCountry = root.findViewById(R.id.country);
        et_motherTongue = root.findViewById(R.id.motherTongue);
        et_homeUniversity = root.findViewById(R.id.homeSchool);
        changePFP = root.findViewById(R.id.changepfp);
        savePFP = root.findViewById(R.id.savepfp);
        pfpView = root.findViewById(R.id.pfp_setts);
        imageView = root.findViewById(R.id.imageCard);

        //to hide the unnecessary buttons for now
        imageView.setVisibility(View.GONE);
        savePFP.setVisibility(View.GONE);
    }

    private void clickListeners(final View root) {
        goBackButton.setOnClickListener(ev -> Navigation.findNavController(root).navigate(R.id.action_settings_to_userProfile));

        saveChanges.setOnClickListener(ev -> saveItems());

        birthday.setOnClickListener(v -> {
            Calendar mcurrentDate = Calendar.getInstance();
            int mYear = mcurrentDate.get(Calendar.YEAR);
            int mMonth = mcurrentDate.get(Calendar.MONTH);
            int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog mDatePicker;
            mDatePicker = new DatePickerDialog(getContext(), R.style.date_dialog_theme, (datepicker, selectedyear, selectedmonth, selectedday) -> {
                Calendar date = Calendar.getInstance();
                date.set(selectedyear, selectedmonth, selectedday);
                b_date = date.getTime();

                birthday.setText(new StringBuilder().append(selectedday).append("/").append(selectedmonth + 1).append("/").append(selectedyear).toString());
                birthday.setError(null);
            }, mYear, mMonth, mDay);
            mDatePicker.show();

        });

        changePFP.setOnClickListener(ev -> openFileChooser());
        savePFP.setOnClickListener(ev -> saveProfileImage());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_PICK);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == getActivity().RESULT_OK && data != null && data.getData() != null){
            pfpUri = data.getData();

            Glide.with(this).load(pfpUri).error(R.drawable.avatarpfp).fitCenter().into(pfpView);

            imageView.setVisibility(View.VISIBLE);
            savePFP.setVisibility(View.VISIBLE);
        }
    }

    private void getUser() {
        FirebaseUser fbuser = FirebaseAuth.getInstance().getCurrentUser();
        database = FirebaseDatabase.getInstance(DATABASE_PATH).getReference().child(PATH).child(fbuser.getUid());

        database.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    user = snapshot.getValue(User.class);
                    assert user != null;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println("Read failed");
            }
        });
    }

    private String getExtension(Uri pfp) {
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(pfp));
    }

    private void saveProfileImage() {
        storageReference = FirebaseStorage.getInstance().getReference();
        getUser();
        if (pfpUri != null) {
            if (!user.getImage().isEmpty()) {
                StorageReference imageDelRef = FirebaseStorage.getInstance().getReferenceFromUrl(user.getImage());
                imageDelRef.delete();
            }

            //ProgressDialog will eventually be replaced by a ProgressBar as ProgressDialog is deprecated
            ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle(getString(R.string.uploading));
            progressDialog.show();

            String imageID = UUID.randomUUID() + "." + getExtension(pfpUri);

            StorageReference fileRef = storageReference.child(imageID);
            mUploadTask = fileRef.putFile(pfpUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                            // Success, Image uploaded
                            Toast.makeText(getContext(), getString(R.string.uploading_success), Toast.LENGTH_LONG).show();
                            user.setImage(uri.toString());
                            database.setValue(user);

                            progressDialog.dismiss();

                            savePFP.setVisibility(View.GONE);
                            pfpView.setVisibility(View.GONE);
                            imageView.setVisibility(View.GONE);

                        });
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        pfpView.setVisibility(View.GONE);
                        progressDialog.dismiss();
                    })
                    .addOnProgressListener(taskSnapshot -> {
                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                        progressDialog.setMessage(getString(R.string.uploading) + (int) progress + "%");
                    });
        } else {
            Toast.makeText(getContext(), getString(R.string.uploading_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void saveItems() {
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

        name = et_Name.getText().toString();
        age = et_Age.getText().toString().trim();
        bd = birthday.getText().toString().trim();
        homeCountry = et_homeCountry.getText().toString().trim();
        motherTongue = et_motherTongue.getText().toString().trim();
        homeUni = et_homeUniversity.getText().toString().trim();

        if (!name.isEmpty()) {
            //save
            FirebaseDatabase.getInstance(DATABASE_PATH).
                    getReference(PATH).child(firebaseUser.getUid()).child("name").setValue(et_Name.getText().toString());
        }
        if (!age.isEmpty()) {
            //save
            FirebaseDatabase.getInstance(DATABASE_PATH).
                    getReference(PATH).child(firebaseUser.getUid()).child("age").setValue(et_Age.getText().toString());
        }
        if (!bd.isEmpty()) {
            //save
            FirebaseDatabase.getInstance(DATABASE_PATH).
                    getReference(PATH).child(firebaseUser.getUid()).child("birthdate").setValue(birthday.getText().toString());
        }
        if (!homeCountry.isEmpty()) {
            //save
            FirebaseDatabase.getInstance(DATABASE_PATH).
                    getReference(PATH).child(firebaseUser.getUid()).child("homeCountry").setValue(et_homeCountry.getText().toString());
        }
        if (!motherTongue.isEmpty()) {
            //save
            FirebaseDatabase.getInstance(DATABASE_PATH).
                    getReference(PATH).child(firebaseUser.getUid()).child("motherTongue").setValue(et_motherTongue.getText().toString());
        }
        if (!homeUni.isEmpty()) {
            //save
            FirebaseDatabase.getInstance(DATABASE_PATH).
                    getReference(PATH).child(firebaseUser.getUid()).child("homeUniversity").setValue(et_homeUniversity.getText().toString());
        }
    }
}