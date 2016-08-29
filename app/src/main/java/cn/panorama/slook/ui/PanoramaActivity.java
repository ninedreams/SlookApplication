package cn.panorama.slook.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.OvershootInterpolator;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.panoramagl.PLCubicPanorama;
import com.panoramagl.PLICamera;
import com.panoramagl.PLIPanorama;
import com.panoramagl.PLIView;
import com.panoramagl.PLImage;
import com.panoramagl.PLView;
import com.panoramagl.PLViewListener;
import com.panoramagl.enumerations.PLCubeFaceOrientation;
import com.panoramagl.hotspots.PLHotspot;
import com.panoramagl.hotspots.PLIHotspot;
import com.panoramagl.ios.structs.CGPoint;
import com.panoramagl.loaders.PLILoader;
import com.panoramagl.loaders.PLJSONLoader;
import com.panoramagl.structs.PLPosition;
import com.panoramagl.transitions.PLITransition;
import com.panoramagl.transitions.PLTransitionBlend;
import com.panoramagl.utils.PLUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xingyaoma on 16-4-29.
 */
public class PanoramaActivity extends PLView {
    /**member variables*/

    private ZoomControls mZoomControls;

    //浮动按钮
    private List<FloatingActionMenu> menus = new ArrayList<>();
    private FloatingActionButton fab4;
    private FloatingActionButton fab5;
    private FloatingActionButton fab6;
    private FloatingActionMenu menuGreen;
    private Handler mUiHandler = new Handler();

    private String jsonUrl;//由其他activity点击进入时传递的jsonurl

    /**init methods*/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //首次进入的加载
        loadPanoramaFromJSON("res://raw/json_cubic_ustb1");
        //loadPanorama();

        //右下角的浮动按钮
        execFloatingActionButton();

        //顶部的search
        //execSearchView();

        this.setListener(new PLViewListener()
        {
            @Override
            public void onDidClickHotspot(PLIView view, PLIHotspot hotspot, CGPoint screenPoint, PLPosition scene3DPoint)
            {
                //从json加载，panorama的属性写在json文件里
                //从本地6张图手动加载，属性在本地写
                //两种方法都可以在本地调节属性
                int index = 1;

                loadPanoramaFromJSON(index);

            }

            @Override
            public void onDidBeginLoader(PLIView view, PLILoader loader)
            {
                setControlsEnabled(false);
            }

            @Override
            public void onDidCompleteLoader(PLIView view, PLILoader loader)
            {
                setControlsEnabled(true);
            }

            @Override
            public void onDidErrorLoader(PLIView view, PLILoader loader, String error)
            {
                setControlsEnabled(true);
            }

            @Override
            public void onDidBeginTransition(PLIView view, PLITransition transition)
            {
                setControlsEnabled(false);
            }

            @Override
            public void onDidStopTransition(PLIView view, PLITransition transition, int progressPercentage)
            {
                setControlsEnabled(true);
            }

            @Override
            public void onDidEndTransition(PLIView view, PLITransition transition)
            {
                setControlsEnabled(true);
            }
        });

    }

    private void execSearchView() {



    }


    /**
     * This event is fired when root content view is created
     * @param contentView current root content view
     * @return root content view that Activity will use
     */
    @Override
    protected View onContentViewCreated(View contentView)
    {
        //display the view on the whole contentview
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Load layout
        ViewGroup mainView = (ViewGroup)this.getLayoutInflater().inflate(R.layout.activity_panorama, null);
        //Add 360 view
        mainView.addView(contentView, 0);

        //Zoom controls
        mZoomControls = (ZoomControls)mainView.findViewById(R.id.zoom_controls);
        mZoomControls.setOnZoomInClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PLICamera camera = getCamera();
                if(camera != null)
                    camera.zoomIn(true);
            }
        });
        mZoomControls.setOnZoomOutClickListener(new OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                PLICamera camera = getCamera();
                if(camera != null)
                    camera.zoomOut(true);
            }
        });
        //Return root content view
        return  super.onContentViewCreated(mainView);
    }

    /**utility methods*/
    private void setControlsEnabled(final boolean isEnable)
    {
        this.runOnUiThread(new Runnable()
        {
            @Override
            public void run()
            {
                mZoomControls.setIsZoomInEnabled(isEnable);
                mZoomControls.setIsZoomOutEnabled(isEnable);
            }
        });
    }

    /**load methods*/

    /**
     * Load panorama image manually
     */
    private void loadPanorama()
    {
        try
        {
            Context context = this.getApplicationContext();
            PLIPanorama panorama = null;
            //Lock panoramic view,lock in memory more quick
            this.setLocked(true);
            //Create panorama
            PLCubicPanorama cubicPanorama = new PLCubicPanorama();
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.ustb1_f), false), PLCubeFaceOrientation.PLCubeFaceOrientationFront);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.ustb1_b), false), PLCubeFaceOrientation.PLCubeFaceOrientationBack);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.ustb1_l), false), PLCubeFaceOrientation.PLCubeFaceOrientationLeft);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.ustb1_r), false), PLCubeFaceOrientation.PLCubeFaceOrientationRight);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.ustb1_u), false), PLCubeFaceOrientation.PLCubeFaceOrientationUp);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.ustb1_d), false), PLCubeFaceOrientation.PLCubeFaceOrientationDown);
            panorama = cubicPanorama;

            if(panorama != null)
            {
                //Set camera rotation
                panorama.getCamera().lookAt(0.0f, 170.0f);
                //Add a hotspot
                panorama.addHotspot(new PLHotspot(1, new PLImage(PLUtils.getBitmap(context, R.raw.hotspot_front), false), 0.0f, 170.0f, 0.05f, 0.05f));
                //Reset view
                this.reset();
                //Load panorama
                this.startTransition(new PLTransitionBlend(2.0f), panorama); //or use this.setPanorama(panorama);
            }
            //Unlock panoramic view
            this.setLocked(false);
        }
        catch(Throwable e)
        {
            Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Load panorama image using JSON protocol
     * use String url
     */
    private void loadPanoramaFromJSON(String url)
    {
        try
        {
            PLILoader loader = null;

            loader = new PLJSONLoader(url);

            if(loader != null)
                this.load(loader, true, new PLTransitionBlend(2.0f));
        }
        catch(Throwable e)
        {
            Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * Load panorama image using JSON protocol
     * local switch
     */
    private void loadPanoramaFromJSON(int index)
    {
        try
        {
            PLILoader loader = null;

            switch (index){

                case 1:
                    loader = new PLJSONLoader("res://raw/json_cubic_ustb1");
                    break;

                case 2:
                    loader = new PLJSONLoader("res://raw/json_cubic_ustb1");
                    break;

                case 3:
                    loader = new PLJSONLoader("res://raw/json_cubic_final");
                    break;

                case 4:
                    loader = new PLJSONLoader("res://raw/json_cubic_final");
                    break;

                default:
                    break;
            }

            if(loader != null)
                this.load(loader, true, new PLTransitionBlend(2.0f));
        }
        catch(Throwable e)
        {
            Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Load panorama image using JSON protocol
     */
    private void loadPanoramaFromJSON()
    {
        try
        {
            PLILoader loader = null;

            loader = new PLJSONLoader("res://raw/json_cubic_final");

            if(loader != null)
                this.load(loader, true, new PLTransitionBlend(2.0f));
        }
        catch(Throwable e)
        {
            Toast.makeText(this.getApplicationContext(), "Error: " + e, Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("NewApi")
    public void execFloatingActionButton(){

        menuGreen = (FloatingActionMenu) findViewById(R.id.menu_green);
        fab4 = (FloatingActionButton) findViewById(R.id.fab4);
        fab5 = (FloatingActionButton) findViewById(R.id.fab5);
        fab6 = (FloatingActionButton) findViewById(R.id.fab6);

        final FloatingActionButton programFab1 = new FloatingActionButton(getActivity());
        programFab1.setButtonSize(FloatingActionButton.SIZE_MINI);
        programFab1.setLabelText(getString(R.string.app_name));
        programFab1.setImageResource(R.mipmap.ic_edit);
        menuGreen.addMenuButton(programFab1);
        programFab1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                programFab1.setLabelColors(ContextCompat.getColor(getActivity(), R.color.grey),
                        ContextCompat.getColor(getActivity(), R.color.md_yellow_A200),
                        ContextCompat.getColor(getActivity(), R.color.about_libraries_card));
                programFab1.setLabelTextColor(ContextCompat.getColor(getActivity(), R.color.theme_accent));
            }
        });

        menuGreen.hideMenuButton(false);
        menus.add(menuGreen);
        fab4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fab5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        fab6.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        int delay = 400;
        for (final FloatingActionMenu menu : menus) {
            mUiHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    menu.showMenuButton(true);
                }
            }, delay);
            delay += 150;
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //fabEdit.show(true);
            }
        }, delay + 150);
        createCustomAnimation();
    }

    private void createCustomAnimation() {
        AnimatorSet set = new AnimatorSet();

        ObjectAnimator scaleOutX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 1.0f, 0.2f);
        ObjectAnimator scaleOutY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 1.0f, 0.2f);

        ObjectAnimator scaleInX = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleX", 0.2f, 1.0f);
        ObjectAnimator scaleInY = ObjectAnimator.ofFloat(menuGreen.getMenuIconView(), "scaleY", 0.2f, 1.0f);

        scaleOutX.setDuration(50);
        scaleOutY.setDuration(50);

        scaleInX.setDuration(150);
        scaleInY.setDuration(150);

        scaleInX.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                menuGreen.getMenuIconView().setImageResource(menuGreen.isOpened()
                        ? R.mipmap.ic_close : R.mipmap.ic_star);
            }
        });

        set.play(scaleOutX).with(scaleOutY);
        set.play(scaleInX).with(scaleInY).after(scaleOutX);
        set.setInterpolator(new OvershootInterpolator(2));

        menuGreen.setIconToggleAnimatorSet(set);
    }


}
