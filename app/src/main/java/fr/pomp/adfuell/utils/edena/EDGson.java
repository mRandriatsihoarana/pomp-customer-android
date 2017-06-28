package fr.pomp.adfuell.utils.edena;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by edena on 13/01/2017.
 *  compile 'com.google.code.gson:gson:2.8.0'
 */

public class EDGson {

    Gson _gson;

    private static EDGson _instance;

    EDGson(){
        _gson = new Gson();
    }
    public static EDGson getInstance(){
        if(_instance == null)  _instance =  new EDGson();
        return _instance;
    }

    /**
     * Serialize all object to json format
     * ex: String strlist = gson.serialize(new ArrayList<EDGson.Tests>())
     * @param obj
     * @param <T>
     * @return
     */
    public <T> String serialize(T obj){
        return _gson.toJson(obj);
    }

    /**
     * deserialized json format to object
     * ex: EDGson.Tests testObjec = gson.deSerialize(str, EDGson.Tests.class);
     * @param json
     * @param typeClass
     * @param <T>
     * @return
     */
    public <T> T deSerialize(String json, Class<T> typeClass){
        return _gson.fromJson(json, typeClass);
    }



    public Gson getGson(){
        return _gson;

    }

    /**
     * Deserialize a json format to arraylist object
     * ex: ArrayList<EDGson.Tests> listTest = gson.deSerializeArray(strlist,EDGson.Tests.class);
     * @param json
     * @param typeClass
     * @param <T>
     * @return
     */
    public <T> ArrayList<T> deSerializeArray(String json, Class<T> typeClass){
        Type type = new ListJson<T>(typeClass);
        return  _gson.fromJson(json,type);
    }

    public class ListJson<T> implements ParameterizedType
    {
        private Class<?> wrapped;

        public ListJson(Class<T> wrapper)
        {
            this.wrapped = wrapper;
        }

        @Override
        public Type[] getActualTypeArguments()
        {
            return new Type[] { wrapped };
        }

        @Override
        public Type getRawType()
        {
            return List.class;
        }

        @Override
        public Type getOwnerType()
        {
            return null;
        }
    }

    public static class Tests{
        public Tests(String a, String b){
            this.a =a;
            this.b =b;
        }
        public String a;
        public String b;
    }


}
