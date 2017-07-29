package com.unicellularsu.smileface;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by szc on 2017/7/28.
 *
 */

public class SmileView extends LinearLayout implements Animator.AnimatorListener {

    private ImageView likeFace;
    private ImageView disFace;

    private LinearLayout likeBack;
    private LinearLayout disBack;

    private TextView likeText;
    private TextView disText;
    private TextView likeNum;
    private TextView disNum;

    private int like=40;
    private int dislike=60;
    private float fLike;
    private float fDislike;



    private LinearLayout likeAll;
    private LinearLayout disAll;


    private AnimationDrawable likeAnim;//笑脸帧动画
    private AnimationDrawable disAnim;//笑脸帧动画

    private ValueAnimator backAnim;//背景拉伸

    private int defaultSize=dip2px(getContext(),35);
    private String defaultLikeString="喜欢";
    private String defaultDislikeString="无感";
    private int defaultLikeFaceResource=R.drawable.animation_like;
    private int defaultDislikeFaceResource=R.drawable.animation_dislike;
    private int defaultTextColor=Color.WHITE;
    private int defaultNumColor=Color.WHITE;


    private int type;//0笑脸1哭脸
    private boolean isClose;//判断收起动画

    private OnFaceClickListener onFaceClickListener;

    public SmileView(Context context) {
        this(context,null);
    }

    public SmileView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public SmileView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
        bindListener();
    }

    public void setOnFaceClickListener(OnFaceClickListener listener){
        this.onFaceClickListener=listener;
    }

    public void setDefaultSize(int defaultSize) {
        this.defaultSize = defaultSize;
    }

    public void setDefaultLikeString(String defaultLikeString) {
        this.likeText.setText(defaultLikeString);
    }

    public void setDefaultDislikeString(String defaultDislikeString) {
        this.disText.setText(defaultDislikeString);
    }


    public void setDefaultLikeFaceResource(int defaultLikeFaceResource) {
        this.likeFace.setBackgroundResource(defaultLikeFaceResource);
    }


    public void setDefaultDislikeFaceResource(int defaultDislikeFaceResource) {
        this.disFace.setBackgroundResource(defaultDislikeFaceResource);
    }


    public void setDefaultTextColor(int defaultTextColor) {
        this.likeText.setTextColor(defaultTextColor);
        this.disText.setTextColor(defaultTextColor);
    }

    public void setDefaultNumColor(int defaultNumColor) {
        this.likeNum.setTextColor(defaultNumColor);
        this.disNum.setTextColor(defaultNumColor);
    }

    void init(){
        this.removeAllViews();
        setOrientation(HORIZONTAL);
        setGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);
        setBackgroundColor(Color.TRANSPARENT);

        float count=like+dislike;
        fLike=like/count;
        fDislike=dislike/count;
        like=(int) (fLike*100);
        dislike= (int) (fDislike*100);


        likeFace=new ImageView(getContext());
        //添加背景资源，获取帧动画
        likeFace.setBackgroundResource(defaultLikeFaceResource);
        likeAnim= (AnimationDrawable) likeFace.getBackground();
        //初始化文字
        likeText=new TextView(getContext());
        likeText.setText(defaultLikeString);
        likeText.setTextColor(defaultTextColor);
        likeNum=new TextView(getContext());
        likeNum.setText(like+"%");
        TextPaint likePaint=likeNum.getPaint();
        likePaint.setTextSize(45f);
        likePaint.setFakeBoldText(true);
        likeNum.setTextColor(defaultNumColor);


        disFace=new ImageView(getContext());
        //添加背景资源，获取帧动画
        disFace.setBackgroundResource(defaultDislikeFaceResource);
        disAnim= (AnimationDrawable) disFace.getBackground();
        //初始化文字
        disText=new TextView(getContext());
        disText.setText(defaultDislikeString);
        disText.setTextColor(defaultTextColor);
        disNum=new TextView(getContext());
        disNum.setText(dislike+"%");
        TextPaint disPaint=disNum.getPaint();
        disPaint.setTextSize(45f);
        disPaint.setFakeBoldText(true);
        disNum.setTextColor(defaultNumColor);

        likeBack=new LinearLayout(getContext());
        disBack=new LinearLayout(getContext());
        LayoutParams backParams=new LayoutParams(defaultSize,defaultSize);
        likeBack.addView(likeFace,backParams);
        disBack.addView(disFace,backParams);
        likeBack.setBackgroundResource(R.drawable.yellow_background);
        disBack.setBackgroundResource(R.drawable.withe_background);


        LayoutParams allParams=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        likeAll=new LinearLayout(getContext());
        likeAll.setOrientation(VERTICAL);
        likeAll.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        likeAll.setBackgroundColor(Color.TRANSPARENT);
        likeAll.addView(likeText,allParams);
        likeAll.addView(likeNum,allParams);
        likeAll.addView(likeBack,allParams);

        disAll=new LinearLayout(getContext());
        disAll.setOrientation(VERTICAL);
        disAll.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
        disAll.setBackgroundColor(Color.TRANSPARENT);
        disAll.addView(disText,allParams);
        disAll.addView(disNum,allParams);
        disAll.addView(disBack,allParams);


        LayoutParams params=new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(30,20,20,70);
        params.gravity=Gravity.BOTTOM;
        addView(likeAll,params);
        addView(disAll,params);

        setTextVisibility(GONE);

    }

    void setTextVisibility(int v){
        likeText.setVisibility(v);
        likeNum.setVisibility(v);
        disText.setVisibility(v);
        disNum.setVisibility(v);
    }

    public void setNum(int like,int dislike){
        float count =like+dislike;
        fLike=like/count;
        fDislike=dislike/count;
        this.like= (int) (fLike*100);
        this.dislike= 100-this.like;
        setLike(this.like);
        setDislike(this.dislike);

    }

    public void setLike(int like){
        likeNum.setText(like+"%");
    }
    public void setDislike(int dislike){
        disNum.setText(dislike+"%");
    }

    private void bindListener(){

        likeFace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFaceClickListener==null){
                    Toast.makeText(getContext(), "你还没有绑定监听", Toast.LENGTH_SHORT).show();
                    return;
                }
                type=0;
                animBack();
                setTextVisibility(VISIBLE);
                onFaceClickListener.clickLike();
                disFace.setBackground(null);
                disFace.setBackgroundResource(defaultDislikeFaceResource);
                disAnim= (AnimationDrawable) disFace.getBackground();
            }
        });
        disFace.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onFaceClickListener==null){
                    Toast.makeText(getContext(), "你还没有绑定监听", Toast.LENGTH_SHORT).show();
                    return;
                }
                type=1;
                animBack();
                setTextVisibility(VISIBLE);
                onFaceClickListener.clickDislike();
                likeFace.setBackground(null);
                likeFace.setBackgroundResource(defaultLikeFaceResource);
                likeAnim= (AnimationDrawable) likeFace.getBackground();

            }
        });
    }

    void animBack(){
        likeFace.setClickable(false);
        disFace.setClickable(false);

        int max=Math.max(like*4,dislike*4);
        backAnim=ValueAnimator.ofInt(5,max);
        backAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int margin= (int) animation.getAnimatedValue();
                LayoutParams likeParams= (LayoutParams) likeFace.getLayoutParams();
                likeParams.bottomMargin=margin;
                if (margin<=like*4){
                    likeFace.setLayoutParams(likeParams);
                }
                if (margin<=dislike*4){
                    disFace.setLayoutParams(likeParams);
                }

            }
        });
        isClose=false;
        backAnim.addListener(this);
        backAnim.setDuration(500);
        backAnim.start();

    }

    void setBackUp(){
        int max=Math.max(like*4,dislike*4);
        backAnim=ValueAnimator.ofInt(max,5);
        backAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int margin= (int) animation.getAnimatedValue();
                LayoutParams likeParams= (LayoutParams) likeFace.getLayoutParams();
                likeParams.bottomMargin=margin;
                if (margin<=like*4){
                    likeFace.setLayoutParams(likeParams);
                }
                if (margin<=dislike*4){
                    disFace.setLayoutParams(likeParams);
                }

            }
        });
        isClose=true;
        backAnim.addListener(this);
        backAnim.setDuration(500);
        backAnim.start();
    }

    //dp转px
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    //px转dp
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    @Override
    public void onAnimationStart(Animator animation) {

    }

    @Override
    public void onAnimationEnd(Animator animation) {
        likeAnim.stop();
        disAnim.stop();
        if(isClose){
            likeFace.setClickable(true);
            disFace.setClickable(true);

            setTextVisibility(GONE);

            setBackgroundColor(Color.TRANSPARENT);
            return;
        }
        isClose=true;
        if (type==0){
            likeAnim.start();
            objectY(likeFace);
        }else {
            disAnim.start();
            objectX(disFace);
        }
    }
    @Override
    public void onAnimationCancel(Animator animation) {

    }

    @Override
    public void onAnimationRepeat(Animator animation) {

    }

    void objectX(View view){

        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationX", -10.0f, 0.0f, 10.0f, 0.0f, -10.0f, 0.0f, 10.0f, 0);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setDuration(1500);
        animator.start();

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setBackUp(); //执行回弹动画
            }
        });
    }
    public void objectY(View view) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(view, "translationY", -10.0f, 0.0f, 10.0f, 0.0f, -10.0f, 0.0f, 10.0f, 0);
        animator.setRepeatMode(ObjectAnimator.RESTART);
        animator.setDuration(1500);
        animator.start();
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                setBackUp(); //执行回弹动画
            }
        });
    }

    interface OnFaceClickListener{
        void clickLike();
        void clickDislike();
    }
}
