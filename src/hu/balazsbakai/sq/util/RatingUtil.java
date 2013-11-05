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

package hu.balazsbakai.sq.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import hu.balazsbakai.sq.MainActivity;
import hu.balazsbakai.sq.ui.fragments.FragmentRating;

public class RatingUtil {

  private final static String MARKET_PREFIX = "market://details?id=";
  private final static String APP_PACKAGE_NAME = "hu.balazsbakai.sq";

  private final static long DAYS_UNTIL_PROMPT = 0;
  private final static long LAUNCH_COUNT_WHEN_PROMPT = 50;

  private static final String PREF_NAME = "SONARQUBE_RATING_PREFERENCES";
  private static final String DISABLED_RATING_DIALOG = "DISABLED_RATING_DIALOG";
  private static final String APPLICATION_LAUNCH_COUNT = "APPLICATION_LAUNCH_COUNT";
  private static final String APPLICATION_FIRST_LAUNCH_TIME = "APPLICATION_FIRST_LAUNCH_TIME";

  public static void applicationLaunched(Context mContext) {
    if (isDisabledRatingDialog(mContext)) {
      return;
    }

    incrementApplicationLaunchCounter(mContext);

    if (isSuitableApplicationLaunchCount(mContext)) {
      new FragmentRating().show(((MainActivity) mContext).getSupportFragmentManager(), "");
    }
  }

  public static void disableRatingDialog(Context mContext) {
    SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putBoolean(DISABLED_RATING_DIALOG, true);
    editor.commit();
  }

  public static Uri getRatingURI() {
    return Uri.parse(RatingUtil.MARKET_PREFIX + RatingUtil.APP_PACKAGE_NAME);
  }

  private static boolean isSuitableApplicationLaunchCount(Context mContext) {
    return getApplicationLaunchCounter(mContext) >= LAUNCH_COUNT_WHEN_PROMPT;
  }

  private static boolean isSuitableElapsedDays(Context mContext) {
    return System.currentTimeMillis() >= getFirstApplicationLaunchTime(mContext) + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000);
  }

  private static long getFirstApplicationLaunchTime(Context mContext) {
    SharedPreferences prefs = mContext.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);

    long firstLaunchTime = prefs.getLong(APPLICATION_FIRST_LAUNCH_TIME, 0);

    if (firstLaunchTime == 0) {
      SharedPreferences.Editor editor = prefs.edit();
      editor.putLong(APPLICATION_FIRST_LAUNCH_TIME, System.currentTimeMillis());
      editor.commit();
    }

    return firstLaunchTime;
  }

  private static long getApplicationLaunchCounter(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    return prefs.getLong(APPLICATION_LAUNCH_COUNT, 0);
  }

  private static void incrementApplicationLaunchCounter(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = prefs.edit();
    editor.putLong(APPLICATION_LAUNCH_COUNT, getApplicationLaunchCounter(context) + 1);
    editor.commit();
  }

  private static boolean isDisabledRatingDialog(Context context) {
    SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    return prefs.getBoolean(DISABLED_RATING_DIALOG, false);
  }

}
