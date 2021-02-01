package com.example.registerbook;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //view for the register button
    private TextView register;
    //forgot password
    private TextView forgotPassword;
    //private variable for email and password and the login
    private EditText editTextEmail, editTextPassword;
    private Button signIn;
    //for firebase object
    private FirebaseAuth mAuth;
    //for progressBar
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialize the register button
        register = (TextView)findViewById(R.id.register);
        //set onClickListener to the regButton
        register.setOnClickListener(this);

        //initialize the signIn button
        signIn = (Button) findViewById(R.id.signIn);
        //and setOnclickListener on the button
        signIn.setOnClickListener(this);

        //also initialize for the other text field
        editTextEmail = (EditText)findViewById(R.id.email);
        editTextPassword = (EditText)findViewById(R.id.password);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
        mAuth = FirebaseAuth.getInstance();

        //initialize the textview for forgot password
        forgotPassword = (TextView) findViewById(R.id.forgetPassw);
        forgotPassword.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        //create a switch and cases for all onClickListener
        switch (view.getId()){
            case R.id.register:
                //intent to take us to register user page
                startActivity(new Intent(this, RegisterUser.class));
                break;
                //when the user click signIn, start new activity
            case R.id.signIn:
                //method for the user login
                userLogin();
                break;
                //forgot password
            case R.id.forgetPassw:
                //redirect the user to forgot password activity
                startActivity(new Intent(this,ForgotPasswordActivity.class));
                break;
        }
    }

    private void userLogin() {
        //get the user credentials and convert to string
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        //validations to check the user credentials
        if (email.isEmpty()) {
            editTextEmail.setError("Email is required!");
            editTextEmail.requestFocus();
            return;
        }
        //check if the email enter by user is valid
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        //check if the password enter by user is valid
        if (password.isEmpty()) {
            editTextPassword.setError("Password is required!");
            editTextPassword.requestFocus();
            return;
        }
        //check if password has minimum length
        if (password.length()<6){
            editTextPassword.setError("Minimum password length is 6 characters");
            editTextPassword.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        //firebase object to sign in
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                //if user has been logged in
                if (task.isSuccessful()){
                    //check if the user email and password has been verified or not
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){
                        //redirect to user profile
                        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
                    }
                    //if user not verified, send a verification mail
                    else {
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check your mail to verify your account",
                                Toast.LENGTH_LONG).show();
                    }

                }
                else {
                    Toast.makeText(MainActivity.this, "Failed to login! " +
                            "Please check your credentials", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}