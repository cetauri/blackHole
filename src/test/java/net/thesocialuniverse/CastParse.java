package net.thesocialuniverse;

/**
 * Created with IntelliJ IDEA.
 * User: cetauri
 * Date: 13. 6. 12.
 * Time: 오후 7:43
 * To change this template use File | Settings | File Templates.
 */
import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CastParse {
    @Test
    public void test() throws Exception {
        InputStream is = getClass().getResource("/america_60_movie_cast.csv").openStream();
        List<String> list = IOUtils.readLines(is,"UTF-8");
        for (int i = 0;i < list.size();i++){
            String casts = list.get(i);
            if (casts.equals("")){
                continue;
            }
            if (casts.startsWith("\"")){
                casts  = casts.substring(1, casts.length()-1);
            }
            String[] castArray = casts.split(",�");
            for (String name : castArray){
                System.out.println(name);
            }
        }
    }

    @Test
    public void checkDuplicate() throws Exception{
        File f  = new File("./source_final.txt");
        FileOutputStream fos = new FileOutputStream(f, true);

        Set<String> arrayList = new HashSet<String>();
        InputStream is = getClass().getResource("/source.txt").openStream();
        List<String> list = IOUtils.readLines(is,"UTF-8");
        for (int i = 0;i < list.size();i++){
            String term = list.get(i).trim();
//            if (arrayList.contains(term)){
//                continue;
//            }

            arrayList.add(term);
        }


        System.out.println(arrayList.size());

        for (String name : arrayList){
//            System.out.println(name);
            IOUtils.write(name + "\n", fos, "UTF-8");
        }
    }
}
