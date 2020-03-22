package com.example.accessibilityapp.app.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.accessibilityapp.R;

import java.util.List;

public class AppAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;
    private List<App> listStorage;

    public AppAdapter(Context context, List<App> customizedListView) {
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        listStorage = customizedListView;
    }

    @Override
    public int getCount() {
        return listStorage.size();
    }

    @Override
    public App getItem(int id) {
        if (getCount() > id) {
            return listStorage.get(id);
        } else
            return null;
    }


    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        AppAdapter.ViewHolder listViewHolder;
        if (convertView == null) {
            listViewHolder = new AppAdapter.ViewHolder();
            convertView = layoutInflater.inflate(R.layout.installed_app_list, parent, false);

            listViewHolder.textInListView = (TextView) convertView.findViewById(R.id.list_app_name);
            listViewHolder.imageInListView = (ImageView) convertView.findViewById(R.id.app_icon);
            listViewHolder.packageInListView = (TextView) convertView.findViewById(R.id.app_package);
            convertView.setTag(listViewHolder);
        } else {
            listViewHolder = (AppAdapter.ViewHolder) convertView.getTag();
        }
        listViewHolder.textInListView.setText(listStorage.get(position).getName());
        listViewHolder.imageInListView.setImageDrawable(listStorage.get(position).getIcon());
        listViewHolder.packageInListView.setText(listStorage.get(position).getPackageName());

        return convertView;
    }

    class ViewHolder {
        TextView textInListView;
        ImageView imageInListView;
        TextView packageInListView;
    }
}
