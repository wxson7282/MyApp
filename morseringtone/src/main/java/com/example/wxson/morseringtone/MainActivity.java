package com.example.wxson.morseringtone;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.EditText;
import layout.*;
import android.text.TextUtils;

public class MainActivity extends AppCompatActivity {
    //当前显示的fragment
    private static final String STATE_FRAGMENT_SHOW = "STATE_FRAGMENT_SHOW";
    private static final String SAVED_EDIT_TEXT = "";
    private FragmentManager mFragmentManager = getSupportFragmentManager();
    private Fragment mCurrentFragment = new Fragment();

    HomeFragment homeFragment;
    BlankFragment blankFragment;
    EditFragment editFragment;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            //根据MenuItem显示相应fragment
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    showFragment(homeFragment);
                    return true;
                case R.id.navigation_edit:
                    showFragment(editFragment);
                    return true;
                case R.id.navigation_dashboard:
                    showFragment(blankFragment);
                    return true;
                case R.id.navigation_notifications:
                    showFragment(blankFragment);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState != null) { // 内存重启时调用

            //获取内存重启时保存的fragment名字
            String saveName = savedInstanceState.getString(STATE_FRAGMENT_SHOW);

            //从fragmentManager里面找到fragment
            homeFragment = (HomeFragment)mFragmentManager.findFragmentByTag(HomeFragment.class.getName());
            editFragment = (EditFragment)mFragmentManager.findFragmentByTag(EditFragment.class.getName());
            blankFragment = (BlankFragment)mFragmentManager.findFragmentByTag(BlankFragment.class.getName());

            //如果为空就默认操作
            if(TextUtils.isEmpty(saveName)){
                //解决重叠问题
                mFragmentManager.beginTransaction()
                        .show(homeFragment)
                        .hide(editFragment)
                        .hide(blankFragment)
                        .commit();
                //把当前显示的fragment记录下来
                mCurrentFragment = homeFragment;
            }
            else {
                if(saveName.equals(homeFragment.getClass().getName())) {    //如果推出之前是homeFragment
                    //解决重叠问题
                    mFragmentManager.beginTransaction()
                            .show(homeFragment)
                            .hide(editFragment)
                            .hide(blankFragment)
                            .commit();
                    //把当前显示的fragment记录下来
                    mCurrentFragment = homeFragment;
                }
                else if(saveName.equals(editFragment.getClass().getName())){  //如果推出之前是editFragment
                    //解决重叠问题
                    mFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .show(editFragment)
                            .hide(blankFragment)
                            .commit();
                    //把当前显示的fragment记录下来
                    mCurrentFragment = editFragment;
                }
                else if(saveName.equals(blankFragment.getClass().getName())){  //如果推出之前是blankFragment
                    //解决重叠问题
                    mFragmentManager.beginTransaction()
                            .hide(homeFragment)
                            .hide(editFragment)
                            .show(blankFragment)
                            .commit();
                    //把当前显示的fragment记录下来
                    mCurrentFragment = blankFragment;
                }
            }
            //获取内存重启时保存的EditText内容
            CharSequence savedEditText = savedInstanceState.getString(SAVED_EDIT_TEXT);
            if(TextUtils.isEmpty(savedEditText)) {
                ((EditText)findViewById(R.id.edit_text)).setText(savedEditText);
            }
        }
        else { //正常启动时调用
            homeFragment = new HomeFragment();
            blankFragment = new BlankFragment();
            editFragment = new EditFragment();
            showFragment(homeFragment);
        }
        //设定BottomNavigationView以及相应的ItemSelectedListener
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //内存重启时保存当前的fragment名字
        outState.putString(STATE_FRAGMENT_SHOW, mCurrentFragment.getClass().getName());
        //内存重启时保存EditText的内容
        outState.putString(SAVED_EDIT_TEXT, ((EditText)findViewById(R.id.edit_text)).getText().toString());
        super.onSaveInstanceState(outState);
    }

    /**
     * 使用show() hide()切换页面
     * 显示fragment
     */
    private void showFragment(Fragment fg){
        FragmentTransaction transaction = mFragmentManager.beginTransaction();
        //如果之前没有添加过
        if(!fg.isAdded()){
            transaction
                    .hide(mCurrentFragment)
                    .add(R.id.content,fg,fg.getClass().getName());  //第三个参数为添加当前的fragment时绑定一个tag，即绑定fragment的类名
        }else{
            transaction
                    .hide(mCurrentFragment)
                    .show(fg);
        }
        mCurrentFragment = fg;
        transaction.commit();
    }

}
