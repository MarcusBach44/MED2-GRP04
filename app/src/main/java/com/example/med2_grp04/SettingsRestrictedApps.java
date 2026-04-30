package com.example.med2_grp04;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class SettingsRestrictedApps extends AppCompatActivity {

    List<String> sortedNames;
    List<String> sortedPackages;
    List<Drawable> sortedIcons;
    private TextView textViewStatus;
    private ListView listViewAppsInstalled;

    SeekBar seekBarCoverageAmount;

    SeekBar seekBarSpreadSpeed;

    SeekBar seekBarRecoveryRate;
    TextView valueCoverageAmount;

    TextView valueSpreadSpeed;

    TextView valueRecoveryRate;

    @Override
    protected void onCreate (Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_restricted_apps);
        textViewStatus = findViewById(R.id.txtRestrictApps);
        listViewAppsInstalled = findViewById(R.id.listViewInstalledApps);

        ImageButton btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        seekBarCoverageAmount = (SeekBar)findViewById(R.id.seekBar1);
        valueCoverageAmount = (TextView)findViewById(R.id.CoverageAmount);
        seekBarCoverageAmount.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueCoverageAmount.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarSpreadSpeed = (SeekBar)findViewById(R.id.seekBar2);
        valueSpreadSpeed = (TextView)findViewById(R.id.SpreadSpeed);
        seekBarSpreadSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueSpreadSpeed.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        seekBarRecoveryRate = (SeekBar)findViewById(R.id.seekBar3);
        valueRecoveryRate = (TextView)findViewById(R.id.RecoveryRate);
        seekBarRecoveryRate.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                valueRecoveryRate.setText(""+progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        GetAllApps();
    }

    public void GetAllApps() {
        PackageManager pm = this.getPackageManager();

        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> ril = pm.queryIntentActivities(mainIntent, 0);

        List<String> appNames = new ArrayList<>();
        List<String> appPackages = new ArrayList<>();
        List<Drawable> appIcons = new ArrayList<>();

        // 1. Collect all apps
        for (ResolveInfo ri : ril) {
            if (ri.activityInfo != null) {
                appNames.add(ri.loadLabel(pm).toString());
                appPackages.add(ri.activityInfo.packageName);
                appIcons.add(ri.loadIcon(pm));
            }
        }

        // 2. Sort alphabetically (this is the part you asked about)
        List<Integer> indexList = new ArrayList<>();
        for (int i = 0; i < appNames.size(); i++) indexList.add(i);


        indexList.sort((i1, i2) ->
                appNames.get(i1).compareToIgnoreCase(appNames.get(i2)));

        sortedNames = new ArrayList<>();
        sortedPackages = new ArrayList<>();
        sortedIcons = new ArrayList<>();

        for (int index : indexList) {
            sortedNames.add(appNames.get(index));
            sortedPackages.add(appPackages.get(index));
            sortedIcons.add(appIcons.get(index));
        }
        listViewAppsInstalled.setAdapter(new MyAdapter());
        Log.d("TAG", ""+sortedNames);
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return sortedNames.size();
        }

        @Override
        public Object getItem(int position) {
            return sortedNames.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.drawable.list_apps, parent, false);
            }

            TextView name = convertView.findViewById(R.id.appName);
            ImageView icon = convertView.findViewById(R.id.appIcon);



            name.setText(sortedNames.get(position));
            icon.setImageDrawable(sortedIcons.get(position));

            return convertView;


            };

        }

    }

