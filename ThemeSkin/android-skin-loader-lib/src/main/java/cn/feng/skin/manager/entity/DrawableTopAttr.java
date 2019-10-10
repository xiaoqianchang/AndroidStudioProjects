package cn.feng.skin.manager.entity;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import cn.feng.skin.manager.loader.SkinManager;

/**
 * Created by chenkai on 2016/10/14.
 */

public class DrawableTopAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof TextView) {
            TextView tv = (TextView) view;
            if(RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)){
                Drawable top = SkinManager.getInstance().getDrawable(attrValueRefId);
                top.setBounds(0, 0, top.getIntrinsicWidth(), top.getIntrinsicHeight());
                tv.setCompoundDrawables(null, top, null, null);
            }
        }
    }
}
