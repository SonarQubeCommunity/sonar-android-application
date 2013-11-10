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
import hu.balazsbakai.sq.pojo.Project;
import hu.balazsbakai.sq.ui.adapter.ProjectsAdapter;
import hu.balazsbakai.sq.util.JsonUtil;
import hu.balazsbakai.sq.util.NetworkUtil;

import java.util.List;

public class FragmentProjects extends PagerFragment {

  private List<Project> sonarQubeProjects;

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    initResources(R.layout.projects, R.id.projectListView, R.id.empty_projectListView, R.id.projectLoadingPanel, R.id.noconnection_projects);
    return super.onCreateView(inflater, container, savedInstanceState);
  }

  @Override
  protected List<Project> getDataObject() {
    return sonarQubeProjects;
  }

  @Override
  protected void setDataObject(List dataObject) {
    sonarQubeProjects = dataObject;
  }

  @Override
  protected boolean getData() {
    return NetworkUtil.getInstance().checkConnectionAndGetData(handler, getActivity(), sonarQubeServer, NetworkUtil.SERVER_PROJECTS);
  }

  @Override
  protected List processJsonData(String data) {
    return JsonUtil.getInstance().processProjectsData(data);
  }

  @Override
  protected void addItemsToListAdapter() {
    listView.setAdapter(new ProjectsAdapter(getActivity(), getDataObject(), sonarQubeServer));
  }

}
