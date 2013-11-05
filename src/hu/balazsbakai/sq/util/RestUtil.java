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

import com.google.common.io.CharStreams;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.security.KeyStore;
import java.util.ArrayList;

public class RestUtil {

  private final int CONNECTION_TIMEOUT = 4000;
  private final int SOCKET_TIMEOUT = 8000;

  private ArrayList<NameValuePair> parameters;
  private ArrayList<NameValuePair> headers;

  private int responseCode;
  private String url;
  private String message;
  private String response;

  public RestUtil(String url) {
    this.url = url;
    this.parameters = new ArrayList<NameValuePair>();
    this.headers = new ArrayList<NameValuePair>();
  }

  public void executePOST() throws Exception {
    HttpPost request = new HttpPost(url);

    for (NameValuePair h : headers) {
      request.addHeader(h.getName(), h.getValue());
    }

    if (!parameters.isEmpty()) {
      request.setEntity(new UrlEncodedFormEntity(parameters, HTTP.UTF_8));
    }

    executeRequest(request, url);
  }

  public void executeGET() throws Exception {

    String queryStringParams = "";

    if (!parameters.isEmpty()) {
      queryStringParams += "?";
      for (NameValuePair p : parameters) {
        String paramString = p.getName() + "=" + URLEncoder.encode(p.getValue(), "UTF-8");
        if (queryStringParams.length() > 1) {
          queryStringParams += "&" + paramString;
        } else {
          queryStringParams += paramString;
        }
      }
    }

    HttpGet request = new HttpGet(url + queryStringParams);

    for (NameValuePair h : headers) {
      request.addHeader(h.getName(), h.getValue());
    }

    executeRequest(request, url);
  }

  private void executeRequest(HttpUriRequest request, String url) {

    DefaultHttpClient client = getNewTrustedHttpClient();// new DefaultHttpClient();

    HttpResponse httpResponse;

    try {
      httpResponse = client.execute(request);
      responseCode = httpResponse.getStatusLine().getStatusCode();
      message = httpResponse.getStatusLine().getReasonPhrase();

      HttpEntity httpEntity = httpResponse.getEntity();

      if (httpEntity != null) {
        InputStream inputStream = httpEntity.getContent();
        response = convertStreamToString(inputStream);
        inputStream.close();// Closing the input stream will trigger connection release
      }

    } catch (Exception e) {
      LogUtil.e("executeRequest", e);
      client.getConnectionManager().shutdown();
      e.printStackTrace();
    }

  }

  private DefaultHttpClient getNewTrustedHttpClient() {
    try {
      KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
      trustStore.load(null, null);

      SSLSocketFactory sf = new CustomTrustedSSLSocketFactory(trustStore);
      sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

      HttpParams params = new BasicHttpParams();
      HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
      HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);
      HttpConnectionParams.setConnectionTimeout(params, CONNECTION_TIMEOUT);
      HttpConnectionParams.setSoTimeout(params, SOCKET_TIMEOUT);

      SchemeRegistry registry = new SchemeRegistry();
      registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
      registry.register(new Scheme("https", sf, 443));

      ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

      return new DefaultHttpClient(ccm, params);
    } catch (Exception e) {
      LogUtil.e("Exception", e);
      return new DefaultHttpClient();
    }
  }

  private static String convertStreamToString(InputStream is) {
    LogUtil.d("RestClient", "convertStreamToString");

    try {
      return CharStreams.toString(new InputStreamReader(is, "UTF-8"));
    } catch (Exception e) {
      LogUtil.e("convertStreamToString", e);
      e.printStackTrace();
    }
    return "";

  }

  public String getResponse() {
    return response;
  }

  public String getErrorMessage() {
    return message;
  }

  public int getResponseCode() {
    return responseCode;
  }

  public void AddParam(String name, String value) {
    parameters.add(new BasicNameValuePair(name, value));
  }

  public void AddHeader(String name, String value) {
    headers.add(new BasicNameValuePair(name, value));
  }
}
