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

import hu.balazsbakai.sq.pojo.Plugin;
import hu.balazsbakai.sq.pojo.Project;
import hu.balazsbakai.sq.pojo.ServerStatus;
import hu.balazsbakai.sq.pojo.Tag;
import hu.balazsbakai.sq.pojo.User;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class JsonUtil {

  private static JsonUtil instance = null;

  protected JsonUtil() {
  }

  public static JsonUtil getInstance() {
    if (instance == null) {
      instance = new JsonUtil();
    }
    return instance;
  }

  public ServerStatus processServerStatusData(String data) {
    LogUtil.d("JsonUtil", "processServerStatusData");

    ServerStatus ss = new ServerStatus();

    try {
      JSONObject jss = new JSONObject(data);

      if (!jss.isNull(Tag.ID.getValue())){
        ss.setId(jss.getString(Tag.ID.getValue()));
      }

      if (!jss.isNull(Tag.VERSION.getValue())){
        ss.setVersion(jss.getString(Tag.VERSION.getValue()));
      }

      if (!jss.isNull(Tag.STATUS.getValue())){
        ss.setStatus(jss.getString(Tag.STATUS.getValue()));
      }
    } catch (JSONException e) {
      LogUtil.e("JSONException", e);
    }
    return ss;
  }

  public List<Project> processProjectsData(String data) {
    LogUtil.d("JsonUtil", "processProjectsData");

    List<Project> sqps = new ArrayList<Project>();

    try {

      JSONArray projects = new JSONArray(data);
      for (int i = 0; i < projects.length(); i++) {

        Project sqp = new Project();
        JSONObject project = projects.getJSONObject(i);

        if (!project.isNull(Tag.ID.getValue())){
          sqp.setId(project.getString(Tag.ID.getValue()));
        }

        if (!project.isNull(Tag.KEY.getValue())){
          sqp.setKey(project.getString(Tag.KEY.getValue()));
        }

        if (!project.isNull(Tag.NAME.getValue())){
          sqp.setName(project.getString(Tag.NAME.getValue()));
        }

        if (!project.isNull(Tag.LANG.getValue())){
          sqp.setLang(project.getString(Tag.LANG.getValue()));
        }

        if (!project.isNull(Tag.VERSION.getValue())){
          sqp.setVersion(project.getString(Tag.VERSION.getValue()));
        }

        if (!project.isNull(Tag.DATE.getValue())){
          sqp.setDate(project.getString(Tag.DATE.getValue()));
        }

        sqps.add(sqp);
      }

    } catch (JSONException e) {
      LogUtil.e("JSONException", e);
    }
    return sqps;
  }

  public List<User> processUsersData(String data) {
    LogUtil.d("JsonUtil", "processUsersData");

    List<User> us = new ArrayList<User>();
    try {

      JSONObject obj = new JSONObject(data);
      JSONArray users = obj.getJSONArray("users");

      for (int i = 0; i < users.length(); i++) {

        User u = new User();
        JSONObject user = users.getJSONObject(i);

        if (!user.isNull(Tag.LOGIN.getValue())){
          u.setLogin(user.getString(Tag.LOGIN.getValue()));
        }

        if (!user.isNull(Tag.ACTIVE.getValue())){
          u.setActive(user.getString(Tag.ACTIVE.getValue()));
        }

        if (!user.isNull(Tag.NAME.getValue())){
          u.setName(user.getString(Tag.NAME.getValue()));
        }

        if (!user.isNull(Tag.EMAIL.getValue())){
          u.setEmail(user.getString(Tag.EMAIL.getValue()));
        }

        us.add(u);
      }
    } catch (JSONException e) {
      LogUtil.e("JSONException", e);
    }
    return us;
  }

  public List<Plugin> processPluinData(String data) {
    LogUtil.d("JsonUtil", "processPluinData");

    List<Plugin> sqps = new ArrayList<Plugin>();
    try {

      JSONArray plugins = new JSONArray(data);

      for (int i = 0; i < plugins.length(); i++) {

        Plugin sqp = new Plugin();
        JSONObject project = plugins.getJSONObject(i);

        if (!project.isNull(Tag.KEY.getValue())){
          sqp.setKey(project.getString(Tag.KEY.getValue()));
        }

        if (!project.isNull(Tag.NAME.getValue())){
          sqp.setName(project.getString(Tag.NAME.getValue()));
        }

        if (!project.isNull(Tag.VERSION.getValue())){
          sqp.setVersion(project.getString(Tag.VERSION.getValue()));
        }

        sqps.add(sqp);
      }
    } catch (JSONException e) {
      LogUtil.e("JSONException", e);
    }
    return sqps;
  }

}
