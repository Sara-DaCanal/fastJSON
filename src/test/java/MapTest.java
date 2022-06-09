
import com.alibaba.fastjson.annotation.JSONField;
import org.junit.Assert;
import junit.framework.TestCase;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Assume;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class MapTest extends TestCase {
    enum Type {no_sort, t_null, on_JSON};
    private Type type;
    private String expected;
    private JSONObject obj=null;
    private Map<String, Object> map;
    private MapNullValue mapNullValue;
    private static boolean bool1;
    private static boolean bool2;



    public MapTest(Type type, String s1, Object s2, String s3, Object id, boolean bool1, boolean bool2, String expected){
        configure(type, s1, s2, s3, id, bool1, bool2, expected);
    }

    private void configure(Type type, String s1, Object s2, String s3, Object id, boolean bool1, boolean bool2, String expected){
        this.type=type;
        this.bool1=bool1;
        this.bool2 = bool2;
        this.expected=expected;

        if(s1!=null && s1!= "") {
            this.obj = new JSONObject(true);
            obj.put(s1, s2);
        }
        if(s3!=null && s3!=""){
            if (this.obj == null) this.obj = new JSONObject(true);
            obj.put(s3, id);
        }

        if(s1!=null && s1!= "") {
            this.map=new HashMap<>();
            map.put(s1, s2);
        }
        if(s3!=null && s3!=""){
            if (this.map == null) this.map=new HashMap<>();
            map.put(s3, id);
        }
        this.mapNullValue = new MapNullValue();
        mapNullValue.setMap( map );
    }

    @Parameterized.Parameters
    public static Collection parameters(){
        return Arrays.asList(new Object[][] {
                {Type.no_sort, "name", "jobs", "id", 33, true, true, "{'name':'jobs','id':33}"},
                {Type.no_sort, "name", "jobs", "id", 33, false, false, "{\"name\":\"jobs\",\"id\":33}"},
                {Type.no_sort, null, null, null, null, false, true, "null"},
                {Type.t_null, "name", null, "", null, false, false, "{\"name\":null}"},
                {Type.t_null, null, null, null, null, false, false, "null"},
                {Type.on_JSON, "Ariston", null, "", 0, false, false,"{\"map\":{\"Ariston\":null}}"},
                {Type.on_JSON, "A", 0, "B", 1, false, false, "{\"map\":{\"A\":0,\"B\":1}}"},
                {Type.on_JSON, null, null, null, null, false, false, "{\"map\":null}"}

        });
    }

    @Test
   public void test_no_sort(){
        Assume.assumeTrue(type == Type.no_sort);
        String text = toJSONString(obj);
        Assert.assertEquals(expected, text);
    }

    @Test
    public void test_null(){
        Assume.assumeTrue(type == Type.t_null);
        String text = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals(expected, text);
    }
    public static final String toJSONString(Object object) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.SortField, bool1);
            serializer.config(SerializerFeature.UseSingleQuotes, bool2);

            serializer.write(object);

            return out.toString();
        } catch (StackOverflowError e) {
            throw new JSONException("maybe circular references", e);
        } finally {
            out.close();
        }
    }
    @Test
   public void test_onJSONField() {
        Assume.assumeTrue(type == Type.on_JSON);
        String json = JSON.toJSONString( mapNullValue );
        assertEquals(expected, json);
    }

    class MapNullValue {
        @JSONField(serialzeFeatures = {SerializerFeature.WriteMapNullValue})
        private Map map;

        public Map getMap() {
            return map;
        }

        public void setMap( Map map ) {
            this.map = map;
        }
    }

}
