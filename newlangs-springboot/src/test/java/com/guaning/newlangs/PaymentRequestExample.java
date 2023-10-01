package com.guaning.newlangs;

import org.junit.jupiter.api.Test;
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
    // 商户ID和密钥，请替换为实际的值
    String merchantId = "153089424";
    String merchantKey = "JD2oN9KAXqd";

    // 构建支付请求参数
    Map<String, String> params = new HashMap<>();
    params.put("pid", merchantId);
    params.put("type", "alipay"); // 指定支付方式，这里以支付宝为例（暂时只用支付宝）
    params.put("out_trade_no", "20160806151343349"); // 商户生成的订单号（随机生成或其他方式生成）
    params.put("notify_url", "支付结果通知url"); // 异步通知地址，支付完成后将结果通知到这个地址
    params.put("return_url", "支付完成后跳转url"); // 页面跳转通知地址，支付完成后用户将被重定向到这个地址
    params.put("name", "解析额度充值"); // 商品名称（充值x解析额度）
    params.put("money", "1.00"); // 商品金额
    params.put("sitename", "嘉创二级域名"); // 网站名称，别写和厚浪有关的，有些支付会看到大网站就针对性跑路

    // 生成签名
    String sign = generateSign(params, merchantKey);
    params.put("sign", sign);
    params.put("sign_type", "MD5"); // 指定签名类型，默认为MD5

    try {
      // 构建请求URL
      String apiUrl = "https://cp.slsmcp.top/submit.php"; // 统一使用https请求
      URL url = new URL(apiUrl);
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();
      connection.setRequestMethod("POST");
      connection.setDoOutput(true);

      // 构建请求参数字符串
      StringBuilder postData = new StringBuilder();
      for (Map.Entry<String, String> entry : params.entrySet()) {
        postData
          .append(entry.getKey())
          .append("=")
          .append(entry.getValue())
          .append("&");
      }
      byte[] postDataBytes = postData.toString().getBytes(StandardCharsets.UTF_8);

      // 发送请求数据
      DataOutputStream outputStream = new DataOutputStream(
        connection.getOutputStream()
      );
      outputStream.write(postDataBytes);
      outputStream.flush();
      outputStream.close();

      // 读取响应
      BufferedReader reader = new BufferedReader(
        new InputStreamReader(connection.getInputStream())
      );
      String line;
      StringBuilder response = new StringBuilder();
      while ((line = reader.readLine()) != null) {
        response.append(line);
      }
      reader.close();

      // 输出支付请求响应
      System.out.println("支付请求响应: " + response.toString());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  // 生成签名的方法
  private static String generateSign(
    Map<String, String> params,
    String merchantKey
  ) {
    // 按照文档规定的签名生成方式
    StringBuilder sb = new StringBuilder();

    // 将所有参数按照字母顺序拼接
    for (Map.Entry<String, String> entry : params.entrySet()) {
      sb
        .append(entry.getKey())
        .append("=")
        .append(entry.getValue())
        .append("&");
    }

    // 在拼接的字符串末尾加上商户密钥
    sb.append("key=").append(merchantKey);

    // 使用MD5算法生成签名
    try {
      MessageDigest md5 = MessageDigest.getInstance("MD5");
      byte[] digest = md5.digest(sb.toString().getBytes("UTF-8"));

      // 将字节数组转换为16进制字符串
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
