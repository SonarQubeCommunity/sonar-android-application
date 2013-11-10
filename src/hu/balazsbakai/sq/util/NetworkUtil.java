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
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import hu.balazsbakai.sq.pojo.Server;

public class NetworkUtil {

  public static final String REPONSE_FORMAT = "application/json";
  public static final String SERVER_STATUS = "/api/server";
  public static final String SERVER_PROJECTS = "/api/resources";
  public static final String SERVER_USERS = "/api/users/search"; // Available since Version 3.6
  public static final String SERVER_PLUGINS = "/api/updatecenter/installed_plugins";
  public static final String PROJECT_METRICS = "/api/metrics";

  private static NetworkUtil instance = null;

  protected NetworkUtil() {

  }

  public static NetworkUtil getInstance() {
    if (instance == null) {
      instance = new NetworkUtil();
    }
    return instance;
  }

  public boolean checkConnectionAndGetData(Handler handler, Context context, Server server, String type) {
    LogUtil.d("NetworkUtil", "checkConnectionAndGetData");

    if (isOnline(context)) {
      startWorkingThread(handler, server, type);
      return true;
    } else {
      return false;
    }
  }

  private void startWorkingThread(final Handler handler, final Server server, final String type) {
    LogUtil.d("NetworkUtil", "startWorkingThread");

    Thread thread = new Thread() {
      @Override
      public void run() {
        Message responseMessage = handler.obtainMessage();
        try {
          Bundle bundle = new Bundle();
          bundle.putString("displayName", server.getDisplayName());
          responseMessage.setData(bundle);
          responseMessage.obj = readData(server, type);
          handler.sendMessage(responseMessage);
        } catch (Exception e) {
          LogUtil.e("startWorkingThread", e);
        }
      }
    };
    thread.start();
  }

  private String readData(Server server, String url) {
    LogUtil.d("NetworkUtil", "readData");

    try {
      RestUtil client = new RestUtil(server.getServerURL() + url);

      String credentials = server.getUsername() + ":" + server.getPassword();
      String base64EncodedCredentials = Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);

      client.AddHeader("Authorization", "Basic " + base64EncodedCredentials);
      client.AddHeader("Accept", REPONSE_FORMAT);
      client.executePOST();

      return client.getResponse();

    } catch (Exception e) {
      LogUtil.e("readDataException", e);
      return null;
    }
  }

  private boolean isOnline(Context context) {
    ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
    NetworkInfo netInfo = cm.getActiveNetworkInfo();
    if (netInfo != null && netInfo.isConnectedOrConnecting()) {
      return true;
    }
    return false;
  }

}
