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
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Server;
import hu.balazsbakai.sq.util.LogUtil;
import hu.balazsbakai.sq.util.UsedServersUtil;

import java.util.List;

public abstract class PagerFragment extends Fragment {

  private int layoutId;
  private int listViewId;
  private int emptyListViewId;
  private int loadingId;
  private int noConnectionId;

  protected Server sonarQubeServer;
  protected ListView listView;
  protected boolean isConnectedToTheNetwork = true;
  protected boolean isRead = false;

  protected abstract List processJsonData(String data);

  protected abstract List getDataObject();

  protected abstract void setDataObject(List dataObject);

  protected abstract void addItemsToListAdapter();

  protected abstract boolean getData();

  protected void initResources(int layoutId, int listViewId, int emptyListViewId, int loadingId, int noConnectionId) {
    this.layoutId = layoutId;
    this.listViewId = listViewId;
    this.emptyListViewId = emptyListViewId;
    this.loadingId = loadingId;
    this.noConnectionId = noConnectionId;

  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    LogUtil.d(getClass().getSimpleName(), "onCreateView");

    if (container == null) {
      return null;
    }
    setHasOptionsMenu(true);
    View rootView = inflater.inflate(layoutId, container, false);

    return rootView;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    LogUtil.d(getClass().getSimpleName(), "onOptionsItemSelected");

    switch (item.getItemId()) {
      case R.id.action_refresh:
        if (listView != null) {
          showLoadingLayout();
          readData();
        }
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  @Override
  public void onResume() {
    LogUtil.d(getClass().getSimpleName(), "onResume");
    super.onResume();

    listView = (ListView) getActivity().findViewById(listViewId);
    listView.setEmptyView(getActivity().findViewById(emptyListViewId));

    if (sonarQubeServer == null || getDataObject() == null) {
      readData();
    } else if (!isCurrentAndPreviousLastUsedDisplayNameEquals()) {
      isRead = false;
      readData();
    } else if (isConnectedToTheNetwork) {
      populateListAdapter();
    } else if (!isConnectedToTheNetwork) {
      showNoConnectionLayout();
    }
  }

  private void readData() {
    LogUtil.d(getClass().getSimpleName(), "readData");

    sonarQubeServer = UsedServersUtil.getUsedServers(getActivity()).getLastUsedServer();
    if (sonarQubeServer != null) {
      if (!isRead) {
        isRead = true;
        isConnectedToTheNetwork = getData();
      }
      if (!isConnectedToTheNetwork) {
        isRead = false;
        showNoConnectionLayout();
      }
    } else {
      showEmptyListView();
    }
  }

  protected Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      LogUtil.d(getClass().getSimpleName(), "handleMessage");
      isRead = false;

      // the asynch answer is belongs to an older server, so we van discard it
      if (!msg.getData().getString("displayName").equals(UsedServersUtil.getUsedServers(getActivity()).getLastUsedDisplayName())) {
        return;
      }

      if (msg.obj != null) {
        setDataObject(processJsonData(msg.obj.toString()));

        try {
          populateListAdapter();
        } catch (Exception e) {
          LogUtil.e("activity is closed, but a refresh action has not finished yet", e);
        }
      } else {
        setDataObject(null);
        showEmptyListView();
      }
    }
  };

  private void populateListAdapter() {
    LogUtil.d(getClass().getSimpleName(), "populateListAdapter");

    if (listView != null && getDataObject() != null) {
      if (getDataObject().size() == 0) {
        showEmptyListView();
      } else {
        addItemsToListAdapter();
        showPopulatedListView();
      }
    }
  }

  protected boolean isCurrentAndPreviousLastUsedDisplayNameEquals() {
    return UsedServersUtil.getUsedServers(getActivity()).getLastUsedDisplayName().equals(sonarQubeServer.getDisplayName());
  }

  protected void showNoConnectionLayout() {
    getActivity().findViewById(loadingId).setVisibility(View.GONE);
    getActivity().findViewById(emptyListViewId).setVisibility(View.GONE);
    getActivity().findViewById(noConnectionId).setVisibility(View.VISIBLE);
  }

  protected void showPopulatedListView() {
    listView.setVisibility(View.VISIBLE);
    RelativeLayout r = (RelativeLayout) getActivity().findViewById(loadingId);
    if (r != null)
      r.setVisibility(View.GONE);
  }

  protected void showLoadingLayout() {
    listView.setVisibility(View.GONE);

    RelativeLayout l = (RelativeLayout) getActivity().findViewById(loadingId);
    if (l != null)
      l.setVisibility(View.VISIBLE);
  }

  protected void showEmptyListView() {
    RelativeLayout r = (RelativeLayout) getActivity().findViewById(loadingId);
    if (r != null)
      r.setVisibility(View.GONE);

    LinearLayout ll = (LinearLayout) getActivity().findViewById(emptyListViewId);
    if (ll != null)
      ll.setVisibility(View.VISIBLE);
  }

}
