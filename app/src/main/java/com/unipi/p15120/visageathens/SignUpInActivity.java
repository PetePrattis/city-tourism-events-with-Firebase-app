package com.unipi.p15120.visageathens;

import android.app.ActivityOptions;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpInActivity extends AppCompatActivity {
    //layout things
    Button signupb,signinb,visitor;
    EditText username,password;

    //database things
    FirebaseDatabase firedb;
    DatabaseReference dbRef;

    //activity intent
    Intent main_ui;

    //object from other classes
    private Users user;

    //variables
    static String username2, password2, where = "nowhere";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_in);

        signupb = findViewById(R.id.signup);
        signinb = findViewById(R.id.signin);
        visitor = findViewById(R.id.visitor);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);


        // user = new Users();
        firedb = FirebaseDatabase.getInstance();
        dbRef = firedb.getReference();


        //extra doesnt have a value if i didnt come from signout button in ScrollingActivity
        if (getIntent().hasExtra("where")) {
            where = getIntent().getStringExtra("where"); //get Value from ScrollingActivity
        }



        signupb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //when signup clicked

                user = new Users();
                username2 = username.getText().toString();
                password2 = password.getText().toString();

                if (!username2.equals("") && !password2.equals("")) // if something was prompted into input
                // then add user
                {
                    //check if user exist
                    dbRef.child("Users/" + username2).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            //user = dataSnapshot.getValue(Users.class);
                            if (!dataSnapshot.exists()) //if user doesnt exists
                            {
                                writeNewUser(username2, password2);
                                main_ui = new Intent(getApplicationContext(), MainActivity.class);
                                main_ui.putExtra("where", "signup");
                                main_ui.putExtra("username", username2);
                                startActivity(main_ui);
                            } else { //if user exists DOULEVEI

                                makeToast( "User already exists. Please enter another username...");

                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

                } else
                    makeToast("Please fill out all fields");

            }
        });

        signinb.setOnClickListener(new View.OnClickListener() { //when sign in button clicked
            @Override
            public void onClick(View v) {
                //final String username_test = username.getText().toString();
                //final String password_test = password.getText().toString();
                if (!username.getText().toString().equals("") && !password.getText().toString().equals(""))
                    loginUser(username.getText().toString(),password.getText().toString());
                else
                    makeToast("Please fill out all fields");
            }

        });

        visitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                main_ui = new Intent(getApplicationContext(), MainActivity.class);
                main_ui.putExtra("where", "visitor");
                main_ui.putExtra("username", username.getText().toString());
                startActivity(main_ui);
            }
        });
    }
    @Override
    public void onBackPressed(){
        if (!where.equals("signout")) //go back only if you didnt came from signout button action,otherwise disable this button in order to prevent show profile data of someone
            //previously logged in
            super.onBackPressed();
    }


    private void makeToast(String text)
    {
        Toast.makeText(SignUpInActivity.this, text, Toast.LENGTH_SHORT).show();
    }

    private void loginUser (final String username, final String password) //final variables 'cause we need those fields not to be changed
    {


        dbRef.child("Users/" + username).addValueEventListener(new ValueEventListener() { //for entry with if = username do the following checks
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                user = dataSnapshot.getValue(Users.class);
                if (dataSnapshot.exists()) {
                    if (password.equals(user.getPassword())) {
                        makeToast("Login successful!");
                        main_ui = new Intent(getApplicationContext(), MainActivity.class);
                        main_ui.putExtra("where","signin");
                        main_ui.putExtra("username", username);
                        startActivity(main_ui);
                        //go to scrolling activity
                    } else {
                        makeToast( "Wrong password, please try again!");

                    }
                }
                else
                    makeToast( "User doesn't exists...");            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void writeNewUser(String username, String password)
    {
        user.setUsername(username);
        user.setPassword(password);

        dbRef.child("Users/"+user.getUsername()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() { //id will be its username
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (!task.isSuccessful())
                    makeToast("Sign Up Failed, please try again!");

            }
        });
    }



}
