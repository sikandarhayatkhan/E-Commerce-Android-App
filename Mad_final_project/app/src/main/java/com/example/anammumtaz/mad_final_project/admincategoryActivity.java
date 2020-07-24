package com.example.anammumtaz.mad_final_project;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class admincategoryActivity extends AppCompatActivity {
    private ImageView mobilephone, mobileaccessories, storagedevices;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admincategory);
        mobilephone=(ImageView) findViewById(R.id.mobile_phones);
        mobileaccessories=(ImageView) findViewById(R.id.mobile_accessories);

        storagedevices=(ImageView) findViewById(R.id.storage_devices);


        mobilephone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(admincategoryActivity.this, adminaddnewproductActivity.class);
                intent.putExtra("category", "mobilephone");
                startActivity(intent);
            }
        });
        mobileaccessories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(admincategoryActivity.this, adminaddnewproductActivity.class);
                intent.putExtra("category", "mobileaccessories");
                startActivity(intent);

            }
        });
        storagedevices.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(admincategoryActivity.this, adminaddnewproductActivity.class);
                intent.putExtra("category", "storagedevices");
                startActivity(intent);

            }
        });
    }
}
