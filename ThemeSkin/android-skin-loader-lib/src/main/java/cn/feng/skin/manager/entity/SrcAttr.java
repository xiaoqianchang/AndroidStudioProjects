package cn.feng.skin.manager.entity;

import android.view.View;
import android.widget.ImageView;
import cn.feng.skin.manager.loader.SkinManager;

/**
 * Created by chenkai on 2016/10/18.
 */

public class SrcAttr extends SkinAttr {
    @Override
    public void apply(View view) {
        if (view instanceof ImageView) {
            if(RES_TYPE_NAME_DRAWABLE.equals(attrValueTypeName)) {
                ((ImageView) view).setImageDrawable(SkinManager.getInstance().getDrawable(attrValueRefId));
            }
        }
    }
}
