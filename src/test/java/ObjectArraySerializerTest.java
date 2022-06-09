import junit.framework.TestCase;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONSerializer;
import com.alibaba.fastjson.serializer.SerializeWriter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ObjectArraySerializerTest extends TestCase {
    private Object[] input;
    private String expected;
    private SerializeWriter out;

    public ObjectArraySerializerTest(Object[] input, String expected){
        configure(input,expected);
    }

    private void configure(Object[] input, String expected){
        this.input=input;
        this.expected=expected;
        this.out = new SerializeWriter(1);
    }

    @Parameterized.Parameters
    public static Collection parameters() throws ParseException {
        return Arrays.asList(new Object[][] {
                {new Object[] {"a12", "b34"}, "[\"a12\",\"b34\"]"},
                {new Object[] {}, "[]"},
                {new Object[] {1,1}, "[1,1]"},
                {new Object[] {1.1,1.1}, "[1.1,1.1]"},
                //{new Object[] {new Color(255,0,0), new Color(0,255,0)}, "[{r:255,g:0,b:0,alpha:255},{r:0,g:255,b:0,alpha:255}]"},
                {new Object[] {new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), new SimpleDateFormat("yyyy-MM-dd").parse("2021-02-02")},"[1640991600000,1612220400000]"},
                {new Object[] {null, null}, "[null,null]"},
                {null, "null"}
        });
    }
    @Test
    public void test(){
        JSONSerializer.write(out, input);
        Assert.assertEquals(expected, out.toString());
    }

    @Test
    public void test_1(){
        Assert.assertEquals(expected, JSON.toJSONString(input, false));
    }
}
