package com.example.med2_grp04;

import static android.content.Intent.FLAG_ACTIVITY_REORDER_TO_FRONT;
import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.graphics.drawable.shapes.Shape;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.SeekBar;
import android.widget.Switch;
import android.widget.TextView;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;

public class ViewAllApps extends AppCompatActivity {

List<String> sortedNames;
List<String> sortedPackages;
List<Drawable> sortedIcons;

List<Integer> filteredIndex;
private ListView listAllApps;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_all_apps);


        listAllApps = findViewById(R.id.listAllApps);

        ImageButton btnBackToRestricted = findViewById(R.id.btnBackToRestricted);
        btnBackToRestricted.setOnClickListener(v -> finish());

        SearchView searchBarApp = findViewById(R.id.searchBarApp);

        searchBarApp.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
                    public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
                    public boolean onQueryTextChange(String s) {
                filterApps(s);
                return false;
            }
        });

        GetAllApps();
    }

    private void filterApps(String query) {
        filteredIndex.clear();

        for (int i=0; i < sortedNames.size(); i++) {
            if (sortedNames.get(i).toLowerCase().contains(query.toLowerCase())) {
                filteredIndex.add(i);
            }
        }

        ((BaseAdapter) listAllApps.getAdapter()).notifyDataSetChanged();
    }

    public void GetAllApps() {
    PackageManager pm = this.getPackageManager();
    Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
    mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

    List<ResolveInfo> ril = pm.queryIntentActivities(mainIntent, 0);

    List<String> appNames = new ArrayList<>();
    List<String> appPackages = new ArrayList<>();
    List<Drawable> appIcons = new ArrayList<>();

    for (ResolveInfo ri : ril) {
        if (ri.activityInfo != null) {
            appNames.add(ri.loadLabel(pm).toString());
            appPackages.add(ri.activityInfo.packageName);
            appIcons.add(ri.loadIcon(pm));
        }
    }

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

    filteredIndex = new ArrayList<>();
    for (int i =0; i < sortedNames.size(); i++)  {
        filteredIndex.add(i);
    }

    listAllApps.setAdapter(new MyAdapter()); }

    private class MyAdapter extends BaseAdapter {
        class ViewHolder {
            TextView name;
            ImageView icon;
            Switch appSwitch;
        }

        @Override
        public int getCount() {
            return filteredIndex.size();
        }

        @Override
        public Object getItem(int position) {
            return sortedNames.get(filteredIndex.get(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = getLayoutInflater().inflate(R.drawable.list_apps, parent, false);

            holder = new ViewHolder();
            holder.name = convertView.findViewById(R.id.appName);
            holder.icon = convertView.findViewById(R.id.appIcon);
            holder.appSwitch = convertView.findViewById(R.id.appSwitch);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        int index = filteredIndex.get(position);

        String pkg = sortedPackages.get(index);
        String appName = sortedNames.get(index);
        Drawable appIcon = sortedIcons.get(index);

        holder.name.setText(appName);
        holder.icon.setImageDrawable(appIcon);

        SharedPreferences prefs = getSharedPreferences("Restricted Apps", MODE_PRIVATE);
        boolean savedState = prefs.getBoolean(pkg, false);

        holder.appSwitch.setOnCheckedChangeListener(null);
        holder.appSwitch.setChecked(savedState);

        holder.appSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (!ForegroundService.restrictedApps.contains(pkg)) {
                    ForegroundService.restrictedApps.add(pkg);
                }
                prefs.edit().putBoolean(pkg, true).apply();
            } else {
                ForegroundService.restrictedApps.remove(pkg);
                prefs.edit().putBoolean(pkg, false).apply();
            }
        });
        return convertView;
    }
    }
    }