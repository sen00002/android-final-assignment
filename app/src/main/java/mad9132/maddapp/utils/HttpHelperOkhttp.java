package mad9132.maddapp.utils;

import org.json.JSONObject;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpHelperOkhttp {

    public static String downloadFromFeed(RequestPackage requestPackage) throws IOException {

        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        String address = requestPackage.getEndpoint();
//        String encodedParams = requestPackage.getEncodedParams();
        OkHttpClient client = new OkHttpClient();

        Request request;
        Request.Builder requestBuilder;
        JSONObject json ;
        RequestBody body ;

        switch (requestPackage.getMethod()){
            case POST:
                json = new JSONObject(requestPackage.getParams());
                body = RequestBody.create(JSON, json.toString());
                request = new Request.Builder().url(address).post(body).build();
                break;
            case PUT:
                json = new JSONObject(requestPackage.getParams());
                body = RequestBody.create(JSON, json.toString());
                request = new Request.Builder().url(address).put(body).build();
                break;
            case GET:
                requestBuilder = new Request.Builder().url(address);
                request = requestBuilder.build();
                break;
            case DELETE:
                requestBuilder = new Request.Builder().url(address).delete();
                request = requestBuilder.build();
                break;
            default:
                requestBuilder = new Request.Builder().url(address);
                request = requestBuilder.build();
                break;
        }

//        if ( requestPackage.getMethod() == HttpMethod.GET &&
//                encodedParams.length() > 0) {
//            address = String.format("%s?%s", address, encodedParams);
//
//            Request.Builder requestBuilder = new Request.Builder().url(address);
//            request = requestBuilder.build();
//
//        } else if (( requestPackage.getMethod() == HttpMethod.POST || requestPackage.getMethod() == HttpMethod.PUT ) &&
//                encodedParams.length() > 0) {
//            address = String.format("%s", address);
//            JSONObject json = new JSONObject(requestPackage.getParams());
//
//            RequestBody body = RequestBody.create(JSON, json.toString());
//            request = new Request.Builder().url(address).post(body).build();
//
//        } else if(requestPackage.getMethod() == HttpMethod.DELETE ){
//
//        } else{
//            request = new Request.Builder().url(address).build();
//        }

        Response response = client.newCall(request).execute(); //synchronous request


        if (response.isSuccessful()) {
            return response.body().string();    //NOT toString
        } else {
            throw new IOException("Exception: Response code " + response.code());
        }

        /****
         InputStream is = null;
         try {
         URL url = new URL(address);
         HttpURLConnection conn = (HttpURLConnection) url.openConnection();
         conn.setReadTimeout(10000);
         conn.setConnectTimeout(15000);
         conn.setRequestMethod(requestPackage.getMethod());
         conn.setDoInput(true);
         if (requestPackage.getMethod().equals("POST") &&
         encodedParams.length() > 0) {
         conn.setDoOutput(true);
         OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
         writer.write(requestPackage.getEncodedParams());
         writer.flush();
         writer.close();
         }
         conn.connect();
         int responseCode = conn.getResponseCode();
         if (responseCode != 200) {
         throw new IOException("Got response code " + responseCode);
         }
         is = conn.getInputStream();
         return readStream(is);
         } catch (IOException e) {
         e.printStackTrace();
         } finally {
         if (is != null) {
         is.close();
         }
         }
         return null;
         *****/
    }

    /*****
     private static String readStream(InputStream stream) throws IOException {
     byte[] buffer = new byte[1024];
     ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
     BufferedOutputStream out = null;
     try {
     int length = 0;
     out = new BufferedOutputStream(byteArray);
     while ((length = stream.read(buffer)) > 0) {
     out.write(buffer, 0, length);
     }
     out.flush();
     return byteArray.toString();
     } catch (IOException e) {
     e.printStackTrace();
     return null;
     } finally {
     if (out != null) {
     out.close();
     }
     }
     }
     *****/

}