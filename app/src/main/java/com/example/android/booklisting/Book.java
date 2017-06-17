package com.example.android.booklisting;

/**
 * Created by Administrator on 2017/6/15 0015.
 * com.example.android.booklisting,BookListing
 */

public class Book {

    private String mBookCoverUrl;

    private String mBookName;

    private String mBookAuthor;

    private String mBookPublisher;

    /***
     * Constractor.
     * @param bookCoverUrl the image url of the book
     * @param bookName  the name of the book
     * @param bookAuthor the author of the book
     * @param bookPublisher the publisher of the book
     */
    public Book(String bookCoverUrl,String bookName,String bookAuthor,String bookPublisher) {

        this.mBookAuthor = bookAuthor;
        this.mBookCoverUrl = bookCoverUrl;
        this.mBookName = bookName;
        this.mBookPublisher = bookPublisher;

    }

    public String getmBookCoverUrl() {
        return mBookCoverUrl;
    }

    public String getmBookName() {
        return mBookName;
    }

    public String getmBookAuthor() {
        return mBookAuthor;
    }

    public String getmBookPublisher() {
        return mBookPublisher;
    }
}
