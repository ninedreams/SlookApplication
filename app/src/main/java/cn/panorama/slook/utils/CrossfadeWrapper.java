package cn.panorama.slook.utils;

import com.mikepenz.crossfader.Crossfader;
import com.mikepenz.materialdrawer.interfaces.ICrossfader;

/**
 * Created by xingyaoma on 16-4-29.
 */
public class CrossfadeWrapper implements ICrossfader {
    private Crossfader mCrossfader;

    public CrossfadeWrapper(Crossfader crossfader) {
        this.mCrossfader = crossfader;
    }

    @Override
    public void crossfade() {
        mCrossfader.crossFade();
    }

    @Override
    public boolean isCrossfaded() {
        return mCrossfader.isCrossFaded();
    }
}

