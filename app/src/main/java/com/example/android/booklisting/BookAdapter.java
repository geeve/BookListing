package com.example.android.booklisting;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/6/15 0015.
 * com.example.android.booklisting,BookListing
 */

public class BookAdapter extends BaseAdapter {

    /*the resualt of the reasrch*/
    private List<Book> mBooks;

    private Context mContext;

    public BookAdapter(Context context, List<Book> books) {
        super();
        this.mBooks = books;
        this.mContext = context;
    }

    public void setBooks(List<Book> books){
        mBooks.addAll(books);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mBooks.size();
    }

    @Override
    public Object getItem(int i) {
        return mBooks.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        //使用View Holder模式节省内存
        BookViewHolder bookViewHolder = new BookViewHolder();

        View currentView = view;

        //使用ViewHolder，如果View为空则将View中的内容传递给Holder并设置为tag，反之，读取tag赋值给holder
        if(currentView == null){
            currentView = LayoutInflater.from(mContext).inflate(R.layout.book_list_item,viewGroup,false);

            bookViewHolder.ivBookCover = (ImageView) currentView.findViewById(R.id.iv_book_cover);
            bookViewHolder.tvBookAuthor = (TextView) currentView.findViewById(R.id.tv_author);
            bookViewHolder.tvBookName = (TextView) currentView.findViewById(R.id.tv_book_name);
            bookViewHolder.tvBookPublisher = (TextView) currentView.findViewById(R.id.tv_publisher);

            currentView.setTag(bookViewHolder);
        }else{
            bookViewHolder = (BookViewHolder) currentView.getTag();
        }


        NewsAsyncTask task = new NewsAsyncTask(bookViewHolder.ivBookCover);
        task.execute(mBooks.get(i).getmBookCoverUrl());

        bookViewHolder.tvBookPublisher.setText(mBooks.get(i).getmBookPublisher());
        bookViewHolder.tvBookName.setText(mBooks.get(i).getmBookName());
        bookViewHolder.tvBookAuthor.setText(mBooks.get(i).getmBookAuthor());

        return currentView;
    }

    //从网路上获取图片
    class NewsAsyncTask extends AsyncTask<String,Void,Bitmap> {
        private ImageView mImageView;
        public NewsAsyncTask(ImageView imageView) {
            super();
            this.mImageView = imageView;
        }

        //String...params是可变参数接受execute中传过来的参数
        @Override
        protected Bitmap doInBackground(String... params) {

            //这里同样调用我们的getBitmapFromeUrl
            Bitmap bitmap = null;
            try {
                bitmap = getBitmapFromUrl(params[0]);
            } catch (IOException e) {
                Log.e(this.getClass().getSimpleName(),"get image error!",e);
            }
            return bitmap;
        }
        //这里的bitmap是从doInBackgroud中方法中返回过来的
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            mImageView.setImageBitmap(bitmap);
        }

        private Bitmap getBitmapFromUrl(String urlString) throws IOException {
            Bitmap bm = null;
            BufferedInputStream bis = null;
            InputStream is = null;
            try {
                URL aURL = new URL(urlString);
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
}
