import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import yearnlune.lab.convertobject.ConvertObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Project : convert-object
 * Created by IntelliJ IDEA
 * Author : DONGHWAN, KIM
 * DATE : 2020.02.05
 * DESCRIPTION :
 */
@Slf4j
public class JUnitTest {

    @Setter
    @Getter
    @EqualsAndHashCode
    @JsonDeserialize
    @NoArgsConstructor
    public static class TestObject {
        private String id;

        private String name;

        private Integer age;

        private String createdAt;

        private Date updatedAt;

        @Builder
        public TestObject(String id, String name, Integer age, String createdAt, Date updatedAt) {
            this.id = id;
            this.name = name;
            this.age = age;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
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
//        Assert.assertNotNull(resultObject.getCreatedAt());
    }

    @Test
    public void tMapToObject() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        Date today = new Date();
        LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<>();

        linkedHashMap.put("id", "TEST_ID");
        linkedHashMap.put("name", "TESTER");
        linkedHashMap.put("age", 123123);
        linkedHashMap.put("updatedAt", today);

        TestObject testObject = ConvertObject.map2Object(linkedHashMap, TestObject.class);
//        TestObject test1Object = objectMapper.convertValue(linkedHashMap, TestObject.class);
        TestObject targetObject = TestObject.builder()
                .id("TEST_ID")
                .name("TESTER")
                .age(123123)
                .updatedAt(today)
                .build();
        Assert.assertEquals(testObject, targetObject);

        Map targetMap = ConvertObject.object2Map(targetObject);

        System.out.println(targetMap);
    }
}
