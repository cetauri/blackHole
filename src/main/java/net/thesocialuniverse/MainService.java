package net.thesocialuniverse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

@Component
public class MainService {
    static Log logger = LogFactory.getLog(Class.class.getClass());

    @Autowired
    CSVLoader csvLoader;
    FileOutputStream fos;

    static int count = 0;

    MainService() throws FileNotFoundException{
        File f = new File("main.csv");
        System.out.println(f.getAbsolutePath());
        fos = new FileOutputStream(f, true);
    }
    public void start() {

        logger.info("Application start");
        for (int i = 0 ;i<csvLoader.length();i++){

            try {
                if (i % 100 == 0){
                    logger.info("current : " + i);
                }

                String keyword = csvLoader.get(i);
                String keywords[] = { keyword };

                String response = gathering(keywords);

                parse(keyword, response);
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
//                     IOUtils.write(keyword,fos);
//            String keyword = StringUtils.substringAfter(list.get(i), "INFO - keyword : ");
//            String xml = StringUtils.substringAfter(list.get(i+1), "INFO - ");
//        System.out.print(xml + ",\t");
        IOUtils.write(keyword + ",\t", fos, "UTF-8");

        SAXBuilder builder = new SAXBuilder();
        Document doc = builder.build(new ByteArrayInputStream(xml.getBytes("UTF-8")));
        Element root = doc.getRootElement();

        List<Element> e = root.getChildren("CompleteSuggestion");
        for (int j = 0; j < e.size(); j++){
            Element element = e.get(j);
            String suggestion = element.getChild("suggestion").getAttribute("data").getValue();
//            System.out.print(suggestion+",\t");
            IOUtils.write(suggestion+",\t",fos, "UTF-8");

        }
//        System.out.println();
        IOUtils.write("\n",fos, "UTF-8");

//        System.out.print(",\t");
        IOUtils.write(",\t",fos, "UTF-8");

        for (int j = 0; j < e.size(); j++){
            Element element = e.get(j);
            try {

                String num_queries = element.getChild("num_queries").getAttribute("int").getValue();
//                System.out.print(num_queries+",\t");
                IOUtils.write(num_queries+",\t",fos, "UTF-8");
            } catch (Exception e1){
//                System.out.print("0,\t");
                IOUtils.write("0,\t",fos, "UTF-8");
            }
        }
        IOUtils.write("\n",fos, "UTF-8");
    }
}
