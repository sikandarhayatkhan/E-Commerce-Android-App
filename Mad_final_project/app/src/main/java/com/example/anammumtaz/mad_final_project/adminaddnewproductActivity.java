package com.example.anammumtaz.mad_final_project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class adminaddnewproductActivity extends AppCompatActivity {
    private  String categoryname, descripton, price, pname, savecurrentdate, savecurrenttime;
    private Button addnewproductbtn;
    private ImageView inputproductimage;
    private EditText inputproductname, inputproductdescription, inputproductprice;
    private  static final  int gallerypick= 1;
    private Uri imageuri;
    private  String productrandomkey, downloadimageurl;
    private  StorageReference productimageref;
    private DatabaseReference productsref;
    private ProgressDialog loadingbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminaddnewproduct);
        categoryname= getIntent().getExtras().get("category").toString();
        productimageref = FirebaseStorage.getInstance().getReference().child("product images");
        productsref= FirebaseDatabase.getInstance().getReference().child("products");

        addnewproductbtn= (Button) findViewById(R.id.add_new_product);
        inputproductimage=(ImageView) findViewById(R.id.select_product_image);
        inputproductname=(EditText) findViewById(R.id.product_name);
        inputproductdescription= (EditText) findViewById(R.id.product_description);
        inputproductprice= (EditText) findViewById(R.id.product_price);
        loadingbar= new ProgressDialog(this);

        inputproductimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opengallery();
                
            }
        });
        addnewproductbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateproductdata();
            }
        });

        }

    private void opengallery() {
        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        //startActivityForResult(intent, 2);
        //Intent galleryintent= new Intent();
       // galleryintent.setAction(Intent.ACTION_GET_CONTENT);
        galleryintent.setType("image/*");
        startActivityForResult(galleryintent,gallerypick);
        //startActivityForResult(Intent.createChooser(galleryintent, "Select Picture"), gallerypick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==gallerypick && resultCode==RESULT_OK && data!= null) {
           imageuri = data.getData();
           Toast.makeText(getApplicationContext(),"OK Image Recieved",Toast.LENGTH_SHORT).show();
           inputproductimage.setImageURI(imageuri);


        }
    }

   private void validateproductdata(){
        descripton= inputproductdescription.getText().toString();
       price= inputproductprice.getText().toString();
       pname= inputproductname.getText().toString();
       if(imageuri== null){
           Toast.makeText(this, "product image is mandatory", Toast.LENGTH_SHORT).show();
           }
           else if(TextUtils.isEmpty(descripton)){
           Toast.makeText(this, "please write product description", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(price)){
           Toast.makeText(this, "please write product price", Toast.LENGTH_SHORT).show();
       }
       else if(TextUtils.isEmpty(pname)){
           Toast.makeText(this, "please write product name", Toast.LENGTH_SHORT).show();
       }
       else{
           storeproductinformation();
       }


   }

    private void storeproductinformation() {
        loadingbar.setTitle("Adding New Product");
        loadingbar.setMessage("please wait while we are adding the the new product");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        Calendar calendar= Calendar.getInstance();
        SimpleDateFormat currentdate= new SimpleDateFormat("MMM dd, yyyy");
        savecurrentdate = currentdate.format(calendar.getTime());


        SimpleDateFormat currenttime= new SimpleDateFormat("HH:mm:ss a");
        savecurrenttime = currenttime.format(calendar.getTime());
        productrandomkey= savecurrentdate  + savecurrenttime;
        final StorageReference filepath= productimageref.child(imageuri.getLastPathSegment() + productrandomkey + ".jpg");
        final UploadTask uploadTask= filepath.putFile(imageuri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message= e.toString();
                Toast.makeText(adminaddnewproductActivity.this, "Error:"+ message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();

            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(adminaddnewproductActivity.this, "image uploaded successfully....", Toast.LENGTH_SHORT).show();
                Task<Uri> urltask= uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful()){

                            throw task.getException();

                        }
                        downloadimageurl= filepath.getDownloadUrl().toString();
                        return filepath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful()){
                            downloadimageurl= task.getResult().toString();
                            Toast.makeText(adminaddnewproductActivity.this, "product image save to dataabase successfully", Toast.LENGTH_SHORT).show();
                            saveproductinfotodatabase();
                        }

                    }
                });
            }
        });


    }

    private void saveproductinfotodatabase() {
        HashMap<String , Object> productmap= new HashMap<>();
        productmap.put("pid", productrandomkey);
        productmap.put("date", savecurrentdate);
        productmap.put("time", savecurrenttime);
        productmap.put("description", descripton);
        productmap.put("image", downloadimageurl);
        productmap.put("category", categoryname);
        productmap.put("price", price);
        productmap.put("pname", pname);
        productsref.child(productrandomkey).updateChildren(productmap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        if(task.isSuccessful()){
                            Intent intent = new Intent(adminaddnewproductActivity.this, admincategoryActivity.class);
                            startActivity(intent);
                            loadingbar.dismiss();
                            Toast.makeText(adminaddnewproductActivity.this, "product is added successfully", Toast.LENGTH_SHORT).show();

                        }
                        else {
                            loadingbar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(adminaddnewproductActivity.this, "Error"+message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }


}
