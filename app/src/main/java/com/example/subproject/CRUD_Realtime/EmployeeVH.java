package com.example.subproject.CRUD_Realtime;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subproject.R;

public class EmployeeVH extends RecyclerView.ViewHolder
{
    public TextView txt_name,txt_position,txt_option,txt_date,txt_time;
    public EmployeeVH(@NonNull View itemView)
    {
        super(itemView);
        txt_name = itemView.findViewById(R.id.txt_name);
        txt_position = itemView.findViewById(R.id.txt_position);
        txt_date=itemView.findViewById(R.id.txt_date);
        txt_time=itemView.findViewById(R.id.txt_time);
        txt_option = itemView.findViewById(R.id.txt_option);
    }
}