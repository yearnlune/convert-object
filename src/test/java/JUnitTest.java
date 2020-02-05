import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import org.junit.Assert;
import org.junit.Test;
import yearnlune.lab.convertobject.ConvertObject;

/**
 * Project : convert-object
 * Created by IntelliJ IDEA
 * Author : DONGHWAN, KIM
 * DATE : 2020.02.05
 * DESCRIPTION :
 */
public class JUnitTest {

    @JsonDeserialize
    public static class TestObject {
        private String id = "TEST_ID";

        private String name = "TEST_NAME";

        public TestObject() {
        }

        public TestObject(String id, String name) {
            this.id = id;
            this.name = name;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TestObject) {
                if (!this.id.equals(((TestObject) obj).id)) {
                    return false;
                }
                return this.name.equals(((TestObject) obj).name);
            }
            return super.equals(obj);
        }

    }

    @Test
    public void tString2Object() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String testString = objectMapper.writeValueAsString(new TestObject());

        TestObject resultObject = ConvertObject.string2Object(testString, TestObject.class);
        Assert.assertEquals(resultObject, new TestObject());
    }
}
