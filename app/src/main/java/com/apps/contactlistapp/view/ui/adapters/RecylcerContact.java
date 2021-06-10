package com.apps.contactlistapp.view.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.apps.contactlistapp.databinding.ContactAdapterLayoutBinding;
import com.apps.contactlistapp.model.ContactModel;
import com.apps.contactlistapp.view.ui.activities.MainActivity;

import java.util.ArrayList;

public class RecylcerContact extends RecyclerView.Adapter<RecylcerContact.ViewHolder> {

    Context mContext;
    MainActivity mainActivity;
    ArrayList<ContactModel> modelArrayList;
    private LayoutInflater inflater;

    public RecylcerContact(Context mainActivity, ArrayList<ContactModel> contactModelArrayList, MainActivity mainActivity1) {
        mainActivity = mainActivity1;
        mContext = mainActivity;
        modelArrayList = contactModelArrayList;
        this.inflater = LayoutInflater.from(mainActivity);
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContactAdapterLayoutBinding view = ContactAdapterLayoutBinding.inflate(inflater, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.adapterBinding.conName.setText(modelArrayList.get(position).getC_name());
        holder.adapterBinding.cNum.setText(modelArrayList.get(position).getC_number());
        holder.adapterBinding.emailTxt.setText(modelArrayList.get(position).getC_email());
        holder.adapterBinding.contactImg.setImageBitmap(StringToBitMap(modelArrayList.get(position).getC_image()));
        //holder.adapterBinding.conName.setText(modelArrayList.get(position).getC_name());


        holder.adapterBinding.callLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callFunction(position);
            }
        });
        holder.adapterBinding.msgLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msgFunction(position);
            }
        });

        holder.adapterBinding.editLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editFunction(position);
            }
        });

        holder.adapterBinding.deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deletFunction(position);
            }
        });

    }

    private void editFunction(int position) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.editFunction(position);
        }
    }

    private void msgFunction(int position) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.msgFunction(position);
        }
    }

    private void callFunction(int position) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.callFunction(position);
        }
    }

    private void deletFunction(int position) {
        if (mContext == null)
            return;
        if (mContext instanceof MainActivity) {
            MainActivity mainActivity = (MainActivity) mContext;
            mainActivity.deletFunction(position);
        }
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private ContactAdapterLayoutBinding adapterBinding;

        public ViewHolder(@NonNull ContactAdapterLayoutBinding adapterBinding) {
            super(adapterBinding.getRoot());
            this.adapterBinding = adapterBinding;
        }
    }

    public Bitmap StringToBitMap(String encodedString)
    {
        try
        {
            byte [] encodeByte= Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        }
        catch(Exception e)
        {
            e.getMessage();
            return null;
        }
    }

}
