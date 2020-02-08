import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import yearnlune.lab.convertobject.ConvertObject;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Project : convert-object
 * Created by IntelliJ IDEA
 * Author : DONGHWAN, KIM
 * DATE : 2020.02.05
 * DESCRIPTION :
 */
@Slf4j
public class JUnitTest {

    @JsonDeserialize
    @Getter
    @NoArgsConstructor
    public static class TestObject {
        private String id;

        private String name;

        private String createdAt;

        @Builder
        public TestObject(String id, String name, String createdAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TestObject) {
                if (!this.id.equals(((TestObject) obj).id)) {
                    return false;
                }
                if (!this.createdAt.equals(((TestObject) obj).createdAt)) {
                    return false;
                }
                return this.name.equals(((TestObject) obj).name);
            }
            return super.equals(obj);
        }

    }

    @JsonDeserialize
    @Getter
    @NoArgsConstructor
    public static class ResultObject {
        private String id;

        private String name;

        private Date createdAt;

        @Builder
        public ResultObject(String id, String name, Date createdAt) {
            this.id = id;
            this.name = name;
            this.createdAt = createdAt;
        }

        @Override
        public boolean equals(Object obj) {
            if (obj instanceof TestObject) {
                if (!this.id.equals(((TestObject) obj).id)) {
                    return false;
                }
                if (!this.createdAt.equals(((TestObject) obj).createdAt)) {
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

    @Test
    public void tString2ObjectWithDateFormat() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Date today = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        TestObject inputObject = TestObject.builder()
                .id("TEST_ID")
                .name("TEST_NAME")
                .createdAt(simpleDateFormat.format(today))
                .build();
        String testString = objectMapper.writeValueAsString(inputObject);

        ResultObject resultObject = ConvertObject.string2Object(testString, ResultObject.class, "yyyy-MM-dd");
        Assert.assertNotNull(resultObject.getCreatedAt());
    }
}
