package com.example.ocrtest;

import org.json.JSONArray;
import org.json.JSONObject;
//import org.springframework.http.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
//import java.net.http.HttpClient;
//import java.net.http.HttpResponse;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ocr")
public class OcrController {

    String BASE_URL = "https://zfusrxrk3k.apigw.ntruss.com/custom/v1/23890/2453eb775e796913aca6d68e93eb5c9d0eda8b9e8f224fe7413ac067ae57fc7b/document/credit-card";

//    @PostMapping(value = "/V1")
//    public void ocrV1() {
//
//        String url = "/document/credit-card";
//        RestTemplate restTemplate = new RestTemplate();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
//        headers.add("X-OCR-SECRET", "UUFKeWxOQ0hMQ0NtdmF1VW9mQVlodFp4ckx5Y1JYWmY=");
//
//        JSONObject jsonObject = new JSONObject();
////        jsonObject.put("");
//
//        HttpEntity<String> entity = new HttpEntity<String>(jsonObject.toString(), headers);
//        Map<String, String> response = restTemplate.exchange(BASE_URL + url, HttpMethod.POST, entity, Map.class).getBody();
//
//        System.out.println(response);
////        startResponseEntity.setAuth_key(response.get("auth_key"));
//    }

    // naver ocr credit-card API
    @GetMapping("/V2")
    public ResponseEntity<String> ocrV2() {
//        String apiURL = "YOUR_API_URL";
        String secretKey = "bHdFSFJGTklOVVJzUFBNeEx3UmhFU3dZdXVtakxUY3U=";
        String imageFile = "static/images/img.png";
        String tmp = null;

        try {
            URL url = new URL(BASE_URL);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", secretKey);

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", "png");
//            image.put("url", "https://kr.object.ncloudstorage.com/ocr-ci-test/sample/1.jpg"); // image should be public, otherwise, should use data
            FileInputStream inputStream = new FileInputStream("/Users/hayoon/Downloads/ocrtest/src/main/resources/static/images/img_1.png");
            byte[] buffer = new byte[inputStream.available()];
            inputStream.read(buffer);
            inputStream.close();
            image.put("data", buffer);
            image.put("name", "demo");
            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            tmp = String.valueOf(response);
        } catch (Exception e) {
            System.out.println(e);
        }

        return new ResponseEntity<>(tmp, HttpStatus.OK);
    }

    // toss card payment API
    @PostMapping("/v1/payments/credit-card")
    public ResponseEntity<String> keyIn(@RequestParam String cardNumber,
                                        @RequestParam String validYear,
                                        @RequestParam String validMonth) throws IOException, InterruptedException {

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://api.tosspayments.com/v1/payments/key-in"))
                .header("Authorization", "Basic dGVzdF9za183WFpZa0tMNE1yakFsYUJ5MGExVjB6SndsRVdSOg==")
                .header("Content-Type", "application/json")
                .method("POST", HttpRequest.BodyPublishers.ofString("{\"amount\":15000,\"orderId\":\"afm4N87tU_uJNHayoon_1ROjD_\",\"orderName\":\"아메리카노 외 2건\",\"customerName\":\"하윤\",\"cardNumber\":\""+ cardNumber +"\",\"cardExpirationYear\":\""+ validYear+"\",\"cardExpirationMonth\":\""+ validMonth+"\",\"cardPassword\":\"12\",\"customerIdentityNumber\":\"881212\"}"))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        return new ResponseEntity<>(response.body(), HttpStatus.OK);
    }
}