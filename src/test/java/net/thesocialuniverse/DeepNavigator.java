package net.thesocialuniverse;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang.StringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 * Created with IntelliJ IDEA.
 * User: cetauri
 * Date: 13. 6. 21.
 * Time: 오전 1:15
 * To change this template use File | Settings | File Templates.
 */

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:spring-config.xml")
public class DeepNavigator {

    FileOutputStream fos;
    @Test
    public void start() throws Exception{

        File f = new File("main.csv");
        fos = new FileOutputStream(new File("2nd.txt"));

        LineIterator iterator =  IOUtils.lineIterator(new FileInputStream(f), "UTF-8");
        while (iterator.hasNext()){
            String run = iterator.nextLine();
            String runs[] = StringUtils.split(run, ",\t");

            // skip count
            iterator.nextLine();

            if (runs.length == 1){
                continue;    //skip
            }

            String startTerm = StringUtils.trim(runs[0]).toLowerCase();
            for (int i = 1;i < runs.length;i++){
                String currentTerm = StringUtils.trim(runs[i]).toLowerCase();

                // 같은 term skip
                if (StringUtils.equalsIgnoreCase(currentTerm, startTerm))   {
                    continue;
                }

                currentTerm = StringUtils.replace(currentTerm, startTerm, "").trim();
                IOUtils.write(startTerm +"::::"+ currentTerm+"\n", fos);
            }
        }
    }
}
