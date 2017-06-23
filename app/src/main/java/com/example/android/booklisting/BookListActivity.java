package com.example.android.booklisting;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;


/**
 * Created by Administrator on 2017/6/15 0015.
 * com.example.android.booklisting,BookListing
 */

public class BookListActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<Book>>{

    private BookAdapter mBookAdapter;

    //private String GOOGLE_BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=ios&maxResults=10";
    private String mRequestUrl;

    private ProgressBar mProgressbar;
    private TextView mEmptyView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_activity);

        //打到从MainActiviy传过来的keyWord参数
        Bundle extras = getIntent().getExtras();

        if(extras == null){
            return;
        }

        String keyWord = extras.getString("keyWord");
        //String keyWord = extras.get("keyWord").toString();

        mBookAdapter = new BookAdapter(this,new ArrayList<Book>());
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(mBookAdapter);

        /*进度显示及无数据时显示*/
        mProgressbar = (ProgressBar) findViewById(R.id.pb_loaging_progress);
        mEmptyView = (TextView) findViewById(R.id.empty_view);

        listView.setEmptyView(mEmptyView);

        try {
            mRequestUrl = String.format("https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=%d", URLEncoder.encode(keyWord,"UTF-8"),20);
        } catch (UnsupportedEncodingException e) {
            Log.e(getClass().getSimpleName(),"RequestUrl error!",e);
        }


        //new BooksAsyncTask().execute(mRequestUrl);
        //判断网络是否连接，如果连接加载Loader，没连接设置提示空视图中的内容
        ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();

        if(isConnected){
            getSupportLoaderManager().initLoader(1,null,this).forceLoad();
        }else{
            mProgressbar.setVisibility(View.GONE);
            mEmptyView.setText("NetWork has mistake!");
        }

    }

    @Override
    public Loader<List<Book>> onCreateLoader(int id, Bundle args) {
        return new BookAsyncTaskLoader(BookListActivity.this,mRequestUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<Book>> loader, List<Book> data) {
        mProgressbar.setVisibility(View.GONE);
        if(data != null && !data.isEmpty()){
            mBookAdapter.setBooks(data);
        }else{
            mEmptyView.setText("No Data Found!");
        }

    }

    @Override
    public void onLoaderReset(Loader<List<Book>> loader) {
        mBookAdapter.setBooks(new ArrayList<Book>());
    }


}
