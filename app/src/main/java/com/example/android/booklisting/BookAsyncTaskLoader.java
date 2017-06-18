package com.example.android.booklisting;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

/**
 * Created by Administrator on 2017/6/18 0018.
 * com.example.android.booklisting,BookListing
 */

public class BookAsyncTaskLoader extends AsyncTaskLoader<List<Book>> {

    private String mUrl;
    public BookAsyncTaskLoader(Context context,String url) {
        super(context);
        this.mUrl = url;
    }

    @Override
    public List<Book> loadInBackground() {
        if(mUrl == null){
            return null;
        }

        return BookListUtils.fetchBookData(mUrl);
    }
}
