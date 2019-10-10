package com.changxiao.themeskin.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import butterknife.BindView;
import cn.feng.skin.manager.entity.AttrFactory;
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

import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity  implements ISkinUpdate, IDynamicNewView {

    private static int currSel = 0;

    @BindView(R.id.group)
    RadioGroup group;
    @BindView(R.id.foot_bar_home)
    RadioButton footbarHome;
    @BindView(R.id.foot_bar_earning)
    RadioButton footbarEarning;
    @BindView(R.id.foot_bar_invite)
    RadioButton footbarInvite;
    @BindView(R.id.main_footbar_user)
    RadioButton footbarUser;

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
        return R.layout.activity_main;
    }

    @Override
    protected void init(Bundle savedInstanceState) {
        fragmentManager = getSupportFragmentManager();
        dynamicAddSkinEnableView(footbarHome, AttrFactory.DRAWABLE_TOP, R.drawable.main_icon_home);
        dynamicAddSkinEnableView(footbarEarning, AttrFactory.DRAWABLE_TOP, R.drawable.main_icon_state);
        dynamicAddSkinEnableView(footbarInvite, AttrFactory.DRAWABLE_TOP, R.drawable.main_icon_invite);
        dynamicAddSkinEnableView(footbarUser, AttrFactory.DRAWABLE_TOP, R.drawable.main_icon_my);
        initFootBar();
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
