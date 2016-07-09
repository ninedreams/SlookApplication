package cn.panorama.slook.ui;

import android.app.Activity;
import android.os.Bundle;

/**
 * 分享activity，包括先前分享的记录
 */

public class SharedActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared);
    }
}
