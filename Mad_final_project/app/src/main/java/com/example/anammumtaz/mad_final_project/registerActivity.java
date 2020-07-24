package com.example.anammumtaz.mad_final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
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

import java.util.HashMap;

public class registerActivity extends AppCompatActivity {

   ////
   // protected SQLiteHelper mSqLiteHelper = null;
   // private int mUserId = -1;
  //////
    private Button createaccountbuutton;
    private EditText inputname, inputphonenumber, inputpassword;
    private ProgressDialog loadingbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);




        //////
       /// final EditText nameEdit = (EditText) findViewById(R.id.register_username_input);
        ///final EditText phone = (EditText) findViewById(R.id.register_phone_number_input);
        ///   final EditText password = (EditText) findViewById(R.id.register_password_input);
        ///   createaccountbuutton= (Button) findViewById(R.id.register_btn);
        ///   mSqLiteHelper = SQLiteHelper.getInstance(getApplicationContext());


       /* createaccountbuutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = nameEdit.getText().toString();
                int phone_number =Integer.parseInt( phone.getText().toString());
                String passwords =  password.getText().toString();
                long result = mSqLiteHelper.insert(name, phone_number, passwords);
                if(result <= 0) {
                    Toast.makeText(getApplicationContext(), "Insert Failed", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "Successfully registered", Toast.LENGTH_LONG).show();
                }
            }
        });*/





        /////////








        createaccountbuutton= (Button) findViewById(R.id.register_btn);
        inputname= (EditText) findViewById(R.id.register_username_input);
        inputphonenumber=(EditText) findViewById(R.id.register_phone_number_input);
        inputpassword=(EditText) findViewById(R.id.register_password_input);
        loadingbar= new ProgressDialog(this);

        createaccountbuutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createaccount();
            }
        });

    }
    private  void createaccount(){
        String name= inputname.getText().toString();
        String phone= inputphonenumber.getText().toString();
        String password= inputpassword.getText().toString();

        if(TextUtils.isEmpty(name)){
            Toast.makeText(this, "please write your name..", Toast.LENGTH_SHORT).show();
        }
       else if(TextUtils.isEmpty(phone)){
            Toast.makeText(this, "please write your phone..", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(password)){
            Toast.makeText(this, "please write your password..", Toast.LENGTH_SHORT).show();
        }
        else {
            loadingbar.setTitle("create account");
            loadingbar.setMessage("please wait while we are checking the credential");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            validatephonenumber(name, phone, password);

        }
        }

    private void validatephonenumber(final String name, final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(!(dataSnapshot.child("users").child(phone).exists())){
                    HashMap<String, Object> userdatamap= new HashMap<>();
                    userdatamap.put("phone", phone);
                    userdatamap.put("password", password);
                    userdatamap.put("name", name);
                    RootRef.child("users").child(phone).updateChildren(userdatamap).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(registerActivity.this, "congratulation! your account has been created", Toast.LENGTH_SHORT).show();
                                loadingbar.dismiss();
                                Intent intent = new Intent(registerActivity.this, loginActivity.class);
                                startActivity(intent);

                            }
                            else {
                                loadingbar.dismiss();
                                Toast.makeText(registerActivity.this, "Network error! please try again later", Toast.LENGTH_SHORT).show();

                            }

                        }
                    });

                    
                }
                else{
                    Toast.makeText(registerActivity.this, "This "+phone+"already exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(registerActivity.this, "please try with another phomne number", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(registerActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

