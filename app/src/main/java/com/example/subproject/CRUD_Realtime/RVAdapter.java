package com.example.subproject.CRUD_Realtime;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.subproject.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RVAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Context context;
    ArrayList<CongViec> list = new ArrayList<>();
    public RVAdapter(Context ctx)
    {
        this.context = ctx;
    }
    public RVAdapter(ArrayList<CongViec> list)
    {
        this.list = list;
    }
    public void setItems(ArrayList<CongViec> emp)
    {
        list.addAll(emp);
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_item,parent,false);
        return new CongViecVH(view);
    }
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position)
    {
        CongViec e = null;
        this.onBindViewHolder(holder,position, e);
    }

    public ArrayList<CongViec> getListItem( )
    {
        return list;
    }

    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position, CongViec e)
    {
        CongViecVH vh = (CongViecVH) holder;
        CongViec emp = e == null ? list.get(position) : e;
        vh.txt_name.setText(emp.getTitle());
        vh.txt_position.setText(emp.getContent());
        vh.txt_date.setText(emp.getDate());
        vh.txt_time.setText(emp.getTime());
        vh.txt_option.setOnClickListener(v->
        {
            PopupMenu popupMenu = new PopupMenu(context,vh.txt_option);
            popupMenu.inflate(R.menu.option_menu);
            popupMenu.setOnMenuItemClickListener(item->
            {
                switch (item.getItemId())
                {
                    case R.id.menu_edit:
                        Intent intent=new Intent(context,AddTaskActivity.class);
                        intent.putExtra("EDIT",emp);
                        context.startActivity(intent);
                        break;
                    case R.id.menu_remove:
                        DAOEmployee dao=new DAOEmployee();
                        dao.remove(emp.getKey()).addOnSuccessListener(suc->
                        {
                            Toast.makeText(context, "Remove successfully", Toast.LENGTH_SHORT).show();
                            notifyItemRemoved(position);
                            list.remove(emp);

                        }).addOnFailureListener(er->
                        {
                            Toast.makeText(context, ""+er.getMessage(), Toast.LENGTH_SHORT).show();
                        });
                        break;
                }
                return false;
            });
            popupMenu.show();
        });
    }

    @Override
    public int getItemCount()
    {
        return list.size();
    }


}