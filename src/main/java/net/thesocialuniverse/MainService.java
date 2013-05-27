package net.thesocialuniverse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.net.URLEncoder;

@Component
public class MainService {
    static Log logger = LogFactory.getLog(Class.class.getClass());

    @Autowired
    CSVLoader csvLoader;

    static int count = 481;

    public void start() {

        logger.info("Application start");

//                System.out.println(csvLoader.get(0));

        for (int i = 0 ;i<csvLoader.length();i++){

            try {
                gathering(csvLoader.get(i));
            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                try {
                    Thread.sleep(1000*60 *1);      //638  365
                } catch (Exception e1){}
            }
        }
//        while(true){
//        }
    }

    private void gathering(String keyword) throws IOException, ParserConfigurationException, SAXException {
        DownloadManager manager = DownloadManager.getInstance();
        String response = manager.getResponse(URLEncoder.encode(keyword, "UTF-8"));

        logger.info("keyword : " + keyword);
        logger.info(response);

        if(manager.status() == 200){
            count++;
        } else{
            logger.fatal("[" + manager.status() + "] " + response);
        }
    }
}
