package com.example.android.booklisting;

import android.content.Context;
import android.graphics.Bitmap;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import java.util.HashMap;
import java.util.List;



/**
 * Created by Administrator on 2017/6/15 0015.
 * com.example.android.booklisting,BookListing
 */

public class BookAdapter extends BaseAdapter{

    /**the resualt of the reasrch*/
    private List<Book> mBooks;

    private Context mContext;

    private BookViewHolder mBookViewHoler;

    //用来存放已经得到过的图片
    private HashMap<String,Bitmap> mImageTempMap;

    public BookAdapter(Context context, List<Book> books) {
        super();
        this.mBooks = books;
        this.mContext = context;
        mImageTempMap = new HashMap<String,Bitmap>();
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
        mBookViewHoler = new BookViewHolder();

        View currentView = view;

        //使用ViewHolder，如果View为空则将View中的内容传递给Holder并设置为tag，反之，读取tag赋值给holder
        if(currentView == null){
            currentView = LayoutInflater.from(mContext).inflate(R.layout.book_list_item,viewGroup,false);

            mBookViewHoler.ivBookCover = (ImageView) currentView.findViewById(R.id.iv_book_cover);
            mBookViewHoler.tvBookAuthor = (TextView) currentView.findViewById(R.id.tv_author);
            mBookViewHoler.tvBookName = (TextView) currentView.findViewById(R.id.tv_book_name);
            mBookViewHoler.tvBookPublisher = (TextView) currentView.findViewById(R.id.tv_publisher);

            currentView.setTag(mBookViewHoler);
        }else{
            mBookViewHoler = (BookViewHolder) currentView.getTag();
        }


        BookImageLoader task = new BookImageLoader(mBookViewHoler.ivBookCover,mImageTempMap);
        task.execute(mBooks.get(i).getmBookCoverUrl());


        mBookViewHoler.ivBookCover.setTag(mBooks.get(i).getmBookCoverUrl());

        mBookViewHoler.tvBookPublisher.setText(mBooks.get(i).getmBookPublisher());
        mBookViewHoler.tvBookName.setText(mBooks.get(i).getmBookName());
        mBookViewHoler.tvBookAuthor.setText(mBooks.get(i).getmBookAuthor());

        return currentView;
    }
    
}
