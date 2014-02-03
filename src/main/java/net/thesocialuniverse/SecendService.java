package net.thesocialuniverse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

@Component
public class SecendService {
    static Log logger = LogFactory.getLog(Class.class.getClass());

    @Autowired
    TxtLoader txtLoader;
    File file;

    static int count = 0;

    SecendService() throws IOException {
        file = new File("2nd");
        file.mkdirs();
    }
    public void start() {

        logger.info("Application start");
        for (int i = 0 ;i< txtLoader.length();i++){

            try {
                if (i % 100 == 0){
                    logger.info("current : " + i);
                }

                String keyword = txtLoader.get(i);
                String keywords[] = keyword.split("::::");

                String response = gathering(keywords);

                parse(keyword, response);
            } catch(IllegalArgumentException e){
                e.printStackTrace();;
                continue;

            } catch (Exception e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                try {
                    Thread.sleep(1000 * 3 * 1);      //638  365
                } catch (Exception e1){}

                DownloadManager.reset();
                i--;

            }
        }
    }

    private String toFlatQuery(String[] keywords){
        StringBuffer buffer = new StringBuffer();

        for (int i = 0 ;i < keywords.length;i++){
            if (i != 0){
                buffer.append(" ");
            }
            buffer.append("\"");
            buffer.append(keywords[i]);
            buffer.append("\"");
        }
        return buffer.toString();
    }

    private String gathering(String[] keywords) throws IOException, ParserConfigurationException, SAXException, Exception {
        DownloadManager manager = DownloadManager.getInstance();
        String response = manager.getResponse(toFlatQuery(keywords));

//        System.out.println("'"+keywords[0]+"'");
//        System.out.println(response);
//        logger.debug("keyword : " + keyword);
//        logger.debug(response);
        if(manager.status() == 200){
            count++;
        } else{
            logger.fatal("[" + manager.status() + "] " + response);
        }
        return response;
    }

    public void parse(String keyword , String xml) throws Exception{

        SAXBuilder builder = new SAXBuilder();
        Document document = builder.build(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Element root = document.getRootElement();

        int count = root.getChildren("CompleteSuggestion").size();
        if (count == 0){
//            System.out.println("skip : count == 0");
            return;
        }

//        StringWriter writer = new StringWriter();
//        new XMLOutputter(Format.getPrettyFormat()).output(document, writer);


        String keywords[] = keyword.split("::::");
        if (keywords.length == 1) return;

        File f = new File(file, keywords[1] + "/" + keyword+".txt");
        FileUtils.writeStringToFile(f, xml, "UTF-8");

    }
}
