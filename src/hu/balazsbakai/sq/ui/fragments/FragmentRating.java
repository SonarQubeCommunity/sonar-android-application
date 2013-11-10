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
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.Button;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.util.UsageTracker;
import hu.balazsbakai.sq.util.UsageTracker.EventLabel;
import hu.balazsbakai.sq.util.RatingUtil;

public class FragmentRating extends DialogFragment implements View.OnClickListener {

  private Button rateButton;
  private Button remindMeLaterButton;
  private Button noThanksButton;

  @Override
  public Dialog onCreateDialog(Bundle savedInstanceState) {

    View v = getActivity().getLayoutInflater().inflate(R.layout.rating, null);

    rateButton = (Button) v.findViewById(R.id.rateButton);
    rateButton.setOnClickListener(this);
    remindMeLaterButton = (Button) v.findViewById(R.id.remindMeLaterButton);
    remindMeLaterButton.setOnClickListener(this);
    noThanksButton = (Button) v.findViewById(R.id.noThanksButton);
    noThanksButton.setOnClickListener(this);

    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
    builder.setTitle(getString(R.string.rating));
    builder.setView(v);
    return builder.create();
  }

  @Override
  public void onClick(View v) {
    if (v.getTag().equals("RATE")) {
      UsageTracker.getInstance().trackUIEvent(getActivity(), EventLabel.RATING_RATE);
      startActivity(new Intent(Intent.ACTION_VIEW, RatingUtil.getInstance().getRatingURI()));
      dismiss();
    } else if (v.getTag().equals("REMINDMELATER")) {
      UsageTracker.getInstance().trackUIEvent(getActivity(), EventLabel.RATING_REMIND_ME_LATER);
      dismiss();
    } else {
      UsageTracker.getInstance().trackUIEvent(getActivity(), EventLabel.RATING_NO_THANKS);
      RatingUtil.getInstance().disableRatingDialog(getActivity());
      dismiss();
    }
  }

}
