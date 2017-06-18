package com.example.android.booklisting;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;


/**
 * Created by Administrator on 2017/6/15 0015.
 * com.example.android.booklisting,BookListing
 */

public class BookListUtils {

    /*tag for the lgo messages*/
    private static final String LOG_TAG = BookListUtils.class.getSimpleName();


    /***
     * get the data from internat
     * @param requestUrl the url of the book
     * @return
     */
    public static ArrayList<Book> fetchBookData(String requestUrl){
        URL url = createUrl(requestUrl);

        String jsonResponse = null;

        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG,"Error in inputStream!",e);
        }

        ArrayList<Book> books = extractBookFromJson(jsonResponse);

        return books;
    }

    /***
     * make URL object from string url
     * @param stringUrl the url from fechBookData
     * @return
     */
    private static URL createUrl(String stringUrl){
        URL url = null;

        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG,"Create URL error!",e);
        }

        return url;
    }

    /***
     * Make an HTTP request to the given URL and return a String as the response.
     * @param url the url of the book
     * @return
     * @throws IOException
     */
    private static String makeHttpRequest(URL url) throws IOException{

        String jsonResponse = "";

        if(url == null){
            return jsonResponse;
        }

        HttpURLConnection httpURLConnection = null;
        InputStream inputStream = null;

        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.connect();

            //如果返回成功代码，就读出返回数据并转化为String
            if(httpURLConnection.getResponseCode() == 200){

                inputStream = httpURLConnection.getInputStream();

                jsonResponse = readFromStream(inputStream);

            }else{
                Log.e(LOG_TAG,"Http error:"+httpURLConnection.getResponseCode());
            }
        }catch (IOException e){
            Log.e(LOG_TAG,"Retriew the book data error",e);
        }finally {
            if(httpURLConnection != null){
                httpURLConnection.disconnect();
            }
            if(inputStream != null){
                inputStream.close();
            }
        }


        return jsonResponse;
    }

    /***
     * 将InputStream中的数据转化为String
     * @param inputStream
     * @return
     * @throws IOException
     */
    private static String readFromStream(InputStream inputStream) throws IOException{
        StringBuilder output = new StringBuilder();

        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String line = bufferedReader.readLine();
            while (line != null){
                output.append(line);
                line = bufferedReader.readLine();
            }
        }

        return output.toString();
    }

    /***
     * 将JSON中的内容转换成ArrayList
     * @param bookJson
     * @return
     */
    private static ArrayList<Book> extractBookFromJson(String bookJson){

        //判断是否是空内容
        if(TextUtils.isEmpty(bookJson)){
            return null;
        }

        ArrayList<Book> books = new ArrayList<Book>();

        try {
            JSONObject baseJsonResponse = new JSONObject(bookJson);
            JSONArray itemsJsonArray = baseJsonResponse.getJSONArray("items");

            if(itemsJsonArray.length() > 0){
                for(int i = 0;i<itemsJsonArray.length();i++){
                    JSONObject firstItem = itemsJsonArray.getJSONObject(i);
                    JSONObject volumInfo = firstItem.getJSONObject("volumeInfo");

                    String bookName = volumInfo.getString("title");

                    //书籍作者可能不止一人
                    String author="";
                    if(volumInfo.has("authors")){
                        JSONArray bookAuthorArray = volumInfo.getJSONArray("authors");

                        if(bookAuthorArray.length()>1){
                            author = bookAuthorArray.getString(0) + "..";
                        }else{
                            author = bookAuthorArray.getString(0);
                        }
                    }else{
                        author="佚名";
                    }



                    String bookPublisher;
                    if(volumInfo.has("publisher")){
                        bookPublisher = volumInfo.getString("publisher");
                    }else{
                        bookPublisher = "未知出版商";
                    }

                    String bookImage;
                    if(volumInfo.has("imageLinks")) {
                        JSONObject imageLink = volumInfo.getJSONObject("imageLinks");
                        bookImage = imageLink.getString("smallThumbnail");  //或者使用thumbnail的图片
                    }else{
                        bookImage = "";
                    }

                    books.add(new Book(bookImage,bookName,author,bookPublisher));
                }

                return books;
            }

        } catch (JSONException e) {
            Log.e(LOG_TAG,"Extract JSON error",e);
        }
        return null;
    }

}
