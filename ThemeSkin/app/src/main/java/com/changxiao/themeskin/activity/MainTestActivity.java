package com.changxiao.themeskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import android.widget.RadioGroup.OnCheckedChangeListener;
import butterknife.BindView;
import cn.feng.skin.manager.entity.DynamicAttr;
import cn.feng.skin.manager.listener.IDynamicNewView;
import cn.feng.skin.manager.listener.ISkinUpdate;
import cn.feng.skin.manager.loader.SkinInflaterFactory;
import cn.feng.skin.manager.loader.SkinManager;
import com.changxiao.themeskin.R;
import com.changxiao.themeskin.activity.base.BaseActivity;
import com.changxiao.themeskin.fragment.BufferKnifeFragment;
import com.changxiao.themeskin.fragment.MainPagerFragment;
import com.changxiao.themeskin.fragment.MemberFragment;

import com.changxiao.themeskin.model.MainTabModel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainTestActivity extends BaseActivity  implements ISkinUpdate, IDynamicNewView {

  private static int currSel = 0;

  /**
   * 主页
   */
  public static final int HOME = 0;
  /**
   * 赚钱
   */
  public static final int EARNING = 1;
  /**
   * 邀请
   */
  public static final int INVITE = 2;
  /**
   * 个人中心
   */
  public static final int USER = 3;
  /**
   * 头条
   */
  public static final int HEADLINE = 4;
  /**
   * H5
   */
  public static final int H5_LINK = 5;

  @BindView(R.id.group)
  RadioGroup group;

  @Nullable
  private SkinInflaterFactory mSkinInflaterFactory;

  private Fragment homeFragment = new MainPagerFragment();
  private Fragment imFragment = new BufferKnifeFragment();
  private Fragment interestFragment = new BufferKnifeFragment();
  private Fragment memberFragment = new MemberFragment();
  private List<Fragment> fragmentList = Arrays.asList(homeFragment, imFragment, interestFragment, memberFragment);

  private FragmentManager fragmentManager;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState) {
    mSkinInflaterFactory = SkinInflaterFactory.getLayoutInflaterFactory(this);
    super.onCreate(savedInstanceState);
  }

  @Override
  protected int getContentViewId() {
    return R.layout.activity_main_test;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    fragmentManager = getSupportFragmentManager();
//    initFootBar();
    initRadioButton();
  }

  private void initRadioButton() {
    List<MainTabModel> mainTabDatas = getMainTabDatas();
    RadioButton[] radioButtons = new RadioButton[mainTabDatas.size()];
    for (int i = 0; i < mainTabDatas.size(); i++) {
      MainTabModel mainTabModel = mainTabDatas.get(i);
      RadioButton radioButton = new RadioButton(this);
      LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0,
          LayoutParams.MATCH_PARENT, 1.0f);
      radioButton.setLayoutParams(params);
      //给RadioButton设置宽度和高度，通过params设置
      radioButton.setLayoutParams(params);
      radioButton.setGravity(Gravity.CENTER);
      radioButton.setBackgroundDrawable(null);
      radioButton.setButtonDrawable(null);
      radioButton.setCompoundDrawablePadding(3);
      radioButton.setTextSize(11);
//      radioButton.setTextCol;
      radioButton.setText(mainTabModel.getText());
      radioButton.setCompoundDrawablesWithIntrinsicBounds(null, mainTabModel.getDrawable(), null, null);	// 设置RadioButton的背景图片
//      radioButton.setButtonDrawable(android.R.color.transparent);            // 设置按钮的样式
//      radioButton.setPadding(30, 0, 0, 0);                // 设置文字距离按钮四周的距离
      radioButtons[i] = radioButton;
      group.addView(radioButton);
    }
    radioButtons[0].setChecked(true);

    group
        .setOnCheckedChangeListener(new OnCheckedChangeListener() {
          @Override
          public void onCheckedChanged(RadioGroup group, int checkedId) {
            //遍历RadioGroup，如果RadioButton数组中的RadioButton的id和当前选中的id一样，就设置ViewPager的位置为
            //该RadioButton所对应的ViewPager
            for (int i = 0; i < group.getChildCount(); i++) {
              if (radioButtons[i].getId() == checkedId) {
              }
            }
          }
        });
  }

  private List<MainTabModel> getMainTabDatas() {
    // 当前底部按钮列表
    List<MainTabModel> mainTabs = new ArrayList<>(4);
    mainTabs.add(new MainTabModel(HOME, getString(R.string.main_navigation_home)
        , ContextCompat.getDrawable(mContext, R.drawable.main_icon_home)));
    mainTabs.add(new MainTabModel(EARNING, getString(R.string.main_navigation_im)
        , ContextCompat.getDrawable(mContext, R.drawable.main_icon_state)));
    mainTabs.add(new MainTabModel(INVITE, getString(R.string.main_navigation_interest)
        , ContextCompat.getDrawable(mContext, R.drawable.main_icon_invite)));
    mainTabs.add(new MainTabModel(USER, getString(R.string.main_navigation_user)
        , ContextCompat.getDrawable(mContext, R.drawable.main_icon_my)));
    return mainTabs;
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    Fragment fragment = fragmentList.get(currSel);
    if (fragment != null) {
      fragment.onActivityResult(requestCode, resultCode, data);
    }
  }

  private void initFootBar() {
    group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
      @Override
      public void onCheckedChanged(RadioGroup group, int checkedId) {
        RadioButton rb = findViewById(checkedId);
        switch (checkedId) {
          case R.id.foot_bar_home: currSel = 0; break;
          case R.id.foot_bar_earning: currSel = 1; break;
          case R.id.foot_bar_invite: currSel = 2; break;
          case R.id.main_footbar_user: currSel = 3; break;
        }
        addFragmentToStack(currSel);
        if(currSel == 3) {
//                    UIHelper.showLogin(MainActivity.this);
        }
        if (currSel == 1) {
          SkinManager.getInstance().load();
        }
        if (currSel == 3) {
          SkinManager.getInstance().restoreDefaultTheme();
        }
      }
    });
    addFragmentToStack(0);
  }

  private void addFragmentToStack(int cur) {
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    Fragment fragment = fragmentList.get(cur);
    if (!fragment.isAdded()) {
      fragmentTransaction.add(R.id.fragment_container, fragment);
    }
    for (int i = 0; i < fragmentList.size(); i++) {
      Fragment f = fragmentList.get(i);
      if (i == cur && f.isAdded()) {
        fragmentTransaction.show(f);
      } else if (f != null && f.isAdded() && f.isVisible()) {
        fragmentTransaction.hide(f);
      }
    }
    fragmentTransaction.commitAllowingStateLoss();
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    if (keyCode == KeyEvent.KEYCODE_BACK) {
      moveTaskToBack(true);
      return true;
    }
    return super.onKeyDown(keyCode, event);
  }

  protected void dynamicAddSkinEnableView(View view, String attrName, int attrValueResId) {
    if (mSkinInflaterFactory != null) {
      mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, attrName, attrValueResId);
    }
  }

  @Override
  public void dynamicAddView(View view, List<DynamicAttr> pDAttrs) {
    if (mSkinInflaterFactory != null) {
      mSkinInflaterFactory.dynamicAddSkinEnableView(this, view, pDAttrs);
    }
  }

  @Override
  public void onThemeUpdate() {
    if (mSkinInflaterFactory != null) {
      mSkinInflaterFactory.applySkin();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    SkinManager.getInstance().attach(this);
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    SkinManager.getInstance().detach(this);
    mSkinInflaterFactory.clean();
  }
}
