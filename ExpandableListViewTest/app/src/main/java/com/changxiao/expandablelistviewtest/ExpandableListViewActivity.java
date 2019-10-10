package com.changxiao.expandablelistviewtest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * http://blog.csdn.net/like7xiaoben/article/details/7211469
 *
 * 1、只有在Group展开的时候才执行getChildView
 * 
 * Created by Chang.Xiao on 2016/6/8.
 * @version 1.0
 */
public class ExpandableListViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expandable_layout);

        /**BaseExpandableListAdapter实现了ExpandableListAdapter*/
        ExpandableListAdapter adapter = new BaseExpandableListAdapter(){

            /**----------定义数组-------------------------------------------------------------------*/
            private int[] images = new int[]{
                    R.mipmap.ic_launcher,
                    R.drawable.ic_bar_friends_pressed,
                    R.drawable.ic_bar_home_pressed
            };
            private String[] armTypes = new String[]{
                    "神族","虫族","人族"
            };
            private String[][] arms = new String[][]{
                    {"狂战士","龙骑士","黑暗圣堂"},
                    {"小狗","飞龙","自爆妃子"},
                    {"步兵","伞兵","护士mm"}
            };

/*===========组元素表示可折叠的列表项，子元素表示列表项展开后看到的多个子元素项=============*/

            /**----------得到armTypes和arms中每一个元素的ID-------------------------------------------*/

            //获取组在给定的位置编号，即armTypes中元素的ID
            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            //获取在给定的组的儿童的ID，就是arms中元素的ID
            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            /**----------根据上面得到的ID的值，来得到armTypes和arms中元素的个数 ------------------------*/

            //获取的群体数量，得到armTypes里元素的个数
            @Override
            public int getGroupCount() {
                return armTypes.length;
            }

            //取得指定组中的儿童人数，就是armTypes中每一个种族它军种的个数
            @Override
            public int getChildrenCount(int groupPosition) {
                return arms[groupPosition].length;
            }

            /**----------利用上面getGroupId得到ID，从而根据ID得到armTypes中的数据，并填到TextView中 -----*/

            //获取与给定的组相关的数据，得到数组armTypes中元素的数据
            @Override
            public Object getGroup(int groupPosition) {
                return armTypes[groupPosition];
            }

            //获取一个视图显示给定组，存放armTypes
            @Override
            public View getGroupView(int groupPosition, boolean isExpanded,
                                     View convertView, ViewGroup parent) {
//                TextView textView = getTextView();//调用定义的getTextView()方法
//                textView.setText(getGroup(groupPosition).toString());//添加数据
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,1);
                TextView textView = new TextView(ExpandableListViewActivity.this);
                textView.setLayoutParams(lp);
//                if (groupPosition == 1)
//                    textView.setVisibility(View.GONE);
                return textView;
            }

            /**----------利用上面getChildId得到ID，从而根据ID得到arms中的数据，并填到TextView中---------*/

            //获取与孩子在给定的组相关的数据,得到数组arms中元素的数据
            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return arms[groupPosition][childPosition];
            }

            //获取一个视图显示在给定的组 的儿童的数据，就是存放arms
            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
                                     View convertView, ViewGroup parent) {
                LinearLayout ll = new LinearLayout(ExpandableListViewActivity.this);
                ll.setOrientation(LinearLayout.HORIZONTAL);//定义为纵向排列-ll.setOrientation(0);//定义为纵向排列
                ImageView logo = new ImageView(ExpandableListViewActivity.this);
                logo.setImageResource(images[groupPosition]);//添加图片
                ll.addView(logo);
                TextView textView = getTextView();//调用定义的getTextView()方法
                textView.setText(getChild(groupPosition,childPosition).toString());//添加数据
                ll.addView(textView);
                return ll;
            }

            /**------------------自定义一个设定TextView属性的方法----------------------------------------------*/

            //定义一个TextView
            private TextView getTextView(){
                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,150);
                TextView textView = new TextView(ExpandableListViewActivity.this);
                textView.setLayoutParams(lp);
                textView.setPadding(36, 0, 0, 0);
                textView.setTextSize(20);
                return textView;
            }

            /**-------------------其他设置-------------------------------------------------------------------*/

            //孩子在指定的位置是可选的，即：arms中的元素是可点击的
            @Override
            public boolean isChildSelectable(int groupPosition,
                                             int childPosition) {
                return true;
            }

            //表示孩子是否和组ID是跨基础数据的更改稳定
            public boolean hasStableIds() {
                return true;
            }
        };

        /**使用适配器*/
        ExpandableListView expandListView = (ExpandableListView) this.findViewById(R.id.ecpandable);
        expandListView.setAdapter(adapter);
//        expandListView.expandGroup(0); // 设置第一组张开
        for (int i = 0; i < 3; i++) {
            expandListView.expandGroup(i);
        }
        expandListView.setGroupIndicator(null); // 除去自带的箭头
    }
}
