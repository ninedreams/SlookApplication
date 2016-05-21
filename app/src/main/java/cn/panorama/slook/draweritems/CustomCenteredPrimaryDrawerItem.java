package cn.panorama.slook.draweritems;

import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;

import cn.panorama.slook.ui.R;

public class CustomCenteredPrimaryDrawerItem extends PrimaryDrawerItem {

    @Override
    public int getLayoutRes() {
        return R.layout.material_drawer_item_primary_centered;
    }

    @Override
    public int getType() {
        return R.id.material_drawer_item_centered_primary;
    }

}
