package com.changxiao.bottomnavigationdemo.model;

import android.graphics.drawable.Drawable;

/**
 * @desc 首页tab model
 * @date 2019/1/9
 */
public class MainTabModel {

  private int type;
  private String normalUrl;
  private String selectedUrl;
  private String text;
  private String pageUrl;
  private String title;

  private Drawable drawable; // drawable selector

  public MainTabModel(int type, String text, Drawable drawable) {
    this.type = type;
    this.text = text;
    this.drawable = drawable;
  }

  public MainTabModel(int type, String text, String pageUrl, String title,
      Drawable drawable) {
    this.type = type;
    this.text = text;
    this.pageUrl = pageUrl;
    this.title = title;
    this.drawable = drawable;
  }

  public MainTabModel(int type, String text, Drawable drawable, String normalUrl, String selectedUrl) {
    this.type = type;
    this.text = text;
    this.drawable = drawable;
    this.normalUrl = normalUrl;
    this.selectedUrl = selectedUrl;
  }

  public int getType() {
    return type;
  }

  public void setType(int type) {
    this.type = type;
  }

  public String getNormalUrl() {
    return normalUrl;
  }

  public void setNormalUrl(String normalUrl) {
    this.normalUrl = normalUrl;
  }

  public String getSelectedUrl() {
    return selectedUrl;
  }

  public void setSelectedUrl(String selectedUrl) {
    this.selectedUrl = selectedUrl;
  }

  public String getText() {
    return text;
  }

  public void setText(String text) {
    this.text = text;
  }

  public String getPageUrl() {
    return pageUrl;
  }

  public void setPageUrl(String pageUrl) {
    this.pageUrl = pageUrl;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public Drawable getDrawable() {
    return drawable;
  }

  public void setDrawable(Drawable drawable) {
    this.drawable = drawable;
  }
}
