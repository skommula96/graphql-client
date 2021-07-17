package org.mountcloud.graphql.util;

import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 2018/2/10. http util
 * @author zhanghaishan
 * @version V1.0
 */
public class HttpClientUtil {

    public String doGet(String url, Map<String, String> param) throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        //Creating SSLContextBuilder object
        SSLContextBuilder SSLBuilder = new SSLContextBuilder();

        SSLBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConSocFactory = new SSLConnectionSocketFactory(SSLBuilder.build(),new String[]{"TLSv1.3"}, new String[] {"TLS_AES_128_GCM_SHA256"},NoopHostnameVerifier.INSTANCE);
        HttpClientBuilder clientbuilder = HttpClients.custom();
        clientbuilder = clientbuilder.setSSLSocketFactory(sslConSocFactory);


        // 创建Httpclient对象
        CloseableHttpClient httpClient = clientbuilder.build();
//        CloseableHttpClient httpclient = HttpClients.createDefault();

        String resultString = "";
        CloseableHttpResponse response = null;
        try {
            // 创建uri
            URIBuilder builder = new URIBuilder(url);
            if (param != null) {
                for (String key : param.keySet()) {
                    builder.addParameter(key, param.get(key));
                }
            }
            URI uri = builder.build();

            // 创建http GET请求
            HttpGet httpGet = new HttpGet(uri);

            // 执行请求
            response = httpClient.execute(httpGet);
            // 判断返回状态是否为200
            if (response.getStatusLine().getStatusCode() == 200) {
                resultString = EntityUtils.toString(response.getEntity(), "UTF-8");
            }
        } finally {
            if (response != null) {
                response.close();
            }
            httpClient.close();
        }
        return resultString;
    }

    public String doGet(String url) throws IOException, URISyntaxException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return doGet(url, null);
    }

    public String doPost(String url, Map<String, String> param,Map<String,String> headers) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        SSLContextBuilder SSLBuilder = new SSLContextBuilder();
        SSLBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConSocFactory = new SSLConnectionSocketFactory(SSLBuilder.build(),new String[]{"TLSv1.3"}, new String[] {"TLS_AES_128_GCM_SHA256"},NoopHostnameVerifier.INSTANCE);
        HttpClientBuilder clientbuilder = HttpClients.custom();
        clientbuilder = clientbuilder.setSSLSocketFactory(sslConSocFactory);


        // 创建Httpclient对象
        CloseableHttpClient httpClient = clientbuilder.build();
        // 创建Httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = "";
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 创建参数列表
            if (param != null) {
                List<NameValuePair> paramList = new ArrayList<>();
                for (String key : param.keySet()) {
                    paramList.add(new BasicNameValuePair(key, param.get(key)));
                }
                // 模拟表单
                UrlEncodedFormEntity entity = new UrlEncodedFormEntity(paramList,"utf-8");
                httpPost.setEntity(entity);
            }
            // 设置请求头
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httpPost.addHeader(key,headers.get(key));
                }
            }
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        }finally {
            response.close();
        }

        return resultString;
    }

    public String doPost(String url,Map<String, String> param) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return doPost(url, param,null);
    }

    public String doPost(String url) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return doPost(url, null,null);
    }

    public String doPostJson(String url,String json) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return doPostJson(url,json,null);
    }

    public String doPostJson(String url, String json,Map<String,String> headers) throws IOException, NoSuchAlgorithmException, KeyStoreException, KeyManagementException {

        SSLContextBuilder SSLBuilder = new SSLContextBuilder();
        SSLBuilder.loadTrustMaterial(null, new TrustSelfSignedStrategy());
        SSLConnectionSocketFactory sslConSocFactory = new SSLConnectionSocketFactory(SSLBuilder.build(),new String[]{"TLSv1.3"}, new String[] {"TLS_AES_128_GCM_SHA256"},NoopHostnameVerifier.INSTANCE);
        HttpClientBuilder clientbuilder = HttpClients.custom();
        clientbuilder = clientbuilder.setSSLSocketFactory(sslConSocFactory);


        // 创建Httpclient对象
        CloseableHttpClient httpClient = clientbuilder.build();
        // 创建Httpclient对象
//        CloseableHttpClient httpClient = HttpClients.createDefault();
        CloseableHttpResponse response = null;
        String resultString = null;
        try {
            // 创建Http Post请求
            HttpPost httpPost = new HttpPost(url);
            // 设置请求头
            if (headers != null) {
                for (String key : headers.keySet()) {
                    httpPost.addHeader(key,headers.get(key));
                }
            }
            // 创建请求内容
            StringEntity entity = new StringEntity(json, ContentType.APPLICATION_JSON);
            httpPost.setEntity(entity);
            // 执行http请求
            response = httpClient.execute(httpPost);
            resultString = EntityUtils.toString(response.getEntity(), "utf-8");
        }finally {
            response.close();
        }
        return resultString;
    }
}
