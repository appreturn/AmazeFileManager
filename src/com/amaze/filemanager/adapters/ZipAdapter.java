/*
 * Copyright (C) 2014 Arpit Khurana <arpitkh96@gmail.com>, Vishal Nehra <vishalmeham2@gmail.com>
 *
 * This file is part of Amaze File Manager.
 *
 * Amaze File Manager is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.amaze.filemanager.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.amaze.filemanager.R;
import com.amaze.filemanager.activities.MainActivity;
import com.amaze.filemanager.fragments.ZipViewer;
import com.amaze.filemanager.services.asynctasks.ZipHelperTask;
import com.amaze.filemanager.utils.Futils;

import java.io.File;
import java.util.ArrayList;
import java.util.zip.ZipEntry;

public class ZipAdapter extends ArrayAdapter<ZipEntry> {
    Context c;
    Drawable folder, unknown;
    ArrayList<ZipEntry> enter;
    ZipViewer zipViewer;
    Futils futils;

    public ZipAdapter(Context c, int id, ArrayList<ZipEntry> enter, ZipViewer zipViewer) {
        super(c, id, enter);
        this.enter = enter;
        this.c = c;
        folder = c.getResources().getDrawable(R.drawable.ic_grid_folder1);
        unknown = c.getResources().getDrawable(R.drawable.ic_doc_generic_am);
        this.zipViewer = zipViewer;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView txtTitle;
        TextView txtDesc;
        TextView date;
        RelativeLayout rl;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        final ZipEntry rowItem = enter.get(position);

        View view;
        final int p = position;
        if (convertView == null) {
            int i = R.layout.simplerow;

            LayoutInflater mInflater = (LayoutInflater) c
                    .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = mInflater.inflate(i, null);
            final ViewHolder vholder = new ViewHolder();

            vholder.txtTitle = (TextView) view.findViewById(R.id.firstline);
            vholder.imageView = (ImageView) view.findViewById(R.id.icon);
            vholder.rl = (RelativeLayout) view.findViewById(R.id.second);

            view.setTag(vholder);

        } else {
            view = convertView;

        }
        final ViewHolder holder = (ViewHolder) view.getTag();

        final StringBuilder stringBuilder = new StringBuilder(rowItem.getName());
        if (rowItem.isDirectory()) {
            stringBuilder.deleteCharAt(rowItem.getName().length() - 1);
            holder.txtTitle.setText(stringBuilder.toString());
        } else {
            File file = new File(rowItem.getName());
            if (file.getParent()!=null){

                String parentLength = file.getParent();
                StringBuilder stringBuilder1 = new StringBuilder(rowItem.getName());
                stringBuilder1.delete(0, parentLength.length()+1);
                holder.txtTitle.setText(stringBuilder1.toString());
            } else {

                holder.txtTitle.setText(stringBuilder.toString());
            }
        }

        if (rowItem.isDirectory()) {
            holder.imageView.setImageDrawable(folder);
        } else {
            holder.imageView.setImageDrawable(unknown);
        }
        holder.rl.setOnClickListener(new View.OnClickListener() {

            public void onClick(View p1) {
                // TODO: Implement this method
                if (rowItem.isDirectory()) {

                    new ZipHelperTask(zipViewer, 1, stringBuilder.toString()).execute(zipViewer.f);
                } else {

                    futils = new Futils();
                    Toast.makeText(c, zipViewer.f.getPath() + "/" + stringBuilder.toString(), Toast.LENGTH_LONG).show();
                    File file = new File(zipViewer.f.getPath() + "/" + stringBuilder.toString());
                    futils.openFile(file, (MainActivity) zipViewer.getActivity());

                }
            }
        });
        return view;
    }
}
