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

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CommonUtil {

  private static final String INPUT_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss";
  private static final String OUTPUT_DATE_FORMAT = "yyyy-MM-dd hh:ss";

  private static CommonUtil instance = null;

  protected CommonUtil() {
  }

  public static CommonUtil getInstance() {
    if (instance == null) {
      instance = new CommonUtil();
    }
    return instance;
  }

  public void hideKeyBoard(Activity activity) {
    InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
    inputManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
  }

  public String formateDateFromstring(String inputDate) {

    Date parsed = null;
    String outputDate = "";

    SimpleDateFormat df_input = new SimpleDateFormat(INPUT_DATE_FORMAT, java.util.Locale.getDefault());
    SimpleDateFormat df_output = new SimpleDateFormat(OUTPUT_DATE_FORMAT, java.util.Locale.getDefault());

    try {
      parsed = df_input.parse(inputDate);
      outputDate = df_output.format(parsed);

    } catch (ParseException e) {
      LogUtil.e("ParseException", e);
    }

    return outputDate;

  }

}
