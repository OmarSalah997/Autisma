package com.example.autisma;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
class InstAdapter implements ListAdapter {
    ArrayList<InstData> arrayList;
    Context context;
    public InstAdapter(Context context, ArrayList<InstData> arrayList) {
        this.arrayList=arrayList;
        this.context=context;
    }
    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }
    @Override
    public boolean isEnabled(int position) {
        return true;
    }
    @Override
    public void registerDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }
    @Override
    public int getCount() {
        return arrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return position;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public boolean hasStableIds() {
        return false;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final InstData instData = arrayList.get(position);
        if(convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(context);
            convertView = layoutInflater.inflate(R.layout.single_instituion, null);
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    TextView tittle =v.findViewById(R.id.single_row);
                    ImageView imag = v.findViewById(R.id.icon);
                    String backgroundImageName = String.valueOf(imag.getTag());
                    Intent intent = new Intent( context,Profile.class);
                    intent.putExtra("inst_name", tittle.getText().toString());
                    intent.putExtra("inst_img",  backgroundImageName);
                    intent.putExtra("inst_desc",instData.Description);
                    intent.putExtra("inst_address",instData.loc);
                    intent.putExtra("inst_number",instData.mobileNumber);
                    intent.putExtra("inst_fbPage",instData.fbPage);
                    intent.putExtra("inst_webpage",instData.webPage);
                    context.startActivity(intent);
                }
            });
            TextView tittle = convertView.findViewById(R.id.single_row);
            ImageView imag = convertView.findViewById(R.id.icon);
            tittle.setText(instData.InstName);
            String imgname=instData.Image;
            Resources resources =context.getResources();
            int drawableId = resources.getIdentifier(imgname, "drawable", context.getPackageName());
           imag.setImageResource(drawableId);
           imag.setTag(imgname);
        }
        return convertView;
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getViewTypeCount() {
        return arrayList.size();
    }
    @Override
    public boolean isEmpty() {
        return false;
    }
}