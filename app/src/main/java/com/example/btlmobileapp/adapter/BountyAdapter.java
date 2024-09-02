package com.example.btlmobileapp.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.btlmobileapp.R;

import java.util.ArrayList;
import java.util.List;

public class BountyAdapter extends ArrayAdapter<String> {
    Context mct;
    ArrayList<String> arr;
    int QuestionPos = 0;
    public BountyAdapter( Context context, int resource,  List<String> objects) {
        super(context, resource, objects);
        this.mct = context;
        this.arr = new ArrayList<>(objects);
    }
    public void setViTri(int QuestionPos){
        this.QuestionPos = QuestionPos;
        notifyDataSetChanged();
    }
    @Override
    public View getView(int pos, View convertView, ViewGroup parent){
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater)mct.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.activity_bounty,null);

        }
        if(arr.size()>0){
            TextView txvBounty = convertView.findViewById(R.id.txvBounty);

            int reversePos = arr.size() - 1 - pos;
            int vitri = reversePos +1 ;
            String txtShow = vitri + "  " + arr.get(reversePos);
            txvBounty.setText(txtShow);

            if ((pos)%5 ==0){
                txvBounty.setTextColor(Color.parseColor("#ffd966"));
            } else{
                txvBounty.setTextColor(Color.parseColor("#ffffff"));
            }
            if(vitri == QuestionPos){
                txvBounty.setBackgroundColor(Color.parseColor("#FFC107"));
                txvBounty.setTextColor(Color.parseColor("#FF181717"));
            } else{
                txvBounty.setBackgroundColor(Color.parseColor("#00ff9800"));
            }
        }
        return convertView;
    }
}
