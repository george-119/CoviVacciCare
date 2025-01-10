package com.covi.vaccicare;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class VaccinedetailsVH extends RecyclerView.ViewHolder
{
    public TextView txt_name,txt_status,txt_option,txt_class;
    CardView txt_card;
    public VaccinedetailsVH(@NonNull View itemView)
    {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_status = itemView.findViewById(R.id.txt_status);
        txt_option = itemView.findViewById(R.id.txt_option);
        txt_card = itemView.findViewById(R.id.list_object);
        txt_class = itemView.findViewById(R.id.txt_clsdiv);


    }
}
