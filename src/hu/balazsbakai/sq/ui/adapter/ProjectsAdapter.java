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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import hu.balazsbakai.sq.R;
import hu.balazsbakai.sq.pojo.Project;
import hu.balazsbakai.sq.pojo.Server;
import hu.balazsbakai.sq.util.CommonUtil;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker;
import hu.balazsbakai.sq.util.GoogleAnalyticsTracker.EventLabel;

import java.util.List;

public class ProjectsAdapter extends BaseAdapter {

  private static final String PROJECT_URL_PART = "/dashboard/index/";

  private LayoutInflater mInflater = null;
  private List<Project> sonarQubeProjects;
  private Server sonarQubeServer;
  private Context mContext;

  private final class ProjectsViewHolder {
    TextView projectName;
    TextView projectVersion;
    TextView projectLanguage;
    TextView projectDate;
    // ImageView favourite;
    ImageView web;
    LinearLayout projectInfoLayout;
  }

  private ProjectsViewHolder mHolder = null;

  public ProjectsAdapter(Context context, List<Project> sonarQubeProjects, Server sonarQubeServer) {
    this.mContext = context;
    this.sonarQubeProjects = sonarQubeProjects;
    this.sonarQubeServer = sonarQubeServer;

    if (mContext != null)
      mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  @Override
  public int getCount() {
    return sonarQubeProjects.size();
  }

  @Override
  public Object getItem(int position) {
    return sonarQubeProjects.get(position);
  }

  @Override
  public long getItemId(int position) {
    return position;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    if (convertView == null) {
      mHolder = new ProjectsViewHolder();
      convertView = mInflater.inflate(R.layout.project_item, null);
      convertView.setTag(mHolder);
    } else {
      mHolder = (ProjectsViewHolder) convertView.getTag();
    }

    mHolder.projectName = (TextView) convertView.findViewById(R.id.projectName);
    mHolder.projectName.setText(sonarQubeProjects.get(position).getName());

    mHolder.projectVersion = (TextView) convertView.findViewById(R.id.projectVersion);
    mHolder.projectVersion.setText(sonarQubeProjects.get(position).getVersion());

    mHolder.projectLanguage = (TextView) convertView.findViewById(R.id.projectLanguage);
    mHolder.projectLanguage.setText(sonarQubeProjects.get(position).getLang());

    mHolder.projectDate = (TextView) convertView.findViewById(R.id.projectDate);
    mHolder.projectDate.setText(CommonUtil.formateDateFromstring(sonarQubeProjects.get(position).getDate()));

    mHolder.web = (ImageView) convertView.findViewById(R.id.imageViewWeb);
    mHolder.web.setTag(position);
    mHolder.web.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        GoogleAnalyticsTracker.trackUIEvent((Activity) mContext, EventLabel.PROJECTS_ADAPTER_WEB);
        mContext.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(sonarQubeServer.getServerURL() + PROJECT_URL_PART + sonarQubeProjects.get(position).getId())));
      }
    });

    mHolder.projectInfoLayout = (LinearLayout) convertView.findViewById(R.id.projectInfoLayout);
    mHolder.projectInfoLayout.setTag(position);
    mHolder.projectInfoLayout.setOnClickListener(new OnClickListener() {
      @Override
      public void onClick(View v) {
        /*
         * Bundle bundle=new Bundle();
         * bundle.putSerializable("project", sonarQubeProjects.get(position));
         * bundle.putSerializable("server", sonarQubeServer);
         *
         * FragmentProjectDetails fpm=new FragmentProjectDetails();
         * fpm.setArguments(bundle);
         * ((hu.balazsbakai.sqm.MainActivity)mContext).getSupportFragmentManager().beginTransaction().replace(R.id.content_frame,
         * fpm).commit();
         */
      }
    });

    /*
     * Needed for the favourite button
     * mHolder.web.setOnTouchListener(new OnTouchListener() {
     *
     * @Override
     * public boolean onTouch(View v, MotionEvent event) {
     * switch (event.getAction()) {
     * case MotionEvent.ACTION_DOWN: {
     * Drawable d=v.getResources().getDrawable(R.drawable.favourite);
     * Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
     * ((ImageView)v).setImageBitmap(bitmap);
     * break;
     * }
     * case MotionEvent.ACTION_CANCEL:{
     * Drawable d=v.getResources().getDrawable(R.drawable.web_site);
     * Bitmap bitmap = ((BitmapDrawable)d).getBitmap();
     * ((ImageView)v).setImageBitmap(bitmap);
     * break;
     * }
     * }
     * return true;
     * }
     * });
     */

    return convertView;
  }

}
