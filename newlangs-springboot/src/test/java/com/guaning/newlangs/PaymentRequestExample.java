package com.guaning.newlangs;

import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
public class PaymentRequestExample {

  public static void main(String[] args) {
    String merchantId = "";
    String merchantKey = "";

    Map<String, String> params = new HashMap<>();
    params.put("pid", merchantId);
    params.put("type", "alipay");
    params.put("out_trade_no", "20160806151343349");
    params.put("notify_url", "支付结果通知url");
    params.put("return_url", "支付完成后跳转url");
    params.put("name", "解析额度充值");
    params.put("money", "1.00");
    params.put("sitename", "嘉创二级域名");

    String sign = generateSign(params, merchantKey);
    params.put("sign", sign);
    params.put("sign_type", "MD5"); 

    try {
      String apiUrl = "https://cp.nihao.top/submit.php";
      URL url = new URL(apiUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      StringBuilder postData = new StringBuilder();
      for (Map.Entry<String, String> entry : params.entrySet()) {
        postData
          .append(entry.getKey())
          .append("=")
          .append(entry.getValue())
          .append("&");
      }
      byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

      DataOutputStream outputStream = new DataOutputStream(
        connection.getOutputStream()
      );
      outputStream.write(postDataBytes);
      outputStream.flush();
      outputStream.close();

      BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getInputStream())
      );
      String line;
      StringBuilder response = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      System.out.println("支付请求响应: " + response.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private static String generateSign(
    Map<String, String> params,
    String merchantKey
  ) {
    StringBuilder sb = new StringBuilder();

    for (Map.Entry<String, String> entry : params.entrySet()) {
      sb
        .append(entry.getKey())
        .append("=")
        .append(entry.getValue())
        .append("&");
    }

    sb.append("key=").append(merchantKey);

    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      byte[] digest = md5.digest(sb.toString().getBytes("UTF-8"));

      StringBuilder result = new StringBuilder();
      for (byte b : digest) {
        result.append(String.format("%02x", b));
      }

      return result.toString();
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }
}
