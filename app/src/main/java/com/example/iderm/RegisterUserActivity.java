package com.example.iderm;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class RegisterUserActivity extends AppCompatActivity implements View.OnClickListener {
    EditText editTextPassword;
    EditText editTextUsername;
    EditText editTextEmail;
    EditText editTextConfirmPassword;
    EditText editTextPhoneNumber;
    Button btnRegister,btnLogin;
    FirebaseAuth mAuth;
    Boolean valid = true;
    ProgressDialog mDialog;
    String password, email, name, phoneNumber;
    FirebaseDatabase db;
    DatabaseReference reference;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_user);
        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPhoneNumber = findViewById(R.id.editTextPhoneNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextRetypePassword);
        btnRegister = findViewById(R.id.buttonRegister);
        btnLogin=findViewById(R.id.btnLogin);
        // for authentication using FirebaseAuth.
        mAuth = FirebaseAuth.getInstance();
        btnRegister.setOnClickListener(this);
        mDialog = new ProgressDialog(this);
        db = FirebaseDatabase.getInstance();
        reference = db.getReference();
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterUserActivity.this, LoginActivity.class));
            }
        });


    }

    @Override
    public void onClick(View v) {

        if (v == btnRegister) {
            UserRegister();
        }
    }

    private void UserRegister() {
        name = editTextUsername.getText().toString().trim();
        email = editTextEmail.getText().toString().trim();
        password = editTextPassword.getText().toString().trim();
        phoneNumber = editTextPhoneNumber.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextUsername.setError("invalid_name");
        } else if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("invalid_email");
        } else if (TextUtils.isEmpty(phoneNumber)) {
            editTextPhoneNumber.setError("invalid_phoneNumber");
        }
        else if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("invalid_password");
        } else if (password.length() < 6) {
            editTextPassword.setError("invalid_password");
        } else if (!(editTextPassword.getText().toString().trim()).equals(editTextConfirmPassword.getText().toString().trim())) {
            editTextConfirmPassword.setError("password_mismatch");
        } else
        {

            mDialog.setMessage("Please wait...");
            mDialog.setCanceledOnTouchOutside(false);
            mDialog.show();
            mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        sendEmailVerification();
                        HashMap<String, Object> hashMap = new HashMap<>();
                        String id = Objects.requireNonNull(mAuth.getCurrentUser()).getUid();
                        hashMap.put("Name", name);
                        hashMap.put("Email", email);
                        hashMap.put("UserName", name);
                        hashMap.put("Phone Number", phoneNumber);
                        reference.child("Clients").child(id).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(RegisterUserActivity.this, "Account created successfully", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(RegisterUserActivity.this, "Failed to create account, an error has occurred", Toast.LENGTH_SHORT).show();
                            }
                        });
                        mDialog.dismiss();
                        mAuth.signOut();

                    } else {
                        Toast.makeText(RegisterUserActivity.this, "Failed to create account, an error has occurred", Toast.LENGTH_SHORT).show();
                        mDialog.dismiss();
                        mAuth.signOut();
                    }
                }
            });
        }
    }


    //Email verification code using FirebaseUser object and using isSucccessful()function.
    private void sendEmailVerification() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {

                        Toast.makeText(RegisterUserActivity.this, "A confirmation email has been sent into your registered email", Toast.LENGTH_SHORT).show();

                    }
                }
            });
        }
    }

}
