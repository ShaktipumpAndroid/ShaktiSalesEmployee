package webservice;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class CustomHttpClient {

    /**
     * The time it takes for our client to timeout
     */

    // public static final int HTTP_TIMEOUT = 30 * 1000; // milliseconds
    private static final int HTTP_TIMEOUT = 5 * 60 * 1000; // milliseconds

    /**
     * Single instance of our HttpClient
     */
    private static HttpClient mHttpClient;

    /**
     * Get our single instance of our HttpClient object.
     *
     * @return an HttpClient object with connection parameters set
     * snsoftindore
     */

    private static HttpClient getHttpClient() {

        if (mHttpClient == null) {
            mHttpClient = new DefaultHttpClient();

            final HttpParams params = mHttpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
            HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
            ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
        }
        return mHttpClient;
    }

    public static String executeHttpPost(String url) throws Exception {
        BufferedReader in = null;
        try {
            HttpClient client = getHttpClient();
            HttpPost request = new HttpPost(url);
            client.getConnectionManager().getSchemeRegistry().register( new Scheme("https", SSLSocketFactory.getSocketFactory(), 443) );
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuilder sb = new StringBuilder();
            String line;
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line).append(NL);
            }
            in.close();
            return sb.toString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Performs an HTTP Post request to the specified url with the specified
     * parameters.
     *
     * @param url            The web address to post the request to
     * @param postParameters The parameters to send via the request
     * @return The result of the request
     */
    public static String executeHttpPost(String url, ArrayList<NameValuePair> postParameters) throws Exception {

        BufferedReader in = null;

        try {

            HttpClient client = getHttpClient();

            HttpPost request = new HttpPost(url);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);

            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuilder sb = new StringBuilder();

            String line;

            String NL = System.getProperty("line.separator");

            while ((line = in.readLine()) != null) {

                sb.append(line).append(NL);

            }

            in.close();


            return sb.toString();

        } finally {

            if (in != null) {

                try {

                    in.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    public static String executeHttpPost1(String url, ArrayList<NameValuePair> postParameters) throws Exception {

        Log.d("output_exec", "check");

        BufferedReader reader = null;

        JSONObject jObj = new JSONObject();
        String result = "";

        System.out.println("URL comes in jsonparser class is:  " + url);
        System.out.println("URL comes in jsonparser class is:  " + postParameters);
        Log.e("url is....", url+"");
        Log.e("params  is....", postParameters+"");

        try {

            int TIMEOUT_MILLISEC = 1500000; // = 12 seconds
            HttpParams httpParams = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParams,
                    TIMEOUT_MILLISEC);
            HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT_MILLISEC);

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            httpPost.setEntity(new UrlEncodedFormEntity(postParameters));
            // httpGet.setURI(new URI(url));
            httpClient.getConnectionManager().getSchemeRegistry().register( new Scheme("https", SSLSocketFactory.getSocketFactory(), 443) );
            HttpResponse httpResponse = httpClient.execute(httpPost);
            int status = httpResponse.getStatusLine().getStatusCode();


            InputStream is = httpResponse.getEntity().getContent();
            reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);

            StringBuilder sb = new StringBuilder();

            String line = null;

            String NL = System.getProperty("line.separator");

            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");

            }

         /*   while ((line = in.readLine()) != null) {

                sb.append(line + NL);

            }*/

            is.close();


            result = sb.toString();

            //JSONObject job=new JSONObject(result);
            return result;

        } finally {

            if (reader != null) {

                try {

                    reader.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    public static JSONArray executeHttpPost2(String url, ArrayList<NameValuePair> postParameters) throws Exception {

        BufferedReader in = null;

        try {

            HttpClient client = getHttpClient();

            HttpPost request = new HttpPost(url);

            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);

            request.setEntity(formEntity);

            HttpResponse response = client.execute(request);

            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

            StringBuffer sb = new StringBuffer("");

            String line = "";

            String NL = System.getProperty("line.separator");

            while ((line = in.readLine()) != null) {

                sb.append(line + NL);

            }

            in.close();


            String result = sb.toString();

            JSONArray job = new JSONArray(result);
            return job;

        } finally {

            if (in != null) {

                try {

                    in.close();

                } catch (IOException e) {

                    e.printStackTrace();

                }

            }

        }

    }

    /**
     * Performs an HTTP GET request to the specified url.
     *
     * @param url The web address to post the request to
     * @return The result of the request
     * @throws Exception
     */

    public static String executeHttpGet(String url) throws Exception {

        InputStream is;

        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet(url);

            client.getConnectionManager().getSchemeRegistry().register( new Scheme("https", SSLSocketFactory.getSocketFactory(), 443) );
            HttpResponse response = client.execute(request);
            HttpEntity httpEntity = response.getEntity();
            is = httpEntity.getContent();

            BufferedReader in = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);

            StringBuffer sb = new StringBuffer("");
            String line = "";

            while ((line = in.readLine()) != null) {
                sb.append(line);
                break;
            }
            in.close();
            String result = sb.toString();
            return result;
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }
}
