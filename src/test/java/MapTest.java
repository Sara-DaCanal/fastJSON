
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@RunWith(Parameterized.class)
public class MapTest extends TestCase {
    enum Type {no_sort, t_null, on_JSON};
    private Type type;
    private String s1;
    private String s2;
    private String s3;
    private int id;
    private String expected;



    public MapTest(Type type, String s1, String s2, String s3, int id, String expected){
        this.type=type;
        this.s1=s1;
        this.s2=s2;
        this.s3=s3;
        this.id=id;
        this.expected=expected;
    }

    @Parameterized.Parameters
    public static Collection list(){
        return Arrays.asList(new Object[][] {
                {Type.no_sort, "name", "jobs", "id", 33, "{'name':'jobs','id':33}"},
                {Type.t_null, "name", "", "", 0, "{\"name\":null}"},
                {Type.on_JSON, "Ariston", "", "", 0, "{\"map\":{\"Ariston\":null}}"}
        });
    }

    @Test
    public void test_no_sort() throws Exception {
        Assume.assumeTrue(type == Type.no_sort);
        JSONObject obj = new JSONObject(true);
        obj.put(s1, s2);
        obj.put(s3, id);
        String text = toJSONString(obj);
        Assert.assertEquals(expected, text);
    }

    @Test
    public void test_null() throws Exception {
        Assume.assumeTrue(type == Type.t_null);
        JSONObject obj = new JSONObject(true);
        obj.put(s1, null);
        String text = JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
        Assert.assertEquals(expected, text);
    }

    public static final String toJSONString(Object object) {
        SerializeWriter out = new SerializeWriter();

        try {
            JSONSerializer serializer = new JSONSerializer(out);
            serializer.config(SerializerFeature.SortField, false);
            serializer.config(SerializerFeature.UseSingleQuotes, true);

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
        Map<String, String> map = new HashMap();
        map.put(s1, null);
        MapNullValue mapNullValue = new MapNullValue();
        mapNullValue.setMap( map );
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
