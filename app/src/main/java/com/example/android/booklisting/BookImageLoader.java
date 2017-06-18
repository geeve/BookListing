package com.example.android.booklisting;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;

import android.util.Log;
import android.widget.ImageView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;


/**
 * Created by Administrator on 2017/6/18 0018.
 * com.example.android.booklisting,BookListing
 *
 */

public class BookImageLoader extends AsyncTask<String,Void,Bitmap> {
    private HashMap<String,Bitmap> mHashMap;
    private ImageView mImageView;
    private String url;
    public BookImageLoader(ImageView imageView, HashMap<String,Bitmap> hashMap) {
        super();
        this.mImageView = imageView;
        this.mHashMap = hashMap;
    }

    //String...params是可变参数接受execute中传过来的参数
    @Override
    protected Bitmap doInBackground(String... params) {

        this.url = params[0];

        //这里同样调用我们的getBitmapFromeUrl
        Bitmap bitmap = null;
        try {
            //如果哈希图中没有则从网路下载,下载完成后放进hashmap中
            if(!mHashMap.containsKey(params[0])){
                bitmap = getBitmapFromUrl(params[0]);
                mHashMap.put(params[0],bitmap);
            }else{
                bitmap = mHashMap.get(params[0]);
            }

        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(),"get image error!",e);
        }
        return bitmap;
    }
    //这里的bitmap是从doInBackgroud中方法中返回过来的
    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);


        /**如果没有图片就加载默认图片*/
        if(bitmap != null && mImageView.getTag().equals(url)){
            mImageView.setImageBitmap(bitmap);
        }else{
            mImageView.setImageResource(R.mipmap.ic_launcher);
        }
    }

    private Bitmap getBitmapFromUrl(String urlString) throws IOException {
        Bitmap bm = null;
        BufferedInputStream bis = null;
        InputStream is = null;
        try {
            URL aURL = new URL(urlString);
            Log.v("imageUrl",urlString);
            URLConnection conn = aURL.openConnection();
            conn.connect();
            is = conn.getInputStream();
            bis = new BufferedInputStream(is);
            bm = BitmapFactory.decodeStream(bis);
        } catch (IOException e) {
            Log.e(this.getClass().getSimpleName(), "Error getting bitmap", e);
        }finally {
            if(bis !=null){
                bis.close();
            }
            if(is != null){
                is.close();
            }
        }
        return bm;
    }
}
