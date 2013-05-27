package net.thesocialuniverse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.junit.Test;

import org.jdom.Element;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cetauri
 * Date: 13. 5. 22.
 * Time: 오전 10:20
 * To change this template use File | Settings | File Templates.
 */
public class XmlParser {

    @Test
    public void parseTest()throws Exception{
        InputStream is = getClass().getResource("/dvd.txt").openStream();
        List<String> list = IOUtils.readLines(is);

        int count = 0;

        for (int i = 0 ;i<list.size();i = i+2){
            String keyword = StringUtils.substringAfter(list.get(i), "INFO - keyword : ");
            String xml = StringUtils.substringAfter(list.get(i+1), "INFO - ");

            System.out.print(keyword + ",\t");

            SAXBuilder builder = new SAXBuilder();
            Document doc = builder.build(new ByteArrayInputStream(xml.getBytes("UTF-8")));
            Element root = doc.getRootElement();

            List<Element> e = root.getChildren("CompleteSuggestion");
            for (int j = 0; j < e.size(); j++){
                Element element = e.get(j);
                String suggestion = element.getChild("suggestion").getAttribute("data").getValue();
                System.out.print(suggestion+",\t");
            }
            System.out.println();

            System.out.print(",\t");

            for (int j = 0; j < e.size(); j++){
                Element element = e.get(j);
                try {

                    String num_queries = element.getChild("num_queries").getAttribute("int").getValue();
                    System.out.print(num_queries+",\t");
                } catch (Exception e1){
                 System.out.print("0,\t");
                }
            }
            System.out.println();
        }
        System.out.println(count);
    }

}
