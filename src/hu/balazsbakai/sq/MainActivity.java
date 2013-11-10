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

package hu.balazsbakai.sq;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import com.google.common.base.Strings;
import hu.balazsbakai.sq.pojo.UsedServers;
import hu.balazsbakai.sq.ui.ActionBarCustomTitleOnClickListener;
import hu.balazsbakai.sq.ui.ActionBarTabListener;
import hu.balazsbakai.sq.ui.adapter.DrawerMenuAdapter;
import hu.balazsbakai.sq.ui.adapter.PagerAdapter;
import hu.balazsbakai.sq.ui.fragments.FragmentAddNewServer;
import hu.balazsbakai.sq.ui.fragments.FragmentAddPublicServers;
import hu.balazsbakai.sq.ui.fragments.FragmentDonation;
import hu.balazsbakai.sq.ui.fragments.FragmentListServers;
import hu.balazsbakai.sq.ui.fragments.FragmentRating;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker.ScreenName;
import hu.balazsbakai.sq.util.LogUtil;
import hu.balazsbakai.sq.util.RatingUtil;
import hu.balazsbakai.sq.util.UsedServersUtil;

public class MainActivity extends ActionBarActivity {

  private static final String GOOGLEPLAY_LINK = "https://play.google.com/store/apps/details?id=hu.balazsbakai.sq";
  private ViewPager viewPager;
  private TextView actionBarTitleTextView;

  private DrawerLayout mLayout;
  private ListView mList;
  private ActionBarDrawerToggle mToggle;
  private ActionBar actionBar;
  private DrawerMenuAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    LogUtil.d("MainActivity", "onCreate");
    setContentView(R.layout.activity_main);

    initActionBar();
    initNavigationDrawer();

    RatingUtil.applicationLaunched(this);

  }

  private void initActionBar() {
    actionBar = getSupportActionBar();
    final PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager());
    viewPager = (ViewPager) findViewById(R.id.viewpager);

    viewPager.setAdapter(pagerAdapter);
    viewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
      @Override
      public void onPageSelected(int position) {
        getSupportActionBar().setSelectedNavigationItem(position); // When swiping between pages, select the corresponding tab.
      }
    });
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    actionBar.setLogo(R.drawable.logo); // A2.2
    final ActionBarTabListener actionBarTabListener = new ActionBarTabListener(viewPager);
    // actionBar.addTab(actionBar.newTab().setText(getString(R.string.favourites)).setTabListener(actionBarTabListener));
    actionBar.addTab(actionBar.newTab().setText(getString(R.string.projects)).setTabListener(actionBarTabListener));
    actionBar.addTab(actionBar.newTab().setText(getString(R.string.users)).setTabListener(actionBarTabListener));
    actionBar.addTab(actionBar.newTab().setText(getString(R.string.plugins)).setTabListener(actionBarTabListener));

    // Clickable custom actionBar title
    actionBar.setDisplayShowTitleEnabled(false);
    actionBar.setCustomView(R.layout.custom_actionbar);
    actionBar.setDisplayShowCustomEnabled(true);
    actionBarTitleTextView = (TextView) actionBar.getCustomView().findViewById(R.id.actionBarTitle);
    actionBarTitleTextView.setOnClickListener(new ActionBarCustomTitleOnClickListener(getSupportFragmentManager()));
    actionBarTitleTextView.setText(getString(R.string.app_name));
    actionBar.show();
  }

  private void initNavigationDrawer() {

    actionBar.setDisplayHomeAsUpEnabled(true);
    actionBar.setHomeButtonEnabled(true);
    mLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
    mList = (ListView) findViewById(R.id.left_drawer);
    mAdapter = new DrawerMenuAdapter(this);
    mList.setAdapter(mAdapter);
    mList.setOnItemClickListener(new DrawerItemClickListener());

    mToggle = new ActionBarDrawerToggle(this, mLayout, R.drawable.ic_navigation_drawer, 0, 0) {
      public void onDrawerOpened(View drawerView) {
        // navigationBar open
      }

      public void onDrawerClosed(View view) {
        // navigationBar closed
      }
    };

    mAdapter.addHeader(getString(R.string.serverManagement));
    mAdapter.addItem(getString(R.string.addNewServer));
    mAdapter.addItem(getString(R.string.listServers));
    mAdapter.addItem(getString(R.string.addPublicServers));
    mAdapter.addHeader(getString(R.string.extra));
    mAdapter.addItem(getString(R.string.sharing));
    mAdapter.addItem(getString(R.string.rating));
    mAdapter.addItem(getString(R.string.donation));
    mLayout.setDrawerListener(mToggle);
  }

  @Override
  protected void onPostCreate(Bundle savedInstanceState) {
    super.onPostCreate(savedInstanceState);
    mToggle.syncState(); // Sync the toggle state after onRestoreInstanceState has occurred.
  }

  @Override
  public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    mToggle.onConfigurationChanged(newConfig);
  }

  @Override
  public boolean onMenuOpened(int featureId, Menu menu) {
    mLayout.openDrawer(mList);
    return super.onMenuOpened(featureId, menu);
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.activity_main_actions, menu);
    final MenuItem searchItem = menu.findItem(R.id.action_search);
    searchItem.setVisible(false);
    // SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) { // Handle presses on the action bar items
    if (mToggle.onOptionsItemSelected(item)) { // Pass the event to ActionBarDrawerToggle, if it returns true, then it has handled the app
                                               // icon touch event
      return true;
    }
    return super.onOptionsItemSelected(item);
  }

  private class DrawerItemClickListener implements OnItemClickListener {

    @Override
    public void onItemClick(AdapterView parent, View view, int position, long id) { // select item from navigator drawer

      mList.setItemChecked(position, true); // Highlight the selected item,
      actionBar.setTitle(mAdapter.getItem(position).title); // update the title
      mLayout.closeDrawer(mList); // And close the drawer
      final String title = mAdapter.getItem(position).title; // chooser

      if (title.equals(getString(R.string.addNewServer))) {
        GoogleAnalyticsTracker.trackScreen(MainActivity.this, ScreenName.ADD_NEW_SERVER);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentAddNewServer()).commit();

      } else if (title.equals(getString(R.string.listServers))) {
        GoogleAnalyticsTracker.trackScreen(MainActivity.this, ScreenName.LIST_SERVERS);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentListServers()).commit();

      } else if (title.equals(getString(R.string.addPublicServers))) {
        GoogleAnalyticsTracker.trackScreen(MainActivity.this, ScreenName.ADD_PUBLIC_SERVERS);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentAddPublicServers()).commit();

      } else if (title.equals(getString(R.string.donation))) {
        GoogleAnalyticsTracker.trackScreen(MainActivity.this, ScreenName.DONATION);
        getSupportFragmentManager().beginTransaction().replace(R.id.content_frame, new FragmentDonation()).commit();

      } else if (title.equals(getString(R.string.rating))) {
        GoogleAnalyticsTracker.trackScreen(MainActivity.this, ScreenName.RATING);
        new FragmentRating().show(getSupportFragmentManager(), "");

      } else if (title.equals(getString(R.string.sharing))) {
        GoogleAnalyticsTracker.trackScreen(MainActivity.this, ScreenName.SHARING);
        sharingApplication();
      }
    }

    private void sharingApplication() {

      startActivity(new Intent().setAction(Intent.ACTION_SEND).putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name))
        .putExtra(Intent.EXTRA_TEXT, getString(R.string.sharedText) + " " + GOOGLEPLAY_LINK).setType("text/plain"));
    }
  }

  private void refreshDisplayName() {
    final UsedServers sqdo = UsedServersUtil.getUsedServers(this);

    if (Strings.isNullOrEmpty(sqdo.getLastUsedDisplayName())) {
      actionBarTitleTextView.setText(getString(R.string.app_name));
    }
    else {
      actionBarTitleTextView.setText(sqdo.getLastUsedDisplayName());
    }
  }

  @Override
  public void onBackPressed() {
    LogUtil.d("MainActivity", "onBackPressed");

    final Fragment currentFragment = (Fragment) getSupportFragmentManager().findFragmentById(R.id.content_frame);
    if (currentFragment == null) {
      super.onBackPressed();
    }
    else {
      goHomeScreen(currentFragment);
    }
  }

  public void goHomeScreen(Fragment fragmentToRemove) {
    getSupportFragmentManager().beginTransaction().remove(fragmentToRemove).commit();
    refreshDisplayName();
    actionBarTitleTextView.setOnClickListener(new ActionBarCustomTitleOnClickListener(getSupportFragmentManager()));
    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
    viewPager.setVisibility(View.VISIBLE);
    viewPager.setCurrentItem(0);
    actionBar.setSelectedNavigationItem(0);
  }

  @Override
  public void onResume() {
    super.onResume();
    LogUtil.d("MainActivity", "onResume");
    refreshDisplayName();
  }

  @Override
  public void onStart() {
    super.onStart();
    LogUtil.d("MainActivity", "onStart");

    GoogleAnalyticsTracker.startTracking(this);
  }

  @Override
  public void onStop() {
    super.onStop();
    LogUtil.d("MainActivity", "onStop");
    GoogleAnalyticsTracker.stopTracking(this);
  }

  @Override
  public void onDestroy() {
    super.onDestroy();
    LogUtil.d("MainActivity", "onDestroy");
  }

  @Override
  public void onPause() {
    super.onPause();
    LogUtil.d("MainActivity", "onPause");
  }

}
