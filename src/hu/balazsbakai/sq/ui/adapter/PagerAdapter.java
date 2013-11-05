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

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import hu.balazsbakai.sq.ui.fragments.FragmentPlugins;
import hu.balazsbakai.sq.ui.fragments.FragmentProjects;
import hu.balazsbakai.sq.ui.fragments.FragmentUsers;

public class PagerAdapter extends FragmentPagerAdapter {

  private Fragment projects;
  private Fragment issues;
  private Fragment profiles;

  public PagerAdapter(FragmentManager fm) {
    super(fm);
  }

  @Override
  public Fragment getItem(int i) {

    if (i == 0) {
      if (projects == null)
        projects = new FragmentProjects();

      return projects;
    }
    if (i == 1) {
      if (issues == null)
        issues = new FragmentUsers();

      return issues;
    }
    if (i == 2) {
      if (profiles == null)
        profiles = new FragmentPlugins();
      return profiles;
    } else return null;

  }

  @Override
  public int getCount() {
    return 3;
  }

  @Override
  public int getItemPosition(Object object) {
    return POSITION_NONE;
  }

}
