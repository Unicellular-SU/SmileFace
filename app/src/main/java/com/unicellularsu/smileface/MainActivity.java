package com.unicellularsu.smileface;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {


    private TextView tvData;
    private SmileView smile;
    private int like=0;
    private int dislike=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvData= (TextView) findViewById(R.id.tv_data);
        tvData.setText("喜欢："+like+"  不喜欢："+dislike);

        smile= (SmileView) findViewById(R.id.smile);
        smile.setDefaultLikeString("喜欢");
        smile.setDefaultDislikeString("不喜欢");
        smile.setNum(like,dislike);
        smile.setOnFaceClickListener(new SmileView.OnFaceClickListener() {
            @Override
            public void clickLike() {
                //Toast.makeText(MainActivity.this, "你点击了笑脸", Toast.LENGTH_SHORT).show();
                like++;
                changeData();
            }

            @Override
            public void clickDislike() {
                //Toast.makeText(MainActivity.this, "你点击了哭脸", Toast.LENGTH_SHORT).show();
                dislike++;
                changeData();
            }
        });
    }

    void changeData(){
        smile.setNum(like,dislike);
        tvData.setText("喜欢："+like+"  不喜欢："+dislike);
    }

}
