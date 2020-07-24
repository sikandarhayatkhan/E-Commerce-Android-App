package com.example.anammumtaz.mad_final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.anammumtaz.mad_final_project.model.users;
import com.example.anammumtaz.mad_final_project.prevalent.prevalent;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class loginActivity extends AppCompatActivity {
    private EditText inputphonenumners, inputpasswords;
    private Button loginbutton;
    private ProgressDialog loadingbar;

    private TextView adminlink, notadminlink;



   private  String parentdbname="users";
////


    protected SQLiteHelper mSqLiteHelper = null;
    private int mUserId = -1;
    //////



    ////

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        Button retrieveRecord =(Button) findViewById(R.id.login_btn);
        retrieveRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Cursor cursor = mSqLiteHelper.query(inputphonenumners.getText().toString());
                    if(cursor.getCount() > 0) {
                        mUserId = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.username));
//						mUserId = cursor.getInt(cursor.getColumnIndex(SQLiteHelper.COLUMN_AGE));
                        String result = String.format("%s, %s, %s", cursor.getString(cursor.getColumnIndex(String.valueOf(SQLiteHelper.phone_number))), cursor.getString(cursor.getColumnIndex(SQLiteHelper.password)));
                        inputpasswords.setText(result);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Record Found", Toast.LENGTH_LONG).show();
                        inputpasswords.setText("");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });







        /////


        loginbutton= (Button) findViewById(R.id.login_btn);
        inputpasswords= (EditText) findViewById(R.id.login_password_input);
        inputphonenumners= (EditText) findViewById(R.id.login_phone_number_input);
        adminlink= (TextView) findViewById(R.id.admin_panel_link);
        notadminlink= (TextView) findViewById(R.id.not_admin_panel_link);

        loadingbar= new ProgressDialog(this);
        loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginusers();
            }

           
        });

adminlink.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        loginbutton.setText("Login Admin");
        adminlink.setVisibility(v.INVISIBLE);
        notadminlink.setVisibility(v.VISIBLE);
        parentdbname= "admin";
    }
});
notadminlink.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        loginbutton.setText("Login");
        adminlink.setVisibility(v.VISIBLE);
        notadminlink.setVisibility(v.INVISIBLE);
        parentdbname= "users";

    }
});
    }

    private void loginusers() {
        String phone= inputphonenumners.getText().toString();
        String password= inputpasswords.getText().toString();
        if(TextUtils.isEmpty(phone)){

            Toast.makeText(this, "please write your phone number", Toast.LENGTH_SHORT).show();

        }
        else if(TextUtils.isEmpty(password)){

            Toast.makeText(this, "please write your password", Toast.LENGTH_SHORT).show();

        }
        else {
            loadingbar.setTitle("login account");
            loadingbar.setMessage("please wait while we are checking the credential");
            loadingbar.setCanceledOnTouchOutside(false);
            loadingbar.show();
            AllowAccessToAccount(phone, password);

        }
    }

    private void AllowAccessToAccount(final String phone, final String password) {
        final DatabaseReference RootRef;
        RootRef= FirebaseDatabase.getInstance().getReference();
        RootRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(parentdbname).child(phone).exists()){
                    users userdata= dataSnapshot.child(parentdbname).child(phone).getValue(users.class);
                    if(userdata.getPhone().equals(phone)){
                        if(userdata.getPassword().equals(password)){
                           if(parentdbname.equals("admin")){
                               Toast.makeText(loginActivity.this, "welcome admin! you are Logged In Successfully...", Toast.LENGTH_SHORT).show();
                               loadingbar.dismiss();
                               Intent intent = new Intent(loginActivity.this, admincategoryActivity.class);
                               startActivity(intent);
                           }
                           else if(parentdbname.equals("users")){
                               Toast.makeText(loginActivity.this, "Logged In Successfully...", Toast.LENGTH_SHORT).show();
                               loadingbar.dismiss();
                               Intent intent = new Intent(loginActivity.this, homeActivity.class);
                               prevalent.currentonlineuser= userdata;
                               startActivity(intent);
                           }


                        }
                    }

                }
                else{
                    Toast.makeText(loginActivity.this, "Account with this "+phone+"number do not exists", Toast.LENGTH_SHORT).show();
                    loadingbar.dismiss();
                    Toast.makeText(loginActivity.this, "you need to create account first then login", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
