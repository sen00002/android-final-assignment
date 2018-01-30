package mad9132.maddapp.utils;

import android.util.Log;

import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import static mad9132.maddapp.MainActivity.TAG_DEBUG;

/**
 * Helper class for working with a remote server
 *
 * @author David Gassner
 */

public class HttpHelper {

    /**
     * Returns text from a URL on a web server (no authentication)
     *
     * @param requestPackage
     * @return
     * @throws IOException
     */
    public static String downloadUrl(RequestPackage requestPackage) throws IOException {

        String address = requestPackage.getEndpoint();
        String encodedParams = requestPackage.getEncodedParams();

        if (requestPackage.getMethod() == HttpMethod.GET &&
                encodedParams.length() > 0) {
            address = String.format("%s?%s", address, encodedParams);
        }

        InputStream is = null;
        try {

            Log.d(TAG_DEBUG, "downloadUrl: trying to connect");

            URL url = new URL(address);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setReadTimeout(10000);
            conn.setConnectTimeout(15000);
            conn.setRequestMethod(requestPackage.getMethod().toString());
            conn.setDoInput(true);
            JSONObject json = new JSONObject(requestPackage.getParams());
            String params = json.toString();
            if ((requestPackage.getMethod() == HttpMethod.POST ||
                    requestPackage.getMethod() == HttpMethod.PUT) &&
                    params.length() > 0) {
                // The web service expects the request body to be in JSON format.
                conn.addRequestProperty("Accept", "application/json");
                conn.addRequestProperty("Content-Type", "application/json");
                conn.setDoInput(true);
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(params);
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
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * Reads an InputStream and converts it to a String.
     *
     * @param stream
     * @return
     * @throws IOException
     */
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
}