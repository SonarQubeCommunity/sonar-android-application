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
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Project;
import hu.balazsbakai.sq.pojo.Server;

public class FragmentProjectDetails extends Fragment {

  private TextView actionBarTitleTextView;
  private MenuItem searchItem;
  private MenuItem refreshItem;
  private ViewPager viewPager;

  private Project sonarQubeProject;
  private Server sonarQubeServer;

  public FragmentProjectDetails() {

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

    if (container == null) {
      return null;
    }

    this.sonarQubeProject = (Project) getArguments().getSerializable("project");
    this.sonarQubeServer = (Server) getArguments().getSerializable("server");

    actionBarTitleTextView = (TextView) ((ActionBarActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionBarTitle);
    actionBarTitleTextView.setOnClickListener(null);

    View rootView = inflater.inflate(R.layout.project_details, container, false);

    return rootView;
  }

  @Override
  public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
    super.onCreateOptionsMenu(menu, inflater);

    searchItem = menu.findItem(R.id.action_search);
    refreshItem = menu.findItem(R.id.action_refresh);

    searchItem.setVisible(false);
    refreshItem.setVisible(false);
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {

    super.onCreate(savedInstanceState);
    setHasOptionsMenu(true);

  }

  @Override
  public void onStart() {

    super.onStart();

    ((ActionBarActivity) getActivity()).getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
  }

  @Override
  public void onResume() {

    super.onResume();

    actionBarTitleTextView.setText(sonarQubeProject.getName());
    viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
    viewPager.setVisibility(View.INVISIBLE);


  }



}
