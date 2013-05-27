package net.thesocialuniverse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cetauri
 * Date: 13. 5. 21.
 * Time: 오후 8:22
 * To change this template use File | Settings | File Templates.
 */

@Component
public class CSVLoader {

    List<String> list ;

    @PostConstruct
    public void init(){

        try {
            InputStream is = getClass().getResource("/dvd_list.csv").openStream();
            list = IOUtils.readLines(is);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public String get(int index){
        return list.get(index);
    }

    public int length(){
        return list.size();
    }

}
