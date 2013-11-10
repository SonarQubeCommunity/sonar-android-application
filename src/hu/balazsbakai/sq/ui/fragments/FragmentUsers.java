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

package hu.balazsbakai.sq.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.User;
import hu.balazsbakai.sq.ui.adapter.UsersAdapter;
import hu.balazsbakai.sq.util.JsonUtil;
import hu.balazsbakai.sq.util.NetworkUtil;

import java.util.List;

public class FragmentUsers extends PagerFragment {

  private List<User> sonarQubeUsers;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    initResources(R.layout.users, R.id.usersListView, R.id.empty_usersListView, R.id.usersLoadingPanel, R.id.noconnection_users);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  protected List<User> getDataObject() {
    return sonarQubeUsers;
  }

  @Override
  protected void setDataObject(List dataObject) {
    sonarQubeUsers = dataObject;
  }

  @Override
  protected boolean getData() {
    return NetworkUtil.getInstance().checkConnectionAndGetData(handler, getActivity(), sonarQubeServer, NetworkUtil.SERVER_USERS);
  }

  @Override
  protected List processJsonData(String data) {
    return JsonUtil.getInstance().processUsersData(data);
  }

  @Override
  protected void addItemsToListAdapter() {
    listView.setAdapter(new UsersAdapter(getActivity(), getDataObject()));
  }

}
