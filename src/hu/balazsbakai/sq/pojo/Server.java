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

import java.io.Serializable;

public class Server implements Serializable, Comparable<Server> {

  private static final long serialVersionUID = 3L;

  private String serverURL;

  private String displayName; // must be unique

  private String username;

  private String password;

  private transient boolean checked;

  public Server withDisplayName(String displayName) {
    this.displayName = displayName;
    return this;
  }

  public Server withserverURL(String serverURL) {
    this.serverURL = serverURL;
    return this;
  }

  public Server withUsernameAndPassword(String username, String password) {
    this.username = username;
    this.password = password;
    return this;
  }

  public Server() {
  }

  public Server(String serverURL, String displayName) {
    this.displayName = displayName;
    this.serverURL = serverURL;
  }

  public Server(String displayName) {
    this.displayName = displayName;
  }

  public boolean isChecked() {
    return checked;
  }

  public void setChecked(boolean checked) {
    this.checked = checked;
  }

  public String getServerURL() {
    return serverURL;
  }

  public void setServerURL(String serverURL) {
    this.serverURL = serverURL;
  }

  public String getDisplayName() {
    return displayName;
  }

  public void setDisplayName(String displayName) {
    this.displayName = displayName;
  }

  public String getUsername() {
    return username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj)
      return true;
    if ((obj == null) || (obj.getClass() != this.getClass()))
      return false;

    return displayName.equals(((Server) obj).displayName);
  }

  @Override
  public int hashCode() {
    int hash = 7;
    hash = 31 * hash;
    hash = 31 * hash + (null == displayName ? 0 : displayName.hashCode());
    return hash;
  }

  @Override
  public int compareTo(Server another) {
    return this.displayName.compareTo(another.displayName);
  }

}
