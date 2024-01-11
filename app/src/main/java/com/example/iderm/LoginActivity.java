package com.example.iderm;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth firebaseAuth;
    private ProgressBar progressBar;
    private ProgressDialog progressDialog;
    String role;
    FirebaseDatabase db;
    DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //Get Firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();
        db=FirebaseDatabase.getInstance();
        reference= db.getReference();

        if (firebaseAuth.getCurrentUser() != null) {
            startActivity(new Intent(LoginActivity.this, IconTextTabsActivity.class));
            finish();

        }

        // set the view now
        setContentView(R.layout.activity_login);

        inputEmail = findViewById(R.id.inputEmail);
        inputPassword = findViewById(R.id.editTextInputPassword);
        progressBar = findViewById(R.id.progressBar);
        progressDialog=new ProgressDialog(this);
        TextView textViewRegister = findViewById(R.id.textViewRegister);
        Button btnLogin = findViewById(R.id.btnLogin);
        TextView textViewForgotPassword = findViewById(R.id.textViewForgotPassword);

        //Get Firebase auth instance
        firebaseAuth = FirebaseAuth.getInstance();

        textViewRegister.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class)));

        textViewForgotPassword.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterUserActivity.class));
        });

        btnLogin.setOnClickListener((View v) -> {
            String email = inputEmail.getText().toString();
            final String password = inputPassword.getText().toString();

            if (TextUtils.isEmpty(email)) {
                Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
                return;
            }

            else if (TextUtils.isEmpty(password)) {
                Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                return;
            }
            else if (password.length() < 6) {
                inputPassword.setError("Minimum password length is 6 characters");
            }else {
                progressDialog.setTitle("Logging in");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.setMessage("Logging you in into the system");
                progressDialog.show();

                //authenticate user
                firebaseAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    String currentUser = firebaseAuth.getCurrentUser().getUid();
                                    reference= FirebaseDatabase.getInstance().getReference("Clients");
                                    reference.child(currentUser).addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            if(snapshot.exists()){
                                                Intent intentlogin = new Intent(LoginActivity.this,IconTextTabsActivity.class);
                                                startActivity(intentlogin);
                                                finish();
                                                progressDialog.dismiss();

                                            }
                                            else {
                                                Toast.makeText(LoginActivity.this, "User Authentication Failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                                progressBar.setVisibility(View.GONE);
                                                firebaseAuth.signOut();
                                                progressDialog.dismiss();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }


                                    });
                                }
                                // there was an error
                                else {
                                    Toast.makeText(LoginActivity.this, "Failed to login, either the login credentials are incorrect or no internet", Toast.LENGTH_SHORT).show();
                                    progressBar.setVisibility(View.GONE);
                                }
                            }
                        });
            }
        });
    }

}
