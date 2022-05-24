import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ObjectArraySerializerTest extends TestCase {
    private Object[] input;
    private String expected;
    private ObjectArraySerializerTest objectArraySerializerTest;

    public ObjectArraySerializerTest(Object[] input, String expected){
        this.input = input;
        this.expected = expected;
    }

    @Parameterized.Parameters
    public static Collection configure(){
        return Arrays.asList(new Object[][] {
                {new Object[] {"a12", "b34"}, "[\"a12\",\"b34\"]"},
                {new Object[] {}, "[]"},
                {new Object[] {null, null}, "[null,null]"}
        });
    }
    @Test
    public void test() throws Exception{
        SerializeWriter out = new SerializeWriter( 1);
        JSONSerializer.write(out, input);
        Assert.assertEquals(expected, out.toString());
    }

  /*  public void test_0() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new Object[] { "a12", "b34" });

        Assert.assertEquals("[\"a12\",\"b34\"]", out.toString());
    }

    public void test_1() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new Object[] {});

        Assert.assertEquals("[]", out.toString());
    }

    public void test_2() throws Exception {
        SerializeWriter out = new SerializeWriter(1);

        JSONSerializer.write(out, new Object[] { null, null });

        Assert.assertEquals("[null,null]", out.toString());
    }*/
    @Test
    public void test_1() throws Exception {
        Assert.assertEquals(expected, JSON.toJSONString(input, false));
    }
}
