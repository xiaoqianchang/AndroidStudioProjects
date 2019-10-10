package com.changxiao.themeskin.iview;

/**
 * $desc$
 * <p/>
 * Created by Chang.Xiao on 2016/7/25.
 *
 * @version 1.0
 */
public interface IMainView extends IBaseView {

    void showProgress();
    void hideProgress();
    void showErrorView();
    void showNoMoreData();
//    void showMeiziList(List<Meizi> meiziList);
}
