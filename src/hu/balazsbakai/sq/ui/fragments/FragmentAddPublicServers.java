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

import hu.balazsbakai.sq.util.LogUtil;

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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Server;
import hu.balazsbakai.sq.ui.adapter.AddPublicServersAdapter;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker.EventLabel;
import hu.balazsbakai.sq.util.UsedServersUtil;

import java.util.List;

public class FragmentAddPublicServers extends Fragment implements OnClickListener {

  private ViewPager viewPager;
  private TextView actionBarTitleTextView;
  private MenuItem searchItem;
  private MenuItem refreshItem;
  private Button buttonPopulate;
  private ListView listView;

  public FragmentAddPublicServers() {
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
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    if (container == null) {
      return null;
    }

    View rootView = inflater.inflate(R.layout.add_public_servers, container, false);

    actionBarTitleTextView = (TextView) ((ActionBarActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionBarTitle);
    actionBarTitleTextView.setOnClickListener(null);
    buttonPopulate = (Button) rootView.findViewById(R.id.autoPopulatePopulateButton);
    buttonPopulate.setOnClickListener(this);

    return rootView;
  }

  @Override
  public void onStart() {
    super.onStart();
    ((ActionBarActivity) getActivity()).getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
  }

  @Override
  public void onResume() {
    super.onResume();
    actionBarTitleTextView.setText(getString(R.string.addPublicServers));
    viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
    viewPager.setVisibility(View.INVISIBLE);

    listView = (ListView) getActivity().findViewById(R.id.listViewA);
    listView.setEmptyView(getActivity().findViewById(R.id.empty_addpublicservers_view));
    listView.setAdapter(new AddPublicServersAdapter(getActivity()));

    if (listView.getAdapter().getCount() == 0) {
      LinearLayout ll = (LinearLayout) getActivity().findViewById(R.id.addButtonLayout);
      ll.removeAllViews();
    }

  }

  @Override
  public void onClick(View v) {
    addPublicServers();
  }

  private void addPublicServers() {

    GoogleAnalyticsTracker.trackUIEvent(getActivity(), EventLabel.ADD_PUBLIC_SERVERS_ADD);

    List<Server> storedServers = UsedServersUtil.getUsedServers(getActivity()).getServers();
    for (int i = 0; i < listView.getAdapter().getCount(); i++) {
      Server sqs = (Server) listView.getAdapter().getItem(i);

      if (sqs.isChecked() && !storedServers.contains(new Server(sqs.getDisplayName()))) {
        try {
          UsedServersUtil.saveNewServer(getActivity(), sqs.getServerURL(), sqs.getDisplayName(), "", "");
        } catch (Exception e) {
          LogUtil.e("AddPublicServers", e);
        }
      }
    }

    // Toast t=Toast.makeText(getActivity(), getString(R.string.ThePublicServersAreSuccessfullyAdded), Toast.LENGTH_SHORT).show();
    viewPager.getAdapter().notifyDataSetChanged();
    ((hu.balazsbakai.sq.MainActivity) getActivity()).goHomeScreen(this);

  }

}
