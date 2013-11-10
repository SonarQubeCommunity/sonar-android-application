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

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Server;
import hu.balazsbakai.sq.util.UsageTracker;
import hu.balazsbakai.sq.util.UsageTracker.EventLabel;
import hu.balazsbakai.sq.util.UsedServersUtil;

import java.util.ArrayList;
import java.util.List;

public class FragmentServerListDialog extends DialogFragment {

  private TextView actionBarTitleTextView;
  private ViewPager viewPager;

  @Override
  public void onResume() {
    actionBarTitleTextView = (TextView) ((ActionBarActivity) getActivity()).getSupportActionBar().getCustomView().findViewById(R.id.actionBarTitle);
    viewPager = (ViewPager) getActivity().findViewById(R.id.viewpager);
    super.onResume();
  }

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    UsageTracker.getInstance().trackUIEvent(getActivity(), EventLabel.SERVER_LIST_DIALOG);

    final List<String> displayNames = new ArrayList<String>();

    for (Server item : UsedServersUtil.getUsedServers(getActivity()).getServers()){
      displayNames.add(item.getDisplayName());
    }

    final CharSequence[] charSequenceItems = displayNames.toArray(new CharSequence[displayNames.size()]);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle("SonarQube Servers");
    builder.setItems(charSequenceItems, new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {

        actionBarTitleTextView.setText(displayNames.get(which));
        UsedServersUtil.updateLastUsedDisplayName(getActivity(), displayNames.get(which));
        viewPager.getAdapter().notifyDataSetChanged();
      }
    });
    return builder.create();
  }

}
