/*
 * sonar-android-application
 * Copyright (C) 2013 Balázs Bakai
 * mailto:bakaibalazs AT gmail DOT com
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02
 */

package hu.balazsbakai.sq.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.ui.DrawerMenuItemModel;

public class DrawerMenuAdapter extends ArrayAdapter<DrawerMenuItemModel> {

  public DrawerMenuAdapter(Context context) {
    super(context, 0);
  }

  public void addItem(String title) {
    add(new DrawerMenuItemModel(title, false));
  }

  public void addHeader(String title) {
    add(new DrawerMenuItemModel(title, true));
  }

  @Override
  public int getViewTypeCount() {
    return 2;
  }

  @Override
  public int getItemViewType(int position) {
    return getItem(position).isHeader ? 0 : 1;
  }

  @Override
  public boolean isEnabled(int position) {
    return !getItem(position).isHeader;
  }

  public static class ViewHolder {
    public final TextView textHolder;

    public ViewHolder(TextView text1) {
      this.textHolder = text1;
    }
  }

  public View getView(int position, View convertView, ViewGroup parent) {

    DrawerMenuItemModel item = getItem(position);
    ViewHolder holder = null;
    View view = convertView;

    if (view == null) {
      int layout = R.layout.drawer_item;
      if (item.isHeader){
        layout = R.layout.drawer_header;
      }

      view = LayoutInflater.from(getContext()).inflate(layout, null);

      TextView text1 = (TextView) view.findViewById(R.id.menurow_title);
      view.setTag(new ViewHolder(text1));
    }

    if (holder == null && view != null) {
      Object tag = view.getTag();
      if (tag instanceof ViewHolder) {
        holder = (ViewHolder) tag;
      }
    }

    if (item != null && holder != null) {
      if (holder.textHolder != null){
        holder.textHolder.setText(item.title);
      }
    }

    return view;
  }

}
