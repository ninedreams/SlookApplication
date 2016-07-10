package cn.panorama.slook.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.speech.RecognizerIntent;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.github.rubensousa.floatingtoolbar.FloatingToolbar;
import com.lapism.searchview.SearchAdapter;
import com.lapism.searchview.SearchHistoryTable;
import com.lapism.searchview.SearchItem;
import com.lapism.searchview.SearchView;
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

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import cn.panorama.slook.adapter.FabAdapter;

/**
 * Created by xingyaoma on 16-4-29.
 */
public class PanoramaActivity extends PLView {
    /**member variables*/

    private ZoomControls mZoomControls;

    protected static final String EXTRA_KEY_VERSION = "version";
    protected static final String EXTRA_KEY_THEME = "theme";
    private static final String EXTRA_KEY_VERSION_MARGINS = "version_margins";
    private static final String EXTRA_KEY_TEXT = "text";
    protected DrawerLayout mDrawerLayout = null;
    private SearchHistoryTable mHistoryDatabase;
    private SearchView mSearchView;

    //浮动按钮
    private FloatingToolbar mFloatingToolbar;
    private FabAdapter mAdapter;

    private ProgressBar progressBar;
    private static Handler sHandler = new Handler(Looper.getMainLooper());
    private WeakRunnable mRunnable = new WeakRunnable(this);
    private static final int MOCK_LOAD_DATA_DELAYED_TIME = 2000;

    private String jsonUrl;//由其他activity点击进入时传递的jsonurl

    /**init methods*/

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        //初始化
        progressBar = (ProgressBar) findViewById(R.id.progressBar_pano);

        //滚动条,加载数据
        loadData();

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

        //mSearchView = (SearchView) findViewById(R.id.searchView);
        //setSearchView();
        mSearchView.setText(R.string.search);
        mSearchView.setOnMenuClickListener(new SearchView.OnMenuClickListener() {
            @Override
            public void onMenuClick() {
                finish();
            }
        });

        //customSearchView();
        mSearchView.open(false);

    }

    protected void setSearchView() {
        mHistoryDatabase = new SearchHistoryTable(this);

        //mSearchView = (SearchView) findViewById(R.id.searchView);
        if (mSearchView != null) {
            mSearchView.setVersion(SearchView.VERSION_TOOLBAR);
            mSearchView.setVersionMargins(SearchView.VERSION_MARGINS_TOOLBAR_BIG);
            mSearchView.setHint(R.string.search);
            mSearchView.setTextSize(16);
            mSearchView.setDivider(false);
            mSearchView.setVoice(true);
            mSearchView.setVoiceText("Set permission on Android 6+ !");
            mSearchView.setAnimationDuration(SearchView.ANIMATION_DURATION);
            mSearchView.setShadowColor(ContextCompat.getColor(this, R.color.search_shadow_layout));
            mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    //getData(query, 0);
                    // mSearchView.close(false);
                    return true;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    return false;
                }
            });
            mSearchView.setOnOpenCloseListener(new SearchView.OnOpenCloseListener() {
                @Override
                public void onOpen() {

                }

                @Override
                public void onClose() {

                }
            });

            List<SearchItem> suggestionsList = new ArrayList<>();
            suggestionsList.add(new SearchItem("search1"));
            suggestionsList.add(new SearchItem("search2"));
            suggestionsList.add(new SearchItem("search3"));

            SearchAdapter searchAdapter = new SearchAdapter(this, suggestionsList);
            searchAdapter.setOnItemClickListener(new SearchAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    TextView textView = (TextView) view.findViewById(R.id.textView_item_text);
                    String query = textView.getText().toString();
                    //getData(query, position);
                    // mSearchView.close(false);
                }
            });
            mSearchView.setAdapter(searchAdapter);
        }
    }

    protected void customSearchView() {
        final Bundle extras = getIntent().getExtras();
        if (extras != null) {
            mSearchView.setVersion(extras.getInt(EXTRA_KEY_VERSION));
            mSearchView.setVersionMargins(extras.getInt(EXTRA_KEY_VERSION_MARGINS));
            mSearchView.setTheme(extras.getInt(EXTRA_KEY_THEME), true);
            mSearchView.setText(extras.getString(EXTRA_KEY_TEXT));
        }
    }

      //mDrawerLayout  问题可能性很大
//    @Override
//    public void onBackPressed() {
//        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)) {
//            mDrawerLayout.closeDrawer(GravityCompat.START);
//        } else {
//            finish();
//            // NAV UTILS
//        }
//    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SearchView.SPEECH_REQUEST_CODE && resultCode == RESULT_OK) {
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (results != null && results.size() > 0) {
                String searchWrd = results.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    mSearchView.setQuery(searchWrd);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
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

    @SuppressLint("NewApi")
    public void execFloatingActionButton(){

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        mFloatingToolbar = (FloatingToolbar) findViewById(R.id.floatingToolbar);

        mAdapter = new FabAdapter(new FabAdapter.ClickListener() {
            @Override
            public void onAdapterItemClick(MenuItem item) {

            }
        });

        mFloatingToolbar.setClickListener(new FloatingToolbar.ItemClickListener() {
            @Override
            public void onItemClick(MenuItem menuItem) {

            }

            @Override
            public void onItemLongClick(MenuItem menuItem) {

            }
        });
        mFloatingToolbar.attachFab(fab);

        // Usage with custom view
        View customView = mFloatingToolbar.getCustomView();
        if (customView != null) {
            customView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFloatingToolbar.hide();
                }
            });
        }

        // How to edit current menu
        Menu menu = mFloatingToolbar.getMenu();
        menu.findItem(R.id.action_copy).setVisible(true);
        mFloatingToolbar.setMenu(menu);

    }

    public void showProgressBar(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    /**
     * mock load data
     */
    private void loadData() {
        showProgressBar(true);
        sHandler.postDelayed(mRunnable, MOCK_LOAD_DATA_DELAYED_TIME);
    }

    @Override
    protected void onDestroy() {
        sHandler.removeCallbacks(mRunnable);
        progressBar = null;
        super.onDestroy();
    }

    private static class WeakRunnable implements Runnable {

        WeakReference<PanoramaActivity> mWeakReference;

        public WeakRunnable(PanoramaActivity panoramaActivity) {
            this.mWeakReference = new WeakReference<PanoramaActivity>(panoramaActivity);
        }

        @SuppressLint("NewApi")
        @Override
        public void run() {
            PanoramaActivity panoramaActivity = mWeakReference.get();
            if (panoramaActivity != null && !panoramaActivity.isDestroyed()) {
                panoramaActivity.showProgressBar(false);
            }
        }
    }

}
