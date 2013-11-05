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
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.ui.adapter.ListServerAdapter;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker.EventLabel;
import hu.balazsbakai.sq.util.UsedServersUtil;

public class FragmentListServers extends Fragment implements OnClickListener {

  private ViewPager viewPager;
  private TextView actionBarTitleTextView;
  private MenuItem searchItem;
  private MenuItem refreshItem;
  private ListView listView;

  public FragmentListServers() {
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

    actionBarTitleTextView = (TextView) ((ActionBarActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionBarTitle);
    actionBarTitleTextView.setOnClickListener(null);
    View rootView = inflater.inflate(R.layout.list_servers, container, false);
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
    actionBarTitleTextView.setText(getString(R.string.listServers));
    viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
    viewPager.setVisibility(View.INVISIBLE);

    listView = (ListView) getActivity().findViewById(R.id.listServerListView);
    listView.setEmptyView(getActivity().findViewById(R.id.empty_listservers_view));
    listView.setAdapter(new ListServerAdapter(getActivity(), this));
  }

  @Override
  public void onClick(View view) {
    String type = view.getTag().toString().substring(0, view.getTag().toString().indexOf("#"));
    String displayName = view.getTag().toString().substring(view.getTag().toString().indexOf("#") + 1);

    if (type.equals("SELECT")) {
      select(displayName);
    } else {
      delete(displayName);
    }
  }

  private void select(String displayName) {
    GoogleAnalyticsTracker.trackUIEvent(getActivity(), EventLabel.LIST_SERVER_SELECT);

    UsedServersUtil.updateLastUsedDisplayName(getActivity(), displayName);
    viewPager.getAdapter().notifyDataSetChanged();
    ((hu.balazsbakai.sq.MainActivity) getActivity()).goHomeScreen(this);
  }

  private void delete(String displayName) {
    GoogleAnalyticsTracker.trackUIEvent(getActivity(), EventLabel.LIST_SERVER_DELETE);

    UsedServersUtil.deleteServer(getActivity(), displayName);

    if (isDeleteItSelf(displayName)) {
      if (isItLastServer()) {
        UsedServersUtil.updateLastUsedDisplayName(getActivity(), "");
      } else {
        UsedServersUtil.updateLastUsedDisplayName(getActivity(), UsedServersUtil.getUsedServers(getActivity()).getServers().get(0).getDisplayName());
      }
      viewPager.getAdapter().notifyDataSetChanged();
    }
    listView.setAdapter(new ListServerAdapter(getActivity(), this));
  }

  private boolean isItLastServer() {
    return UsedServersUtil.getUsedServers(getActivity()).getServers().size() == 0;
  }

  private boolean isDeleteItSelf(String displayName) {
    return displayName.equals(UsedServersUtil.getUsedServers(getActivity()).getLastUsedDisplayName());
  }
}
