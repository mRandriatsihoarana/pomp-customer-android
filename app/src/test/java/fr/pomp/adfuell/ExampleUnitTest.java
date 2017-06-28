package fr.pomp.adfuell;

import org.junit.Test;

import java.util.ArrayList;

import fr.pomp.adfuell.utils.edena.EDGson;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }
    @Test
    public void testJson(){
        EDGson gson = EDGson.getInstance();
        ArrayList<EDGson.Tests> list = new ArrayList<>();
        EDGson.Tests a = new EDGson.Tests("edena","edena2");
        EDGson.Tests b = new EDGson.Tests("b1","b2");
        list.add(a);
        list.add(b);
        String str = gson.serialize(a);
        String stre = gson.getGson().toJson(b);
        EDGson.Tests c = gson.deSerialize(str, EDGson.Tests.class);
        String strlist = gson.serialize(list);
        ArrayList<EDGson.Tests> listTA = gson.deSerialize(strlist,ArrayList.class);
        ArrayList<EDGson.Tests> listTB = gson.deSerializeArray(strlist,EDGson.Tests.class);
        System.out.println(str);
        System.out.println(stre);
        assertEquals(4, 2 + 2);

    }
}