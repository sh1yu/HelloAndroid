package com.example.helloandroid;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class ShowRunningProcessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_running_process);

        Intent intent = getIntent();
        boolean isSystem = intent.getStringExtra("is_system").equals("true");

        PackageManager packageManager = getPackageManager();
        List<PackageInfo> packageInfos = packageManager.getInstalledPackages(0);

        //table

        TableLayout tl = (TableLayout) findViewById(R.id.tablelayout);

        TableRow tr_head = new TableRow(this);
        tr_head.setBackgroundColor(Color.GRAY);
        tr_head.setLayoutParams(new TableRow.LayoutParams(
                TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        TextView textView = new TextView(this);
        textView.setText("pid");
        textView.setTextColor(Color.BLACK);
        textView.setPadding(5, 5, 5, 5);
        tr_head.addView(textView);

        textView = new TextView(this);
        textView.setText("描述");
        textView.setTextColor(Color.BLACK);
        textView.setPadding(5, 5, 5, 5);
        tr_head.addView(textView);

        textView = new TextView(this);
        textView.setText("是否系统应用");
        textView.setTextColor(Color.BLACK);
        textView.setPadding(5, 5, 5, 5);
        tr_head.addView(textView);

        tl.addView(tr_head, new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                TableLayout.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < packageInfos.size(); i++) {

            PackageInfo pi = packageInfos.get(i);
            if ((pi.applicationInfo.flags & ApplicationInfo.FLAG_STOPPED) != 0) {
                continue;
            }

            if (isSystem && (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                continue;
            }

            if (!isSystem && (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                continue;
            }

            TableRow tr_row = new TableRow(this);
            tr_row.setBackgroundColor(Color.GRAY);
            tr_row.setLayoutParams(new TableRow.LayoutParams(
                    TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            // Here create the TextView dynamically

            textView = new TextView(this);
            textView.setText(String.valueOf(pi.applicationInfo.uid));
            textView.setTextColor(Color.WHITE);
            textView.setPadding(5, 5, 5, 5);
            tr_row.addView(textView);

            textView = new TextView(this);
            textView.setText(pi.applicationInfo.processName);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(5, 5, 5, 5);
            tr_row.addView(textView);

            textView = new TextView(this);
            String isSystemStr = (pi.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0 ? "是" : "否";
            textView.setText(isSystemStr);
            textView.setTextColor(Color.WHITE);
            textView.setPadding(5, 5, 5, 5);
            tr_row.addView(textView);

            tl.addView(tr_row, new TableLayout.LayoutParams(
                    TableLayout.LayoutParams.MATCH_PARENT,                    //part4
                    TableLayout.LayoutParams.WRAP_CONTENT));
        }

    }


}