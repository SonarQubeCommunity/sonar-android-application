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

package hu.balazsbakai.sq.pojo;

import hu.balazsbakai.sq.util.LogUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UsedServers implements Serializable {

  private static final long serialVersionUID = 3L;

  private String lastUsedDisplayName;

  private List<Server> servers = new ArrayList<Server>();

  public List<Server> getServers() {
    Collections.sort(servers);
    return servers;
  }

  public void setServers(List<Server> servers) {
    this.servers = servers;
  }

  public String getLastUsedDisplayName() {
    return lastUsedDisplayName;
  }

  public void setLastUsedDisplayName(String lastUsedDisplayName) {
    this.lastUsedDisplayName = lastUsedDisplayName;
  }

  public Server getLastUsedServer() {
    int index = servers.indexOf(new Server(lastUsedDisplayName));
    LogUtil.d("getLastUsedServer index", String.valueOf(index));

    if (index == -1) {
      return null;
    } else {
      return servers.get(index);
    }

  }

}
