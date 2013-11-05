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

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Server;
import hu.balazsbakai.sq.pojo.ServerStatus;
import hu.balazsbakai.sq.util.CommonUtil;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker.EventLabel;
import hu.balazsbakai.sq.util.JsonUtil;
import hu.balazsbakai.sq.util.LogUtil;
import hu.balazsbakai.sq.util.NetworkUtil;
import hu.balazsbakai.sq.util.UsedServersUtil;

@SuppressLint("HandlerLeak")
public class FragmentAddNewServer extends Fragment implements OnClickListener {

  private Button buttonAddNewServer;
  private Button buttonTestConnection;

  private TextView actionBarTitleTextView;

  private MenuItem searchItem;
  private MenuItem refreshItem;

  private ViewPager viewPager;

  private EditText serverURL;
  private EditText displayName;
  private EditText userName;
  private EditText password;

  public FragmentAddNewServer() {
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

    View rootView = inflater.inflate(R.layout.add_new_server, container, false);
    buttonAddNewServer = (Button) rootView.findViewById(R.id.buttonAddNewServer);
    buttonAddNewServer.setOnClickListener(this);

    buttonTestConnection = (Button) rootView.findViewById(R.id.buttonTestConnection);
    buttonTestConnection.setOnClickListener(this);

    return rootView;
  }

  @Override
  public void onStart() {
    super.onStart();

    serverURL = (EditText) getActivity().findViewById(R.id.editText1);
    displayName = (EditText) getActivity().findViewById(R.id.editText2);
    userName = (EditText) getActivity().findViewById(R.id.editText3);
    password = (EditText) getActivity().findViewById(R.id.editText4);

    ((ActionBarActivity) getActivity()).getSupportActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
  }

  @Override
  public void onResume() {
    super.onResume();
    actionBarTitleTextView.setText(getString(R.string.addNewServer));
    viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
    viewPager.setVisibility(View.INVISIBLE);// prevent two fragment layer in each other
  }

  @Override
  public void onPause() {
    super.onPause();
    serverURL.setError(null);
    displayName.setError(null);
  }

  @Override
  public void onClick(View v) {
    if (v.getTag().equals("ADD")) {
      addNewServer();
    } else {
      testConnection();
    }
  }

  private void addNewServer() {
    if (isValidForm()) {

      GoogleAnalyticsTracker.trackUIEvent(getActivity(), EventLabel.ADD_NEW_SERVER_SAVE);

      CommonUtil.hideKeyBoard(getActivity());
      try {
        UsedServersUtil.saveNewServer(getActivity(), serverURL.getText().toString(), displayName.getText().toString(), userName.getText().toString(),
          password.getText().toString());
        Toast.makeText(getActivity(), getString(R.string.theServerIsSuccessfullySaved), Toast.LENGTH_SHORT).show();
        viewPager.getAdapter().notifyDataSetChanged();
        ((hu.balazsbakai.sq.MainActivity) getActivity()).goHomeScreen(this);
        serverURL.setText("");
        displayName.setText("");
        userName.setText("");
        password.setText("");
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private boolean isValidForm() {
    boolean isValid = true;

    if (serverURL.getText().toString().length() == 0) {
      serverURL.setError(getString(R.string.serverURLIsRequired));
      isValid = false;
    }

    if (!serverURL.getText().toString().startsWith("http://") && !serverURL.getText().toString().startsWith("https://")) {
      serverURL.setError(getString(R.string.serverMustStartsWith));
      isValid = false;
    }

    if (serverURL.getText().toString().endsWith("/")) {
      serverURL.setError(getString(R.string.serverMustNotEndsWith));
      isValid = false;
    }

    if (displayName.getText().toString().length() == 0) {
      displayName.setError(getString(R.string.displayNameIsRequired));
      isValid = false;
    }

    if (displayName.getText().toString().length() > 18) {
      displayName.setError(getString(R.string.displayNameMustBeLessThan));
      isValid = false;
    }

    if (!UsedServersUtil.isUniqueDisplayName(displayName.getText().toString())) {
      displayName.setError(getString(R.string.displayNameMustBeUnique));
      isValid = false;
    }

    return isValid;
  }

  private Handler handler = new Handler() {
    @Override
    public void handleMessage(Message msg) {
      if (msg.obj != null) {
        ServerStatus ss = JsonUtil.processServerStatusData(msg.obj.toString());
        LogUtil.i("Server status", ss.toString());
        buttonTestConnection.setBackgroundColor(getResources().getColor(R.color.button_green));
      } else {
        buttonTestConnection.setBackgroundColor(getResources().getColor(R.color.button_red));
      }
    }
  };

  private void testConnection() {

    if (isValidForm()) {

      GoogleAnalyticsTracker.trackUIEvent(getActivity(), EventLabel.ADD_NEW_SERVER_TEST_CONNECTION);

      boolean isConnected = NetworkUtil.checkConnectionAndGetData(handler, getActivity(), new Server().withDisplayName(displayName.getText().toString())
        .withserverURL(serverURL.getText().toString()).withUsernameAndPassword(userName.getText().toString(), password.getText().toString()),
        NetworkUtil.SERVER_STATUS);

      if (!isConnected) {
        new AlertDialog.Builder(getActivity()).setTitle(getString(R.string.networkStatus)).setMessage(getString(R.string.yourDevice)).create().show();
      }
    }
  }

}
