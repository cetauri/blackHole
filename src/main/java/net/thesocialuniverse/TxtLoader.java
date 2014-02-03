package net.thesocialuniverse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: cetauri
 * Date: 13. 5. 21.
 * Time: 오후 8:22
 * To change this template use File | Settings | File Templates.
 */

@Component
public class TxtLoader {

    List<String> list ;

    @PostConstruct
    public void init(){

        try {
            File f = new File("2nd.txt");
            FileInputStream fis = new FileInputStream(f);
//            InputStream is = getClass().getResource("/source.txt").openStream();
            list = IOUtils.readLines(fis);
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
