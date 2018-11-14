package springboot.service.async;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.thymeleaf.TemplateEngine;
import springboot.wxcms.service.EmailService;
import springboot.core.util.JsonUtil;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
@RunWith(SpringRunner.class)
public class AsyncSendEmailServiceTest {

    @Autowired
    private EmailService emailService;

    @Autowired
    private TemplateEngine templateEngine;

    private ObjectMapper objectMapper = new ObjectMapper();

    private String VI = "0102030405060708";

    private String NONCE = "0CoJUm6Qyw8W8jud";
    private String secretKey = "TA3YiYCfY2dDJQgg";
    private String encSecKey = "84ca47bca10bad09a6b04c5c927ef077d9b9f1e37098aa3eac6ea70eb59df0aa28b691b7e75e4f1f9831754919ea784c8f74fbfadf2898b0be17849fd656060162857830e241aba44991601f137624094c114ea8d17bce815b0cd4e5b8e2fbaba978c6d1d14dc3d1faf852bdd28818031ccdaaa13a6018e1024e2aae98844210";





    private static List<String> userAgentList = new ArrayList<>();

    static {
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Safari/537.36");
        userAgentList.add("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        userAgentList.add("Mozilla/5.0 (iPhone; CPU iPhone OS 9_1 like Mac OS X) AppleWebKit/601.1.46 (KHTML, like Gecko) Version/9.0 Mobile/13B143 Safari/601.1");
        userAgentList.add("Mozilla/5.0 (Linux; Android 5.0; SM-G900P Build/LRX21T) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36");
        userAgentList.add("Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36");
        userAgentList.add("Mozilla/5.0 (Linux; Android 5.1.1; Nexus 6 Build/LYZ28E) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/59.0.3071.115 Mobile Safari/537.36");
        userAgentList.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_3_2 like Mac OS X) AppleWebKit/603.2.4 (KHTML, like Gecko) Mobile/14F89;GameHelper");
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10_12_5) AppleWebKit/603.2.4 (KHTML, like Gecko) Version/10.1.1 Safari/603.2.4");
        userAgentList.add("Mozilla/5.0 (iPhone; CPU iPhone OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A300 Safari/602.1");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/51.0.2704.103 Safari/537.36");
        userAgentList.add("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.12; rv:46.0) Gecko/20100101 Firefox/46.0");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:46.0) Gecko/20100101 Firefox/46.0");
        userAgentList.add("Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 6.0)");
        userAgentList.add("Mozilla/4.0 (compatible; MSIE 8.0; Windows NT 6.0; Trident/4.0)");
        userAgentList.add("Mozilla/5.0 (compatible; MSIE 9.0; Windows NT 6.1; Trident/5.0)");
        userAgentList.add("Mozilla/5.0 (compatible; MSIE 10.0; Windows NT 6.2; Win64; x64; Trident/6.0)");
        userAgentList.add("Mozilla/5.0 (Windows NT 6.3; Win64, x64; Trident/7.0; rv:11.0) like Gecko");
        userAgentList.add("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/42.0.2311.135 Safari/537.36 Edge/13.10586");
        userAgentList.add("Mozilla/5.0 (iPad; CPU OS 10_0 like Mac OS X) AppleWebKit/602.1.38 (KHTML, like Gecko) Version/10.0 Mobile/14A300 Safari/602.1");
    }

    @Test
    public void receive() throws JsonProcessingException {

        MultiValueMap map = new LinkedMultiValueMap<String, String>();
        map.add("categoryCode","1001");
        map.add("offset","0");
        map.add("total","false");
        map.add("limit","30");
        String a = objectMapper .writeValueAsString(prepare(map));





        RestTemplate restTemplate = new RestTemplate();
//        Email email = new Email();
//        Context context = new Context();
//        String uuid = UUIDUtil.getUUID();
//
//        context.setVariable("id",uuid);
//        String htmlContext = templateEngine.process("template/registerEmailTemplate",context);
//
//weixin
//        email.setReceiveAddress("465184205@qq.com");
//        email.setTitle("标题");
//        emailService.sendHtmlMail(email);
        HttpHeaders headers = new HttpHeaders();
        List<String> cookieList = new ArrayList<>();
        cookieList.add("appver=2.0.3.131777");
        headers.put(HttpHeaders.COOKIE, cookieList);


        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        headers.add("Referer", "http://music.163.com");
        headers.add("Connection", "keep-alive");
        headers.add("Host", "music.163.com");
        headers.add("Accept", "*/*");
        headers.add("Accept-Language", "zh-CN,zh;q=0.8,gl;q=0.6,zh-TW;q=0.4");
        headers.set("User-Agent", userAgentList.get((int) ((userAgentList.size() + 1) * Math.random())));
        HttpEntity<String> requestEntity = new HttpEntity<String>(a, headers);
        ResponseEntity<String> response = restTemplate.exchange("http://music.163.com/weapi/artist/list", HttpMethod.POST, requestEntity, String.class);







    }



    public Map<String, Object> prepare(Map<String, Object> raw) throws JsonProcessingException {
        Map<String, Object> data = new HashMap<String, Object>();
        data.put("params", encrypt(JsonUtil.objectToJson(raw), this.NONCE));
        data.put("params", encrypt((String) data.get("params"), this.secretKey));
        data.put("encSecKey", this.encSecKey);
        return data;
    }

    private String encrypt(String content, String password) {
        try {
            SecretKeySpec key = new SecretKeySpec(password.getBytes(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");// 创建密码器
            IvParameterSpec iv = new IvParameterSpec(VI.getBytes());// 创建iv
            byte[] byteContent = content.getBytes("utf-8");
            cipher.init(Cipher.ENCRYPT_MODE, key, iv);// 初始化
            byte[] result = cipher.doFinal(byteContent);
            BASE64Encoder encoder = new BASE64Encoder();
            return encoder.encode(result); // 加密
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

}