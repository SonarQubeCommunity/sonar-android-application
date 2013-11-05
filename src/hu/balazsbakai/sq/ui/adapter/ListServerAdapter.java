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

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Server;
import hu.balazsbakai.sq.ui.fragments.FragmentListServers;
import hu.balazsbakai.sq.util.UsedServersUtil;

import java.util.List;

public class ListServerAdapter extends BaseAdapter {

  private LayoutInflater mInflater = null;
  private List<Server> sonarQubeServers;
  private Context mContext;
  private FragmentListServers mFragment;

  private final class ListServerViewHolder {
    TextView serverURL;
    TextView displayName;
    Button select;
    Button delete;
  }

  private ListServerViewHolder mHolder = null;

  public ListServerAdapter(Context context, FragmentListServers fragment) {
    mContext = context;
    mFragment = fragment;
    mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    sonarQubeServers = UsedServersUtil.getUsedServers((Activity) context).getServers();
  }

  @Override
  public int getCount() {
    return sonarQubeServers.size();
  }

  @Override
  public Object getItem(int position) {
    return sonarQubeServers.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {

    if (convertView == null) {
      mHolder = new ListServerViewHolder();
      convertView = mInflater.inflate(R.layout.list_servers_item, null);
      convertView.setTag(mHolder);

      mHolder.displayName = (TextView) convertView.findViewById(R.id.ListServerDisplayName);
      mHolder.displayName.setText(sonarQubeServers.get(position).getDisplayName());
      mHolder.serverURL = (TextView) convertView.findViewById(R.id.ListServerServerURL);
      mHolder.serverURL.setText(sonarQubeServers.get(position).getServerURL());
      mHolder.select = (Button) convertView.findViewById(R.id.listServersSelectButton);
      mHolder.select.setOnClickListener(mFragment);
      mHolder.select.setTag("SELECT#" + sonarQubeServers.get(position).getDisplayName());
      mHolder.delete = (Button) convertView.findViewById(R.id.listServersDeleteButton);
      mHolder.delete.setOnClickListener(mFragment);
      mHolder.delete.setTag("DELETE#" + sonarQubeServers.get(position).getDisplayName());

    } else {
      mHolder = (ListServerViewHolder) convertView.getTag();
    }

    return convertView;
  }

}
