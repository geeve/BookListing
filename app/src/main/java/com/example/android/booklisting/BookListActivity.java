package com.example.android.booklisting;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.text.Format;
import java.util.ArrayList;

import static android.bluetooth.BluetoothAssignedNumbers.GOOGLE;

/**
 * Created by Administrator on 2017/6/15 0015.
 * com.example.android.booklisting,BookListing
 */

public class BookListActivity extends AppCompatActivity {

    private BookAdapter mBookAdapter;

    //private String GOOGLE_BOOK_URL = "https://www.googleapis.com/books/v1/volumes?q=ios&maxResults=10";
    private String mRequestUrl;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_list_activity);

        //打到从MainActiviy传过来的keyWord参数
        Bundle extras = getIntent().getExtras();

        if(extras == null){
            return;
        }

        //String keyWord = extras.getString("keyWord");
        String keyWord = extras.get("keyWord").toString();

        mBookAdapter = new BookAdapter(this,new ArrayList<Book>());
        ListView listView = (ListView)findViewById(R.id.list_view);
        listView.setAdapter(mBookAdapter);

        mRequestUrl = String.format("https://www.googleapis.com/books/v1/volumes?q=%s&maxResults=%d",keyWord,10);

        new BooksAsyncTask().execute(mRequestUrl);
    }


    /***
     * 异步加载books数据的内部类
     */
    private class BooksAsyncTask extends AsyncTask<String,Void,ArrayList<Book>>{

        @Override
        protected ArrayList<Book> doInBackground(String... strings) {
            if(strings.length == 0){
                return null;
            }

            return BookListUtils.fetchBookData(strings[0]);
        }

        @Override
        protected void onPostExecute(ArrayList<Book> books) {
            if(books.size() == 0){
                return;
            }
            mBookAdapter.setBooks(books);
        }
    }
}
