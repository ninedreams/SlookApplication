package cn.panorama.slook.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.ZoomControls;

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

/**
 * Created by xingyaoma on 16-4-29.
 */
public class
PanoramaActivity extends PLView {
    /**member variables*/

    private ZoomControls mZoomControls;

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
        return super.onContentViewCreated(mainView);
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
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.final_f), false), PLCubeFaceOrientation.PLCubeFaceOrientationFront);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.final_b), false), PLCubeFaceOrientation.PLCubeFaceOrientationBack);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.final_l), false), PLCubeFaceOrientation.PLCubeFaceOrientationLeft);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.final_r), false), PLCubeFaceOrientation.PLCubeFaceOrientationRight);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.final_u), false), PLCubeFaceOrientation.PLCubeFaceOrientationUp);
            cubicPanorama.setImage(new PLImage(PLUtils.getBitmap(context, R.raw.final_d), false), PLCubeFaceOrientation.PLCubeFaceOrientationDown);
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

    public void execFloatingActionButton(){
        FloatingActionButton pano_fab = (FloatingActionButton) findViewById(R.id.pano_fab);
        if (pano_fab != null) {
            pano_fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
            });
        }
    }

}
