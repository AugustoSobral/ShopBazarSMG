package com.sobralapps.android.shop_bazarsmg.Start_Login_Register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.sobralapps.android.shop_bazarsmg.HomeActivity;
import com.sobralapps.android.shop_bazarsmg.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.lang.reflect.Field;
import java.util.Timer;
import java.util.TimerTask;

public class StartActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;

    private ViewPager mViewPager;
    private LinearLayout dotLayout;
    private TextView[] mDots;
    private Button btn_createAccount;
    private Button btn_login;

    private SliderAdapter sliderAdapter;

    private int currentPage = 0;
    private Handler mHandler;
    private int delay = 8000; //milliseconds
    Runnable mUpdateResults = new Runnable() {
        public void run() {
            int numPages = mViewPager.getAdapter().getCount();
            currentPage = (currentPage + 1) % numPages;
            mViewPager.setCurrentItem(currentPage);

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        mViewPager = findViewById(R.id.view_pager);
        dotLayout = findViewById(R.id.dots_layout);
        btn_createAccount = findViewById(R.id.btn_create_account_screen);
        btn_login = findViewById(R.id.btn_login_screen_open);

        sliderAdapter = new SliderAdapter(this);
        mViewPager.setAdapter(sliderAdapter);

        addDotsIndicator(0);

        mViewPager.addOnPageChangeListener(viewListener);

        //Código que cria o auto Slider com Loop.
        Field mScroller = null;
        try {
            mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            Scroller scroller = new Scroller(this, new DecelerateInterpolator());
            mScroller.set(mViewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        mHandler = new Handler();
        int period = 8000; // repeat every 4 sec.
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                mHandler.post(mUpdateResults);
            }
        }, delay, period);

        //Adicionando os clickListeners:

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btn_createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, RegisterOptionsActivity.class);
                startActivity(intent);
            }
        });

    }

    //Adiciona os pontos embaixo do slide
    public void addDotsIndicator(int position){

        mDots = new TextView[5];
        dotLayout.removeAllViews();

        for(int i = 0; i<mDots.length; i++){

            mDots[i] = new TextView(this);
            mDots[i].setText(Html.fromHtml("&#8226;"));
            mDots[i].setTextSize(30);
            mDots[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));
            dotLayout.addView(mDots[i]);
        }

        //Na posição atual a cor do ponto correspondente é setada para ser diferente dos demais.
        if(mDots.length > 0)
        mDots[position].setTextColor(getResources().getColor(R.color.colorWhite));
    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

            addDotsIndicator(position);
            currentPage = position;

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        mHandler.removeCallbacks(mUpdateResults);
    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mUser!= null && !mUser.isAnonymous()){
            Intent intent = new Intent(StartActivity.this, HomeActivity.class);
            //Essas flags limpam as activities anteriores.
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }

}
