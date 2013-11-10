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
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.google.common.base.Strings;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.User;
import hu.balazsbakai.sq.util.UsageTracker;
import hu.balazsbakai.sq.util.UsageTracker.EventLabel;

import java.util.List;

public class UsersAdapter extends BaseAdapter {

  private LayoutInflater mInflater = null;
  private List<User> sonarQubeUsers;
  private Context mContext;

  private final class ProjectsViewHolder {
    TextView userName;
    TextView userLogin;
    // TextView userActive;
    TextView userEmail;
  }

  private ProjectsViewHolder mHolder = null;

  public UsersAdapter(Context context, List<User> sonarQubeUsers) {
    this.mContext = context;
    this.sonarQubeUsers = sonarQubeUsers;
    if (mContext != null)
      mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return sonarQubeUsers.size();
  }

  @Override
  public Object getItem(int position) {
    return sonarQubeUsers.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      mHolder = new ProjectsViewHolder();
      convertView = mInflater.inflate(R.layout.user_item, null);
      convertView.setTag(mHolder);
    } else {
      mHolder = (ProjectsViewHolder) convertView.getTag();
    }

    mHolder.userName = (TextView) convertView.findViewById(R.id.userName);
    if (!Strings.isNullOrEmpty(sonarQubeUsers.get(position).getName()))
      mHolder.userName.setText(sonarQubeUsers.get(position).getName());

    mHolder.userLogin = (TextView) convertView.findViewById(R.id.userLogin);
    mHolder.userLogin.setText(sonarQubeUsers.get(position).getLogin());

    mHolder.userEmail = (TextView) convertView.findViewById(R.id.userEmail);
    if (!Strings.isNullOrEmpty(sonarQubeUsers.get(position).getEmail())) {
      mHolder.userEmail.setText(sonarQubeUsers.get(position).getEmail());
      mHolder.userEmail.setOnClickListener(new OnClickListener() {
        @Override
        public void onClick(View v) {
          UsageTracker.getInstance().trackUIEvent((Activity) mContext, EventLabel.USERS_ADAPTER_EMAIL);
          mContext.startActivity(new Intent(Intent.ACTION_SEND).setType("message/rfc822").putExtra(Intent.EXTRA_EMAIL,
            new String[] {sonarQubeUsers.get(position).getEmail()}));

        }
      });

    } else {
      mHolder.userEmail.setVisibility(View.GONE);
    }

    return convertView;
  }

}
