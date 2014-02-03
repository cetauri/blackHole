package net.thesocialuniverse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cetauri
 * Date: 13. 5. 19.
 * Time: 오후 4:25
 * To change this template use File | Settings | File Templates.
 */

public class DownloadManager {
    private Log logger = LogFactory.getLog(getClass());

    private static DownloadManager manager;
    private HttpClient httpclient;
    int status;

    public static DownloadManager getInstance(){
        if (manager == null) {
            synchronized(DownloadManager.class) {
                manager = new DownloadManager();
            }
        }
        return manager;
    }

    public static DownloadManager reset(){
        synchronized(DownloadManager.class) {
            System.gc();
            System.gc();
            manager = null;

            manager = new DownloadManager();
        }
        return manager;
    }

    DownloadManager(){
        httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 1000 * 60 * 5);
        HttpConnectionParams.setSoTimeout(params, 1000 * 60 * 5);
    }

    final String pattern = "http://google.com/complete/search";
    public String getResponse(String keyword) throws IOException {

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("output", "toolbar"));
        params.add(new BasicNameValuePair("q", keyword));
        String query = URLEncodedUtils.format(params, "UTF-8");
        String url = pattern + "?" + query;

        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        status = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String content = IOUtils.toString(entity.getContent(), "ISO-8859-1");
        content = new String(content.getBytes(), "UTF-8");

//        System.out.println(keyword);
//        System.out.println(url);
//        System.out.println(content);
//        System.out.println(entity.getContentEncoding());

        if (status == 200) {
            return content;
        }else{
            logger.fatal("["+keyword+"]" + content);

            if (StringUtils.contains(content, "Your client has issued a malformed or illegal request.")) {
                throw new IllegalArgumentException("Your client has issued a malformed or illegal request. : " + keyword);
            }
            throw new IllegalStateException("[" + status + "] Fail to keyword : " + keyword);
        }
    }

    public int status() {
        return status;
    }
}