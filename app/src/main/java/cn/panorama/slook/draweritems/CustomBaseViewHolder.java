package cn.panorama.slook.draweritems;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-4-29.
 */
public class CustomBaseViewHolder extends RecyclerView.ViewHolder {

    protected View view;
    protected ImageView icon;
    protected TextView name;
    protected TextView description;

    public CustomBaseViewHolder(View view) {
        super(view);

        this.view = view;
        this.icon = (ImageView) view.findViewById(R.id.material_drawer_icon);
        this.name = (TextView) view.findViewById(R.id.material_drawer_name);
        this.description = (TextView) view.findViewById(R.id.material_drawer_description);
    }

}
