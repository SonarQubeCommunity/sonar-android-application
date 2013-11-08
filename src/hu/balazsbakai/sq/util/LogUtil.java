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

import android.util.Log;

import java.io.PrintWriter;
import java.io.StringWriter;

public class LogUtil {

  private static final boolean LOGGING_ENABLED = true; // false to disable logging

  public static void i(String tag, String message) {
    if (LOGGING_ENABLED) {
      Log.i(tag, message);
    }
  }

  public static void i(String tag, String message, String parameter1) {
    if (LOGGING_ENABLED) {
      Log.i(tag, new StringBuilder(message).append(": ").append(parameter1).toString().toString());
    }
  }

  public static void d(String tag, String message) {
    if (LOGGING_ENABLED) {
      Log.d(tag, message);
    }
  }

  public static void d(String tag, String message, String parameter1) {
    if (LOGGING_ENABLED) {
      Log.d(tag, new StringBuilder(message).append(": ").append(parameter1).toString().toString());
    }
  }

  public static void e(String message, Exception e) {
    if (LOGGING_ENABLED) {
      Log.e("EXCEPTION", new StringBuilder(message).append(": ").append(e.getMessage()).toString().toString());

      StringWriter stack = new StringWriter();
      e.printStackTrace(new PrintWriter(stack));
      Log.e("StackTrace", stack.toString());
    }
  }

}
