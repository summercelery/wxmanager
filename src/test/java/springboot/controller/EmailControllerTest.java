package springboot.controller;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import javassist.*;
import javassist.bytecode.AnnotationsAttribute;
import javassist.bytecode.ConstPool;
import javassist.bytecode.FieldInfo;
import javassist.bytecode.MethodInfo;
import javassist.bytecode.annotation.*;
import org.apache.shiro.session.mgt.SimpleSession;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import springboot.entity.User;
import springboot.util.JsonUtil;
import springboot.util.MapUtil;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

class EmailControllerTest {
    @Test
    void sengMail() throws Exception {



//
//        //获取要修改的类的所有信息
//        ClassPool pool = ClassPool.getDefault();
//        CtClass ct = pool.get("org.apache.shiro.session.mgt.SimpleSession.java");
//        CtMethod[] cms = ct.getDeclaredMethods();
//
//        for(CtMethod ctMethod :cms){
//            if("getAttributeKeys".equals(ctMethod.getName())){
//                MethodInfo minInfo = ctMethod.getMethodInfo();
//                AnnotationsAttribute attribute = (AnnotationsAttribute) minInfo.getAttribute(AnnotationsAttribute.visibleTag);
//                ConstPool cPool = minInfo.getConstPool();
//                Annotation jsonIgnore = new Annotation("com.fasterxml.jackson.annotation.JsonIgnore", cPool);
//                attribute.addAnnotation(jsonIgnore);
//                minInfo.addAttribute(attribute);
//            }
//        }
//
//
//        CtField cf = ct.getField("attributes");
//        FieldInfo fieldInfo = cf.getFieldInfo();
//
//        ConstPool cp = fieldInfo.getConstPool();
//
//        AnnotationsAttribute attribute2 = new AnnotationsAttribute(cp, AnnotationsAttribute.visibleTag);
//        Annotation jsonTypeInfo = new Annotation("com.fasterxml.jackson.annotation.JsonTypeInfo", cp);


//        jsonTypeInfo.addMemberValue("use", new EnumMemberValue(JsonTypeInfo.Id.CLASS), cp);
//        jsonTypeInfo.addMemberValue("include", new EnumMemberValue(JsonTypeInfo.Id.CLASS), cp);
//        attribute2.addAnnotation(jsonTypeInfo);
        LocalDateTime time = LocalDateTime.now();
        System.out.println(time.toString());

        System.out.println(JsonUtil.objectToJson(time));

        User user = new User();
        user.setName("123");
        user.setEmail("12346");
        SimpleSession session = new SimpleSession();
        Map map = new HashMap<>();
        map.put("user",user);
        session.setAttributes(map);

        Map m = MapUtil.objectToMap(session);


        String o = "[\"java.util.HashMap\",{\"user\":[\"springboot.entity.User\",{\"id\":\"8949072725f342139ad9b3b7451d1303\",\"name\":\"123\",\"email\":\"12346\",\"createDate\":[\"java.util.Date\",1533275155520],\"isDelete\":false}]}]";
        Map map1 = JsonUtil.jsonToObject(o,HashMap.class);
        User user1 = (User) map1.get("user");
        SimpleSession s = MapUtil.mapToObject(m,SimpleSession.class);



        String json  =JsonUtil.objectToJson(session);

        JSONObject jsonObject = new JSONObject(json);
        System.out.println(jsonObject.toString());






    }

}