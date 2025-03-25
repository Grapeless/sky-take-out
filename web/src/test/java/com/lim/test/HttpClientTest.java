package com.lim.test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.lim.dto.EmployeeLoginDTO;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class HttpClientTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGet()  {
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse response = null;
        try {
            //1.创建httpclient对象
            httpClient = HttpClients.createDefault();
            //2.创建请求对象
            HttpGet httpGet = new HttpGet("http://localhost:8080/user/shop/status");
            //3.发送请求,获取响应
            response = httpClient.execute(httpGet);
            //4.解析响应
            System.out.println("状态码:" + response.getStatusLine().getStatusCode());
            HttpEntity entity = response.getEntity();
            System.out.println("响应体:" + EntityUtils.toString(entity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (response != null) {
                    response.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Test
    void testPost(){
        CloseableHttpClient httpClient = null;
        CloseableHttpResponse httpResponse = null;
        try{
            //1.构造httpclient对象
            httpClient = HttpClients.createDefault();
            //2.构造post请求对象
            HttpPost httpPost = new HttpPost("http://localhost:8080/admin/employee/login");
            //3.设置请求体
            StringEntity stringEntity = new StringEntity(
                    objectMapper.writeValueAsString(
                            new EmployeeLoginDTO("admin","123456")));
            stringEntity.setContentEncoding("utf-8");
            stringEntity.setContentType("application/json");
            //4.放入请求对象
            httpPost.setEntity(stringEntity);
            //5.发送请求,获取响应
            httpResponse = httpClient.execute(httpPost);
            //6.解析响应
            System.out.println("状态码:" + httpResponse.getStatusLine().getStatusCode());
            HttpEntity entity = httpResponse.getEntity();
            System.out.println("响应体:" + EntityUtils.toString(entity));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }finally {
            try {
                if (httpClient != null) {
                    httpClient.close();
                }
                if (httpResponse != null) {
                    httpResponse.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
