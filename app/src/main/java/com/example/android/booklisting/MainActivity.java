package com.example.android.booklisting;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;

public class MainActivity extends AppCompatActivity {

    private static final int SEARCH_CODE=100;

    private EditText mKeyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ImageButton imageButton = (ImageButton) findViewById(R.id.ib_search);

        //查询关键字
        mKeyWord = (EditText) findViewById(R.id.search_key_word);

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //如果参数内容为空，不执行.
                if(mKeyWord == null || mKeyWord.equals("")){
                    return;
                }

                //跳转到另一个Activity
                Intent i = new Intent(MainActivity.this,BookListActivity.class);

                //将搜索关键字传到另一个Activity
                i.putExtra("keyWord",mKeyWord.getText());


                //startActivity(i);
                startActivityForResult(i,SEARCH_CODE);

            }
        });
    }
}
