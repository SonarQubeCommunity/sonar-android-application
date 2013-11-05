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
import android.widget.BaseAdapter;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Plugin;

import java.util.List;

public class PluginsAdapter extends BaseAdapter {

  private LayoutInflater mInflater = null;
  private List<Plugin> sonarQubePlugins;
  private Context mContext;

  private final class ProjectsViewHolder {
    TextView pluginName;
    TextView pluginVersion;
  }

  private ProjectsViewHolder mHolder = null;

  public PluginsAdapter(Context context, List<Plugin> sonarQubePlugins) {
    this.mContext = context;
    this.sonarQubePlugins = sonarQubePlugins;
    if (mContext != null)
      mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return sonarQubePlugins.size();
  }

  @Override
  public Object getItem(int position) {
    return sonarQubePlugins.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      mHolder = new ProjectsViewHolder();
      convertView = mInflater.inflate(R.layout.plugin_item, null);
      convertView.setTag(mHolder);
    } else {
      mHolder = (ProjectsViewHolder) convertView.getTag();
    }

    mHolder.pluginName = (TextView) convertView.findViewById(R.id.pluginName);
    mHolder.pluginName.setText(sonarQubePlugins.get(position).getName());

    mHolder.pluginVersion = (TextView) convertView.findViewById(R.id.pluginVersion);
    mHolder.pluginVersion.setText(sonarQubePlugins.get(position).getVersion());

    return convertView;
  }

}
