package cn.panorama.slook.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ScrollView;

import com.amap.api.location.AMapLocation;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

import cn.panorama.slook.AppManager;
import cn.panorama.slook.adapter.LabelAdapter;
import cn.panorama.slook.control.ALocationController;
import cn.panorama.slook.control.AMapQueryer;
import cn.panorama.slook.control.AMapQueryer.OnInputTipsQueryListener;
import cn.panorama.slook.control.APoiSearcher;
import cn.panorama.slook.control.APoiSearcher.APoiSearchListener;
import cn.panorama.slook.data.DatabaseManager;
import cn.panorama.slook.data.GeoPointDao;
import cn.panorama.slook.model.GeoPoint;
import cn.panorama.slook.model.LabelModel;
import cn.panorama.slook.ui.MapActivity;
import cn.panorama.slook.ui.MixActivity;
import cn.panorama.slook.ui.R;
import cn.panorama.slook.ui.UserDefinedPointActivity;
import cn.panorama.slook.utils.CollectedLabelManager;
import cn.panorama.slook.utils.PoiTypeMatcher;
import cn.panorama.slook.utils.TimerUtil;
import cn.panorama.slook.utils.ToastUtil;
import cn.panorama.slook.view.BottomBtnDialog;
import cn.panorama.slook.view.GeoPointInfoDialog;
import cn.panorama.slook.view.GeoPointInfoDialog.OnInputConfirmListener;
import cn.panorama.slook.view.IconEditText;
import cn.panorama.slook.view.MGridView;
import cn.panorama.slook.view.RippleLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;

import static cn.panorama.slook.Constants.EVENT_ACTIVITY_START_AR;
import static cn.panorama.slook.Constants.EVENT_ACTIVITY_START_MAP;
import static cn.panorama.slook.Constants.EVENT_ACTIVITY_START_USER_DEFINED_POINT;
import static cn.panorama.slook.Constants.EVENT_BOTTOM_DIALOG_SHOW;
import static cn.panorama.slook.Constants.EVENT_MGRIDVIEVW_ARISE;
import static cn.panorama.slook.Constants.EVENT_SCROLL_DOWN;
import static cn.panorama.slook.Constants.EVENT_SEARCH_POI;
import static cn.panorama.slook.Constants.IS_DEBUG;
import static cn.panorama.slook.Constants.ITEM_LABEL_BANK;
import static cn.panorama.slook.Constants.ITEM_LABEL_BUS;
import static cn.panorama.slook.Constants.ITEM_LABEL_MARKET;
import static cn.panorama.slook.Constants.ITEM_LABEL_STORE;
import static cn.panorama.slook.Constants.ITEM_LABEL_VIEWSPOT;
import static cn.panorama.slook.Constants.ITEM_LABEL_WC;
import static cn.panorama.slook.Constants.ITEM_LABLE_ENTERTAINMENT;
import static cn.panorama.slook.Constants.ITEM_LABLE_FOOD;
import static cn.panorama.slook.Constants.ITEM_LABLE_HOTEL;
import static cn.panorama.slook.Constants.NO_RESULT;
import static cn.panorama.slook.Constants.NUM_ITEM_LABEL;
import static cn.panorama.slook.Constants.VALUE_ALPHA_INIT_MGRIDVIEW;

/**
 * Created by xingyaoma on 16-8-5.
 */
public class ARFragment extends BaseARFragment implements OnClickListener,
         APoiSearchListener, OnInputTipsQueryListener,
        OnInputConfirmListener {

    public static final String TAG = ARFragment.class.getSimpleName();

    private View view;

    private static final String KEY_TITLE = "title";

    // -------- 界面相关 --------
	/* 搜索标签网格.. */
    private MGridView mGridView;
    /* 搜索标签适配器.. */
    private LabelAdapter mLabelApdater;
    /* 标签集合.. */
    private List<LabelModel> mLabelList;
    /* 搜索标签网格 滚动视图 . */
    private ScrollView mScrollView;

    /* 搜索栏.. */
    private IconEditText mEditText;

    /* 工具栏 . */
	/* 地图层按钮.. */
    private RippleLayout mMapBtn;
    /* 实景层按钮 . */
    private RippleLayout mCameraBtn;
    /* 添加搜索标签 . */
    //private ImageView mAddBtn;

    /* 进度条对话框 . */
    private SweetAlertDialog mProgressDialog;

    /* 底部对话框 . */
    private BottomBtnDialog mBottomBtnDialog;

    /* 地理信息点输入对空框 . */
    private GeoPointInfoDialog mGeoPointInfoDialog;

    // -------- 业务相关 --------

    /* 退出程序判定. */
    private AtomicBoolean isExit = new AtomicBoolean(false);

    /* Poi点搜索. */
    private APoiSearcher mPoiSearcher;

    /* 高德搜索组件 . */
    private AMapQueryer mMapQueryer;

    /* 数据操作锁 . */
    private AtomicBoolean mAffairLock = new AtomicBoolean(false);



    public ARFragment() {
        // Required empty public constructor
    }

    public static ARFragment newInstance() {
        return new ARFragment();
    }

    public static ARFragment newInstance(String title) {
        ARFragment f = new ARFragment();

        Bundle args = new Bundle();

        args.putString(KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }


    // ------------------------ 初始化视图 ------------------------
    /**
     * 初始化视图组件
     */
    private void initView() {

        mBottomBtnDialog = new BottomBtnDialog(getActivity());
        mGeoPointInfoDialog = new GeoPointInfoDialog(getActivity());

        // ---初始化视图适配器---
        initViewApdater();

        // ---注册视图监听器---
        registerViewListener();

    }

    /**
     * 初始化视图适配器
     */
    private void initViewApdater() {
        // ----初始化搜索标签----
        // 获取标签数据
        String[] item_name = { ITEM_LABLE_FOOD, ITEM_LABLE_HOTEL,
                ITEM_LABEL_BUS, ITEM_LABEL_BANK, ITEM_LABLE_ENTERTAINMENT,
                ITEM_LABEL_MARKET, ITEM_LABEL_VIEWSPOT, ITEM_LABEL_STORE,
                ITEM_LABEL_WC };
        int[] item_image = { R.mipmap.ic_main_food, R.mipmap.ic_main_hotel,
                R.mipmap.ic_main_bus, R.mipmap.ic_main_bank,
                R.mipmap.ic_main_ktv, R.mipmap.ic_main_market,
                R.mipmap.ic_main_viewspot, R.mipmap.ic_main_store,
                R.mipmap.ic_main_wc };

        // 添加Label数据
        mLabelList = new ArrayList<LabelModel>();
        for (int i = 0; i < NUM_ITEM_LABEL; i++) {
            mLabelList.add(new LabelModel(item_image[i], item_name[i]));
        }
        // 获取已收藏的搜索标签
        int collectedLabelNums = CollectedLabelManager.getInstance(getActivity())
                .getLabelNums();
        if (0 < collectedLabelNums) {
            for (int i = 1; i <= collectedLabelNums; i++) {
                String labelName = CollectedLabelManager.getInstance(getActivity())
                        .load(i);
                mLabelList.add(new LabelModel(R.mipmap.ic_main_like,
                        labelName));
            }
        }

        // 生成适配器
        mLabelApdater = new LabelAdapter(getActivity(), LabelAdapter.unpack(mLabelList));
        mGridView.setAdapter(mLabelApdater);

        // 无定位信息,标签网格不能使用
        if (null == mLocationPoint.getLatLng()) {
            mGridView.setAlpha(VALUE_ALPHA_INIT_MGRIDVIEW);
            mGridView.setEnabled(false);
            // 同时搜索栏失活
            mEditText.setEnabled(false);
            // 跳转按钮失活
            mMapBtn.setEnabled(false);
            mCameraBtn.setEnabled(false);
        }
    }

    /**
     * 初始化视图监听器
     */
    private void registerViewListener() {
        // ------注册监听器-------
        mMapBtn.setOnClickListener(this);
        mCameraBtn.setOnClickListener(this);
        //mAddBtn.setOnClickListener(this);
        mGeoPointInfoDialog.setOnInputConfirmListener(this);

        // ----搜索标签视图触发响应----
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (null == mALocation)
                    return;
                String labelName = mLabelList.get(position).getName().trim();
                String poiType = PoiTypeMatcher.getPoiType(labelName);

                //mProgressDialog.show();
                // 清空之前PoiResult
                mPoiSearchData.clearPois();
                // 搜索Poi
                if (null != poiType) {
                    // 通过分类搜索Poi
                    mPoiSearcher.searchNearbyType(mALocation, poiType,
                            mPoiSearchData.getRadius());
                } else {
                    // 通过关键字搜索Poi
                    mPoiSearcher.searchNearbyKeyword(mALocation, labelName,
                            mPoiSearchData.getRadius());
                }
            }
        });

        // ----监听搜索栏输入----
        mEditText.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (KeyEvent.KEYCODE_ENTER == keyCode) {
                    // 用户输入完成
                    String keyword = mEditText.getText().toString();
                    mEditText.setText("");
                    if (null == keyword || "".equals(keyword))
                        return false;

                    mProgressDialog.show();
                    // 清空之前PoiResult
                    mPoiSearchData.clearPois();
                    // 关键字搜索Poi
                    mPoiSearcher.searchNearbyKeyword(mALocation, keyword,
                            mPoiSearchData.getRadius());
                    return true;
                }
                return false;
            }
        });

        // 注册搜索栏图标触发响应
        mEditText.setOnIconClickedListener(new IconEditText.OnIconClickedListener() {

            @Override
            public void onVoiceStart() {
                mVoiceController.startListeningByDialog(getActivity());
            }

            @Override
            public void onSearchStart() {
                String keyword = mEditText.getText().toString();
                if (null == keyword || "".equals(keyword))
                    return;

                mProgressDialog.show();
                // 清空之前PoiResult
                mPoiSearchData.clearPois();
                // 关键字搜索Poi
                mPoiSearcher.searchNearbyKeyword(mALocation, keyword,
                        mPoiSearchData.getRadius());
                mEditText.setText("");
            }
        });

        // 搜索栏文本改变监听
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                String mText = s.toString().trim();
                if (null == mText || 0 == mText.length())
                    return;
                // 进行Poi提示字搜索
                mMapQueryer.searchPoiInputTips(mText, mLocationPoint.getCity());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Poi搜索
        mPoiSearcher = new APoiSearcher(getActivity().getApplicationContext());
        mPoiSearcher.setAPoiSearchListener(this);

        // Poi提示字搜索
        mMapQueryer = new AMapQueryer(getActivity().getApplicationContext());
        mMapQueryer.setOnInputTipsQueryListener(this);
        if (IS_DEBUG) {
            Log.d(TAG, "--OnCreated()--");
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        this.view=inflater.inflate(R.layout.fragment_ar, container, false);

        // ---初始化视图组件---
        // 搜索标签
        mGridView = (MGridView) view.findViewById(R.id.id_mgridView_nineGridView);
        mScrollView = (ScrollView) view.findViewById(R.id.id_scrollView_nineGridView);

        // 搜索栏
        mEditText = (IconEditText) view.findViewById(R.id.id_editText_search);

        // 工具栏
        mMapBtn = (RippleLayout) view.findViewById(R.id.id_rippleLayout_toolBar_mapBtn);
        mCameraBtn = (RippleLayout) view.findViewById(R.id.id_rippleLayout_toolBar_cameraBtn);
        //mAddBtn = (ImageView) view.findViewById(R.id.id_imageView_addBtn);


        // 初始化对话框
        mProgressDialog = new SweetAlertDialog(getActivity().getApplicationContext(),
                SweetAlertDialog.PROGRESS_TYPE);


        // 初始化布局组件
        initView();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (IS_DEBUG) {
            Log.d(TAG, "--OnStarted()--");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (IS_DEBUG) {
            Log.d(TAG, "--OnResumed()--");
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mAffairLock.set(false);
        if (IS_DEBUG) {
            Log.d(TAG, "--OnPaused()--");
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (IS_DEBUG) {
            Log.d(TAG, "--OnStoped()--");
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (IS_DEBUG) {
            Log.d(TAG, "--OnDestroyed()--");
        }
    }


    // ------------------------ 业务逻辑 ------------------------
    /**
     * MHandler:处理子线程分发的事件
     *
     */
    private MHandler mHandler = new MHandler(this);

    static class MHandler extends Handler {

        private WeakReference<ARFragment> mFragment;

        public MHandler(ARFragment mFragment) {
            this.mFragment = new WeakReference<ARFragment>(mFragment);
        }

        /**
         * 处理消息
         *
         * @param msg
         */
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case EVENT_SCROLL_DOWN:
                    // 滚动视图下拉
                    mFragment.get().mScrollView.fullScroll(View.FOCUS_DOWN);
                    break;

                case EVENT_MGRIDVIEVW_ARISE:
                    // 标签网格视图显示
                    Animation mAlphaAnim = new AlphaAnimation(
                            VALUE_ALPHA_INIT_MGRIDVIEW, 1.0f);
                    mAlphaAnim.setDuration(2000);
                    mFragment.get().mGridView.startAnimation(mAlphaAnim);
                    mFragment.get().mGridView.setAlpha(1.0f);
                    mFragment.get().mGridView.setEnabled(true);
                    // 同时激活搜索栏
                    mFragment.get().mEditText.setEnabled(true);
                    // 激活跳转Activity按钮
                    mFragment.get().mMapBtn.setEnabled(true);
                    mFragment.get().mCameraBtn.setEnabled(true);
                    break;

                case EVENT_SEARCH_POI:
                    // 搜索Poi结果处理
                    List<PoiItem> pois = mFragment.get().mPoiSearchData.getPois();
                    if (null == pois || 0 == pois.size()) {
                        ToastUtil.showShort(NO_RESULT);
                        break;
                    }
                    // 直接启动MixActivity
                case EVENT_ACTIVITY_START_AR:
                    // 启动ARActivity
                    Intent arIntent = new Intent(mFragment.get().getActivity().getApplicationContext(), MixActivity.class);
                    mFragment.get().startActivity(arIntent);
                    break;

                case EVENT_ACTIVITY_START_MAP:
                    // 启动MapActivity
                    mFragment.get().mPoiSearchData.clearPois();
                    Intent mapIntent = new Intent(mFragment.get().getActivity().getApplicationContext(), MapActivity.class);
                    mFragment.get().startActivity(mapIntent);
                    break;

                case EVENT_BOTTOM_DIALOG_SHOW:
                    mFragment.get().mBottomBtnDialog.show();
                    mFragment.get().mBottomBtnDialog.setItem1Txt("当前位置");
                    mFragment.get().mBottomBtnDialog.setItem2Txt("地图选点");
                    mFragment.get().mBottomBtnDialog.setOnClickListener(mFragment
                            .get());
                    break;
                case EVENT_ACTIVITY_START_USER_DEFINED_POINT:
                    Intent geoPointSignIntent = new Intent(mFragment.get().getActivity()
                            .getApplicationContext(),
                            UserDefinedPointActivity.class);
                    mFragment.get().startActivity(geoPointSignIntent);
                    break;
            }
        }
    }

    // ------------------------ 响应事件 ------------------------
    /**
     * 主界面里按钮点击响应
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_rippleLayout_toolBar_mapBtn:
                // 水波纹动画结束后跳转至MapActivity
                //mMapBtn.showRipple();
                TimerUtil.schedule(mHandler, EVENT_ACTIVITY_START_MAP,
                        mMapBtn.getAnimDuration());
                break;
            case R.id.id_rippleLayout_toolBar_cameraBtn:
                //mCameraBtn.showRipple();
                TimerUtil.schedule(mHandler, EVENT_ACTIVITY_START_AR,
                        mMapBtn.getAnimDuration());
                break;

//            case R.id.id_imageView_addBtn:
////                // 添加搜索标签 弹出输入对话框
//                SweetAlertDialog mInputDialog = new SweetAlertDialog(getActivity(),
//                        SweetAlertDialog.INPUT_TYPE);
//                mInputDialog.setConfirmClickListener(this);
//                mInputDialog.show();
//                break;
            case R.id.id_textView_item1:
                // 标记当前位置信息
                mBottomBtnDialog.dismiss();
                mGeoPointInfoDialog.show();
                break;
            case R.id.id_textView_item2:
                mBottomBtnDialog.dismiss();
                TimerUtil.schedule(mHandler,
                        EVENT_ACTIVITY_START_USER_DEFINED_POINT,
                        mMapBtn.getAnimDuration());
                break;
        }
    }

    /**
     * 对话框确认按钮响应
     */
//    @Override
//    public void onClick(SweetAlertDialog sweetAlertDialog) {
//        int dialogType = sweetAlertDialog.getAlerType();
//        switch (dialogType) {
//            case SweetAlertDialog.INPUT_TYPE:
//                // --- 输入对话框 ---
//                // 添加新搜索标签
//                String inputText = sweetAlertDialog.getInputText();
//                if (null != inputText && !"".equals(inputText)) {
//                    ToastUtil.showShort("已收藏标签");
//                    // 适配器添加新标签
//                    LabelModel newLabelModel = new LabelModel(
//                            R.mipmap.ic_main_like, inputText);
//                    mLabelList.add(newLabelModel);
//                    mLabelApdater.addLabelModel(newLabelModel);
//                    mLabelApdater.notifyDataSetChanged();
//
//                    // 保存已收藏的搜索标签至本地
//                    CollectedLabelManager.getInstance(getActivity()).store(inputText);
//
//                    // 滚动视图下拉 显示新标签
//                    TimerUtil.schedule(mHandler, EVENT_SCROLL_DOWN,
//                            TIME_WAIT_SCROLL_DOWN);
//                }
//                break;
//            case SweetAlertDialog.SEEK_TYPE:
//                // 拖动条对话框
//                // 设置搜索半径
//                mPoiSearchData.setRadius(sweetAlertDialog.getRadius());
//                break;
//        }
//        sweetAlertDialog.dismiss();
//    }

    /**
     * 定位信息更新回调
     */
    @Override
    public void onLocationSucceeded(AMapLocation amapLocation) {
        super.onLocationSucceeded(amapLocation);
        // 去掉省会级的地址信息
        String address = mLocationPoint.getAddress();
        if (null != address) {
            if (address.length() >= 3)
                address = address.substring(3);
        } else {
            address = "";
        }

        if (ALocationController.Is_Frist_Locate
                && AppManager.getInstance().getNetConnectedState()) {
            // 显示标签网格
            if (!mGridView.isEnabled())
                mHandler.sendEmptyMessage(EVENT_MGRIDVIEVW_ARISE);
            ToastUtil.showShort("当前位置:" + address);
            ALocationController.Is_Frist_Locate = false;
        }
    }

    /**
     * Poi点搜索信息更新回调
     */
    @Override
    public void onPoiSearched(PoiResult result) {
        final PoiResult fresult = result;

        // 延迟500ms再隐藏进度对话框
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                mProgressDialog.dismiss();

                if (null == fresult) {
                    return;
                }
                mPoiSearchData.setPois(fresult.getPois());
                // 处理Poi搜索
                mHandler.sendEmptyMessage(EVENT_SEARCH_POI);
            }
        }, 500);
    }

    /**
     * 语音识别回调结果
     */
    @Override
    public void onResult(String result) {
        super.onResult(result);
        mEditText.setText(result);
    }

    /**
     * Poi提示字搜索回调
     */
    @Override
    public void onGetInputtips(List<String> nameList) {
        if (null == nameList || 0 == nameList.size())
            return;
        ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(getActivity(),
                R.layout.item_poi_tip, nameList);
        mEditText.setAdapter(aAdapter);
        aAdapter.notifyDataSetChanged();
    }

    /**
     * 标记地点输入备注信息确认回调
     */
    @Override
    public synchronized void onInputeConfirm(String inputTxt) {
        if (null == inputTxt)
            return;

        if (mAffairLock.get())
            return;
        mProgressDialog.show();
        mAffairLock.set(true);

        final GeoPoint point = mLocationPoint.clone();
        point.setName(inputTxt);

        new Timer(true).schedule(new TimerTask() {
            @Override
            public void run() {
                GeoPointDao mGeoPointDao = DatabaseManager.getInstance(
                        getActivity()).getGeoPointDao(); // 数据库已在AppManager层打开
                mGeoPointDao.addGeoPoint(point); // 向数据库提交当前位置点

                mProgressDialog.dismiss();

                mAffairLock.set(false);
            }
        }, 500);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Log.i(TAG, "onActivityCreated");
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }



    public static Fragment newInstance(int position) {
        Fragment fragment = new ARFragment();
        return fragment;
    }

}
