package net.thesocialuniverse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.io.IOException;

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

    DownloadManager(){
        httpclient = new DefaultHttpClient();
        HttpParams params = httpclient.getParams();
        HttpConnectionParams.setConnectionTimeout(params, 1000 * 60 * 5);
        HttpConnectionParams.setSoTimeout(params, 1000 * 60 * 5);
    }



//    public boolean imageResources(IssueContext context){
//        boolean status = true;
//        status = downloads(context.getThumbImageList(), context.getJiwonNm(), context.getSaNo(), status);
//        status = downloads(context.getDetailImageList(), context.getJiwonNm(), context.getSaNo(), status);
//        return status;
//    }

    //    private boolean downloads(List<String> thumbImageList, String jiwonNm, String saNo, boolean status) {
//        for (String url : thumbImageList) {
//            try {
//                String path = CrawlerUtils.getFilePath(jiwonNm, saNo) + File.separator + getFileName(url);
//                download(url, path);
//            } catch (Exception e) {
//                logger.warn("Fail to Image download : " + url, e);
//                status = false;
//            }
//        }
//        return status;
//    }
//
//    private static String getFileName(String url) {
//        String pattern = "downloadfilename=";
//        return StringUtils.substring(url, StringUtils.indexOf(url, "downloadfilename=") + pattern.length());
//    }
//
//    public boolean pdf(IssueContext context){
//        boolean status = true;
//        String url = context.get(CrawlerContext.GamEvalSeo_FIELD);
//        String path = CrawlerUtils.getFilePath(context.getJiwonNm(), context.getSaNo()) + File.separator + context.getSaNo() + ".pdf";
//
//        try {
//            //download(url, path);
//            AdHocHttpUtil.download(url, path);
//        } catch (Exception e) {
//            logger.warn("Fail to PDF download : " + url, e);
//            return false;
//        }
//        return status;
//    }
//
//    private void download(String url, String path) throws ClientProtocolException, IOException {
//        File f = new File(path);
//        if (!f.exists() || f.length() == 0 || FileUtils.readFileToString(f).startsWith("<!DOCTYPE")) {
//            url = url.replaceAll("&amp;", "&");
//
//            HttpPost httpget = new HttpPost(url);
//            HttpResponse response = httpclient.execute(httpget);
//            int status = response.getStatusLine().getStatusCode();
//
//            if (status == 200) {
//                HttpEntity entity = response.getEntity();
//                FileUtils.writeByteArrayToFile(f, IOUtils.toByteArray(entity.getContent()));
//            }else{
//                throw new IllegalArgumentException("[" + status + "] Fail to Download : " + url);
//            }
//
//            logger.debug("url : " + url);
//            logger.debug("path : " + f.getAbsoluteFile());
//        }
//    }


    final String pattern = "http://google.com/complete/search?output=toolbar&q=";
    public String getResponse(String keyword) throws IOException {
        String url = pattern + keyword;

        HttpGet httpget = new HttpGet(url);
        HttpResponse response = httpclient.execute(httpget);
        status = response.getStatusLine().getStatusCode();

        HttpEntity entity = response.getEntity();
        String content = IOUtils.toString(entity.getContent());

        if (status == 200) {
            return content;
        }else{
            logger.fatal(content);
            throw new IllegalArgumentException("[" + status + "] Fail to keyword : " + keyword);
        }
    }

    public int status() {
        return status;
    }
}