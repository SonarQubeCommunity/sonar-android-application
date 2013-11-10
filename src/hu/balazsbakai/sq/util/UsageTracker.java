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
import com.google.analytics.tracking.android.EasyTracker;
import com.google.analytics.tracking.android.Fields;
import com.google.analytics.tracking.android.MapBuilder;

public class UsageTracker {

  private static UsageTracker instance = null;

  protected UsageTracker() {
  }

  public static UsageTracker getInstance() {
    if (instance == null) {
      instance = new UsageTracker();
    }
    return instance;
  }

  public enum EventCategory {
    UI_EVENT;
  }

  public enum EventAction {
    BUTTON_PRESS,
  }

  public enum EventLabel {
    USERS_ADAPTER_EMAIL,
    PROJECTS_ADAPTER_WEB,
    DONATION_DONATE,
    RATING_RATE,
    RATING_REMIND_ME_LATER,
    RATING_NO_THANKS,
    ADD_PUBLIC_SERVERS_ADD,
    SERVER_LIST_DIALOG,
    ADD_NEW_SERVER_TEST_CONNECTION,
    ADD_NEW_SERVER_SAVE,
    LIST_SERVER_SELECT,
    LIST_SERVER_DELETE;
  }

  public enum ScreenName {
    ADD_NEW_SERVER,
    LIST_SERVERS,
    ADD_PUBLIC_SERVERS,
    SHARING,
    RATING,
    DONATION,
  }

  /**
   * Add the send methods to the onStart() and onStop() methods of each of your Activities
   */
  public void startTracking(Activity activity) {
    EasyTracker.getInstance(activity).activityStart(activity);
  }

  /**
   * Add the send methods to the onStart() and onStop() methods of each of your Activities
   */
  public void stopTracking(Activity activity) {
    EasyTracker.getInstance(activity).activityStop(activity);
  }

  public void trackScreen(Activity activity, ScreenName screenName) {
    EasyTracker.getInstance(activity).send(MapBuilder.createAppView().set(Fields.SCREEN_NAME, screenName.name()).build());
  }

  public void trackUIEvent(Activity activity, EventLabel eventLabel) {
    EasyTracker.getInstance(activity).send(MapBuilder.createEvent(EventCategory.UI_EVENT.name(), EventAction.BUTTON_PRESS.name(), eventLabel.name(), null).build());
  }

}
