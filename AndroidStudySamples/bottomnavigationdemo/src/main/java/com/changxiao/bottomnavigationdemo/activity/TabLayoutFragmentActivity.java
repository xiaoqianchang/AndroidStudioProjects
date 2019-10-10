package com.changxiao.bottomnavigationdemo.activity;

import static com.changxiao.bottomnavigationdemo.Constants.EARNING;
import static com.changxiao.bottomnavigationdemo.Constants.HOME;
import static com.changxiao.bottomnavigationdemo.Constants.INVITE;
import static com.changxiao.bottomnavigationdemo.Constants.USER;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.changxiao.bottomnavigationdemo.Constants;
import com.changxiao.bottomnavigationdemo.ISkinUpdate;
import com.changxiao.bottomnavigationdemo.R;
import com.changxiao.bottomnavigationdemo.fragment.GeneralWebViewFragment;
import com.changxiao.bottomnavigationdemo.fragment.HomeFragment;
import com.changxiao.bottomnavigationdemo.fragment.UserFragment;
import com.changxiao.bottomnavigationdemo.model.MainTabModel;
import com.changxiao.framework.activity.BaseActivity;
import com.changxiao.framework.util.GlideUtil;
import com.changxiao.framework.util.Logger;
import java.util.ArrayList;
import java.util.List;

public class TabLayoutFragmentActivity extends BaseActivity {

  private static final String TAG = "TabLayoutFragmentActivity";

  TabLayout mTabLayout;

  public static int sCurrentTab = Constants.HOME;  //当前处于哪个tab

  private FragmentManager mFragmentManager;
  public Fragment mCurrentFragment;
  private List<MainTabModel> mainTabDatas;

  @Override
  protected int getContentViewId() {
    return R.layout.activity_tab_layout_fragment;
  }

  @Override
  protected void init(Bundle savedInstanceState) {
    mFragmentManager = getSupportFragmentManager();
    initView();
    ImageView iv_head = findViewById(R.id.iv_head);
    Glide.with(this).load("http://www.pptbz.com/pptpic/UploadFiles_6909/201211/2012111719294197.jpg").asBitmap().into(iv_head);
    Glide.with(this).load("http://www.pptbz.com/pptpic/UploadFiles_6909/201211/2012111719294197.jpg").asBitmap().into(
        new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(Bitmap resource,
              GlideAnimation<? super Bitmap> glideAnimation) {
            Logger.e(TAG, "1111111111111111111111111111");
          }

          @Override
          public void onLoadFailed(Exception e, Drawable errorDrawable) {
            super.onLoadFailed(e, errorDrawable);
            Logger.e(TAG, "33333333333333333333");
          }
        });
    Logger.e(TAG, "2222222222222222222222222222222");
  }

  private List<MainTabModel> getMainTabDatas() {
    // 当前底部按钮列表
    List<MainTabModel> mainTabs = new ArrayList<>(4);
    mainTabs.add(new MainTabModel(HOME, getString(R.string.main_navigation_home)
        , ContextCompat.getDrawable(mContext, R.drawable.main_icon_home), "http://photo.tuchong.com/4067228/f/508035880.jpg", "http://img4q.duitang.com/uploads/item/201408/27/20140827154904_NRkaa.thumb.700_0.jpeg"));
    mainTabs.add(new MainTabModel(EARNING, getString(R.string.main_navigation_im)
        , ContextCompat.getDrawable(mContext, R.drawable.main_icon_state)));
    mainTabs.add(new MainTabModel(INVITE, getString(R.string.main_navigation_interest)
        , ContextCompat.getDrawable(mContext, R.drawable.main_icon_invite)));
    mainTabs.add(new MainTabModel(USER, getString(R.string.main_navigation_user)
        , ContextCompat.getDrawable(mContext, R.drawable.main_icon_my)));
    return mainTabs;
  }

  private void initView() {
    mainTabDatas = getMainTabDatas();
    mTabLayout = findViewById(R.id.bottom_tab_layout);
    // 设置 OnTabChangeListener 需要在添加Tab之前，不然第一次不会回调onTabSelected()方法
    mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
      @Override
      public void onTabSelected(TabLayout.Tab tab) {
//        onTabItemSelected(tab.getPosition());
        int tabPosition = tab.getPosition();
        int type = mainTabDatas.get(tabPosition).getType();
        changeFragment(type);
      }

      @Override
      public void onTabUnselected(TabLayout.Tab tab) {

      }

      @Override
      public void onTabReselected(TabLayout.Tab tab) {

      }
    });

    for (int i = 0; i < mainTabDatas.size(); i++) {
      MainTabModel mainTabModel = mainTabDatas.get(i);
//      mTabLayout.addTab(mTabLayout.newTab().setIcon(mainTabModel.getDrawable()).setText(mainTabModel.getText()));
//      mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTabView(this, i)));
      mTabLayout.addTab(mTabLayout.newTab().setCustomView(getTabView(this, i)), i == 0 ? true : false);
      View view = mTabLayout.getTabAt(i).getCustomView().findViewById(R.id.tv_main_wake_num);

      downloadPic(mainTabModel, i, new ISkinUpdate() {
        @Override
        public void onSkinUpdate(int position, Drawable selectedDrawable) {
          ImageView tv_main_tab = mTabLayout.getTabAt(position).getCustomView()
              .findViewById(R.id.iv_main_tab);
          tv_main_tab.setImageDrawable(selectedDrawable);
        }
      });
    }
//    downloadPic(mainTabDatas.get(0), 0, new ISkinUpdate() {
//      @Override
//      public void onSkinUpdate(int position, Drawable selectedDrawable) {
//        ImageView tv_main_tab = mTabLayout.getTabAt(position).getCustomView()
//            .findViewById(R.id.iv_main_tab);
//        tv_main_tab.setImageDrawable(selectedDrawable);
//      }
//    });
  }

  private void downloadPic(final MainTabModel mainTabModel, final int position, final ISkinUpdate skinUpdate) {
    // 下载图片(下载好之后通过回调通知)
    final String normalUrl = /*"http://www.pptbz.com/pptpic/UploadFiles_6909/201211/2012111719294197.jpg"; // */ mainTabModel.getNormalUrl();
    String selectedUrl =  /*"http://img4q.duitang.com/uploads/item/201408/27/20140827154904_NRkaa.thumb.700_0.jpeg"; // */mainTabModel.getSelectedUrl();
    final StateListDrawable selectorDrawable = new StateListDrawable();
    GlideUtil.loadImage(this, selectedUrl, new SimpleTarget<Bitmap>() {
      @Override
      public void onResourceReady(Bitmap resource,
          GlideAnimation<? super Bitmap> glideAnimation) {
        final Drawable selectedDrawable = new BitmapDrawable(resource);
        selectorDrawable.addState(new int[] {android.R.attr.state_selected}, selectedDrawable);
        selectorDrawable.addState(new int[] {android.R.attr.state_pressed}, selectedDrawable);
        GlideUtil.loadImage(TabLayoutFragmentActivity.this, normalUrl, new SimpleTarget<Bitmap>() {
          @Override
          public void onResourceReady(@NonNull Bitmap resource,
              @Nullable GlideAnimation<? super Bitmap> glideAnimation) {
            Drawable normalDrawable = new BitmapDrawable(resource);
            selectorDrawable.addState(new int[] {-android.R.attr.state_selected}, normalDrawable);
            selectorDrawable.addState(new int[] {-android.R.attr.state_pressed}, normalDrawable);

            if (null != skinUpdate) {
              skinUpdate.onSkinUpdate(position, selectorDrawable);
            }
//            tabModel = new MainTabModel(new MainTabModel(Constants.EARNING, mainTabModel.getText()
//                , selectedDrawable);
          }
        });
      }
    });
  }

  /**
   * 切换底部4个Tab
   */
  private void changeFragment(int type) {
    Fragment changeToFragment = null;
    Fragment fragment = mFragmentManager.findFragmentByTag(String.valueOf(type));
    if (fragment == null) {
      switch (type) {
        case Constants.HOME:
//          fragment = getHomeFragment(false);
          fragment = new HomeFragment();
          break;
//        case MAIN_TAB.EARNING:
//          fragment = new EarningFragment();
//          break;
//        case MAIN_TAB.INVITE:
//          fragment = new InviteFragment();
//          break;
        case Constants.EARNING:
        case Constants.INVITE:
        case Constants.H5_LINK:
//          MainTabModel mainTabModel = getMainTabModel(type);
//          if (null == mainTabModel) {
//            return;
//          }
//          String pageUrl = mainTabModel.getPageUrl();
//          String title = mainTabModel.getTitle();
//          fragment = GeneralWebViewFragment.newInstance(type, pageUrl, title);
          fragment = new GeneralWebViewFragment();
          break;
        case Constants.HEADLINE:
          break;
        case Constants.USER:
//          if (LoginUserService.isTourist()) {
//            showLoginDialog(TouristLoginType.LOGIN_FROM_USER_MY, null);
//            ARouterHelper.getInstance().setRouter("");
//            return;
//          }
          fragment = new UserFragment();
//          Bundle argz = new Bundle();
//          argz.putInt(KEY_CLOUD_SWITCH, cloudConfig != null ? cloudConfig.getInfoStreamSwitch() : CloudControlSwitchConfig.HOME_FRAGMENT);
//          fragment.setArguments(argz);
          break;
        default:
//          fragment = getHomeFragment(false);
          break;
      }
    }
    changeToFragment = fragment;
    if (changeToFragment != null) {
//      refreshTabViewState(type);
      if (changeToFragment != null && mCurrentFragment != changeToFragment) {
        try {
          FragmentTransaction transaction = mFragmentManager.beginTransaction();
          if (mCurrentFragment != null) {
            transaction.hide(mCurrentFragment);
          }
          if (changeToFragment.isAdded()) {
            transaction.show(changeToFragment);
          } else {
            transaction.add(R.id.main_content, changeToFragment, String.valueOf(type));
          }
          transaction.commitAllowingStateLoss();
          mCurrentFragment = changeToFragment;
        } catch (Exception e) {
          e.printStackTrace();
        }

        //统计切换到主页和个人中心页
//        if (type == MAIN_TAB.HOME) {
//          StatisticsUtil.onEvent(this, StatisticsEvent.SY_100);
//        } else if (type == MAIN_TAB.USER) {
//          StatisticsUtil.onEvent(this, StatisticsEvent.GRZX_22);
//        }
      }
      sCurrentTab = type;
//      if (sCurrentTab == MAIN_TAB.INVITE) {
//        hideWakeFriendsHint();
//      }
    }
  }

  private void onTabItemSelected(int position){
    Fragment fragment = null;
//    switch (position){
//      case 0:
//        fragment = mFragmensts[0];
//        break;
//      case 1:
//        fragment = mFragmensts[1];
//        break;
//
//      case 2:
//        fragment = mFragmensts[2];
//        break;
//      case 3:
//        fragment = mFragmensts[3];
//        break;
//    }
    if(fragment!=null) {
      getSupportFragmentManager().beginTransaction().replace(R.id.main_content,fragment).commit();
    }
  }

  /**
   * 获取Tab 显示的内容
   * @param context
   * @param position
   * @return
   */
  public View getTabView(Context context, int position) {
    MainTabModel mainTabModel = mainTabDatas.get(position);
//    View view = LayoutInflater.from(context).inflate(R.layout.view_home_tab_item,null);
    View view = LayoutInflater.from(context).inflate(R.layout.view_home_tab_item2,null);
    ImageView tabIcon = view.findViewById(R.id.iv_main_tab);
    tabIcon.setImageDrawable(mainTabModel.getDrawable());
    TextView tabText = view.findViewById(R.id.tv_main_tab);
    tabText.setText(mainTabModel.getText());
    return view;
  }
}
