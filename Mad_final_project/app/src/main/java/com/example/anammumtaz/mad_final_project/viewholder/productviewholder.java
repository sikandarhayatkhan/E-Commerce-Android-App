package com.example.anammumtaz.mad_final_project.viewholder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.anammumtaz.mad_final_project.Interface.itemclicklistner;
import com.example.anammumtaz.mad_final_project.R;
import com.rey.material.widget.Spinner;

public class productviewholder extends RecyclerView.ViewHolder implements View.OnClickListener

{
    public TextView txtproductname, txtproductdescription, txtproductprice;
    public ImageView imageView;
    public itemclicklistner listner;





    public productviewholder(@NonNull View itemView) {
        super(itemView);

        imageView= (ImageView) itemView.findViewById(R.id.product_image);
        txtproductname= (TextView) itemView.findViewById(R.id.product_name);
        txtproductdescription= (TextView) itemView.findViewById(R.id.product_description);
        txtproductprice= (TextView) itemView.findViewById(R.id.product_price);


    }

    public void setItemClickListner(itemclicklistner listner){
        this.listner= listner;


    }

    @Override
    public void onClick(View view) {
        listner.onClick(view, getAdapterPosition(), false);

    }
}
