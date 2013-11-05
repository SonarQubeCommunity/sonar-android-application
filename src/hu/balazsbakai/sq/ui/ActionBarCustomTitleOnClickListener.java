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

package hu.balazsbakai.sq.ui;

import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.view.View.OnClickListener;
import hu.balazsbakai.sq.ui.fragments.FragmentServerListDialog;

public class ActionBarCustomTitleOnClickListener implements OnClickListener {

  private FragmentManager fm;

  public ActionBarCustomTitleOnClickListener(FragmentManager fm) {
    this.fm = fm;
  }

  public void onClick(View v) {
    if (v.getId() == hu.balazsbakai.sq.R.id.actionBarTitle) {
      DialogFragment dialog = new FragmentServerListDialog();
      dialog.show(fm, "");
    }
  }
}
