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
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import com.google.gson.Gson;
import hu.balazsbakai.sq.pojo.Server;
import hu.balazsbakai.sq.pojo.UsedServers;

public class UsedServersUtil {

  private static final String PREF_NAME = "SONARQUBE_SERVER_PREFERENCES";
  private static final String USED_SERVERS = "USED_SERVERS";

  private static UsedServers usedServers;

  public static UsedServers getUsedServers(Activity activity) {
    if (usedServers == null) {
      updateUsedServers(activity);
    }
    return usedServers;
  }

  public static boolean isUniqueDisplayName(String displayName) {
    return !usedServers.getServers().contains(new Server(displayName));
  }

  public static void updateLastUsedDisplayName(Activity activity, String lastUsedDisplayName) {
    UsedServers us = getUsedServers(activity);
    us.setLastUsedDisplayName(lastUsedDisplayName);
    writeUsedServerData(activity, new Gson().toJson(us));
    usedServers = us;
  }

  public static void saveNewServer(Activity activity, String serverURL, String displayName, String userName, String password) throws Exception {

    UsedServers us = getUsedServers(activity);
    us.setLastUsedDisplayName(displayName);
    us.getServers().add(new Server().withserverURL(serverURL).withDisplayName(displayName).withUsernameAndPassword(userName, password));
    writeUsedServerData(activity, new Gson().toJson(us));
    usedServers = us;
  }

  public static void cleanUsedServers(Activity activity) {
    writeUsedServerData(activity, "");
  }

  public static void deleteServer(FragmentActivity activity, String displayName) {
    UsedServers us = getUsedServers(activity);
    us.getServers().remove(new Server(displayName));
    writeUsedServerData(activity, new Gson().toJson(us));
    usedServers = us;
  }

  private static void updateUsedServers(Activity activity) {
    String data = readUsedServerData(activity);
    if (!"".equals(data)) {
      usedServers = new Gson().fromJson(data, UsedServers.class);
    } else {
      usedServers = new UsedServers();
    }
  }

  private static void writeUsedServerData(Activity activity, String data) {
    LogUtil.i("UsedServersUtil.writeUsedServerData", data);

    SharedPreferences settings = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = settings.edit();
    editor.putString(USED_SERVERS, data);
    editor.commit();
  }

  private static String readUsedServerData(Activity activity) {
    SharedPreferences settings = activity.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    String data = settings.getString(USED_SERVERS, "");
    LogUtil.i("UsedServersUtil.readUsedServerData", data);
    return data;
  }

}
