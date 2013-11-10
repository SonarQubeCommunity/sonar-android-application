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

package hu.balazsbakai.sq.ui.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Server;
import hu.balazsbakai.sq.util.UsedServersUtil;

import java.util.ArrayList;

public class AddPublicServersAdapter extends BaseAdapter {
  private LayoutInflater mInflater = null;
  private ArrayList<Server> sonarQubeServers;

  private final class AutoPopulateViewHolder {
    private TextView serverURL;
    private CheckBox checkBox;
  }

  private AutoPopulateViewHolder mHolder = null;

  public AddPublicServersAdapter(Context context) {
    Context mContext = context;
    mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    sonarQubeServers = new ArrayList<Server>(); // http://www.sonarqube.org/resources/public-sonarqube-instances/

    sonarQubeServers.add(new Server("http://nemo.sonarqube.org:80", "nemo"));
    sonarQubeServers.add(new Server("https://analysis.apache.org:443", "asf"));
    sonarQubeServers.add(new Server("https://dev.eclipse.org:443/sonar", "eclipse"));
    sonarQubeServers.add(new Server("https://sonar.springsource.org:443", "springsource"));
    sonarQubeServers.add(new Server("https://sonar-ci.codehaus.org:443", "codehause"));
    sonarQubeServers.add(new Server("http://sonar.xwiki.org:80", "xwiki"));
    sonarQubeServers.add(new Server("http://sonar.gbif.org", "gbif"));
    sonarQubeServers.add(new Server("https://sonar.nuxeo.org", "nuxeo"));
    sonarQubeServers.add(new Server("http://sonar.chorem.org:80/sonar", "chorem"));
    sonarQubeServers.add(new Server("http://integration.silverpeas.org/sonar", "silverpea"));
    sonarQubeServers.add(new Server("http://hudson.codelutin.com/sonar", "codelutin"));
    sonarQubeServers.add(new Server("http://sonar.nuiton.org/sonar", "nuiton"));
    sonarQubeServers.add(new Server("https://sonar.exoplatform.org:443", "exoplatform"));
    // sonarQubeServers.add(new Server("https://www.tinyjee.org:443/sonar", "tinyjee")); // timeout
    // sonarQubeServers.add(new Server("https://metrics.typo3.org:443", "typo3")); // out of memory was before
    // sonarQubeServers.add(new Server("http://sonar.dictat.org", "dictat")); // not exists

    sonarQubeServers.removeAll(UsedServersUtil.getUsedServers((FragmentActivity) context).getServers());

  }

  @Override
  public int getCount() {
    return sonarQubeServers.size();
  }

  @Override
  public Object getItem(int position) {
    return sonarQubeServers.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      mHolder = new AutoPopulateViewHolder();
      convertView = mInflater.inflate(R.layout.add_public_servers_item, null);
      convertView.setTag(mHolder); // !!!
    } else {
      mHolder = (AutoPopulateViewHolder) convertView.getTag();
    }

    mHolder.serverURL = (TextView) convertView.findViewById(R.id.autoPopulateServerURL);
    mHolder.serverURL.setText(sonarQubeServers.get(position).getServerURL());

    mHolder.checkBox = (CheckBox) convertView.findViewById(R.id.autoPopulateCheckBox);
    mHolder.checkBox.setTag(position);
    mHolder.checkBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {

      @Override
      public void onCheckedChanged(CompoundButton checkboxView, boolean isChecked) {
        sonarQubeServers.get(position).setChecked(isChecked);
      }
    });

    mHolder.checkBox.setChecked(sonarQubeServers.get(position).isChecked());
    return convertView;
  }

}
