package com.example.btlmobileapp;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class DialogGuestHelp extends Dialog {

    private TextView txvA, txvB, txvC, txvD;
    private TextView txvAPercent, txvBPercent, txvCPercent, txvDPercent;

    public DialogGuestHelp(@NonNull Context context, int rightAnsPos) {
        super(context);
        setContentView(R.layout.activity_guest_help);
        Button btnOK= findViewById(R.id.btnOK);
        btnOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        txvA = findViewById(R.id.txvA);
        txvB = findViewById(R.id.txvB);
        txvC = findViewById(R.id.txvC);
        txvD = findViewById(R.id.txvD);

        txvAPercent = findViewById(R.id.txvA_Percent);
        txvBPercent = findViewById(R.id.txvB_Percent);
        txvCPercent = findViewById(R.id.txvC_Percent);
        txvDPercent = findViewById(R.id.txvD_Percent);

        // Tạo tỷ lệ phần trăm ngẫu nhiên cho các đáp án
        Random random = new Random();
        int totalPercent = 100;
        int[] percentages = new int[4];
        int highPercent = random.nextInt(1) + 40;
        percentages[rightAnsPos] = highPercent;
        totalPercent -= highPercent;

        for (int i = 0; i < 4; i++) {
            if (i != rightAnsPos) {
                if (totalPercent > 0) {
                    int percent = random.nextInt(totalPercent / 3) + 5;
                    percentages[i] = percent;
                    totalPercent -= percent;
                } else {
                    percentages[i] = 0;
                }
            }
        }

        if (totalPercent > 0) {
            for (int i = 0; i < 4; i++) {
                if (i != rightAnsPos && totalPercent > 0) {
                    percentages[i] += totalPercent;
                    totalPercent = 0;
                    break;
                }
            }
        }
        TextView[] answerViews = {txvA, txvB, txvC, txvD};
        TextView[] percentViews = {txvAPercent, txvBPercent, txvCPercent, txvDPercent};

        for (int i = 0; i < 4; i++) {
            answerViews[i].setText("Option " + (char) ('A' + i));
            percentViews[i].setText(percentages[i] + "%");

        }

    }
}


