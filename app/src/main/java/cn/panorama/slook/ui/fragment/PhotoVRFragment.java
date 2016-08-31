package cn.panorama.slook.ui.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;

import com.github.clans.fab.FloatingActionButton;
import com.google.vr.sdk.widgets.pano.VrPanoramaView;

import cn.panorama.slook.adapter.PhotoVRAdapter;
import cn.panorama.slook.ui.PanoVRActivity;
import cn.panorama.slook.ui.R;

/**
 * Created by xingyaoma on 16-5-1.
 * 一级分类VR下的二级分类，第一个VRphoto
 */
public class PhotoVRFragment extends Fragment {
    public static final String TAG = PhotoVRFragment.class.getSimpleName();

    private static final String KEY_TITLE = "title";

    private FloatingActionButton mFab;

    private ListView listview;
    private int mPreviousVisibleItem;
    /** Tracks the file to be loaded across the lifetime of this app. **/
    private Uri fileUri;
    /** Configuration information for the panorama. **/
    private VrPanoramaView.Options panoOptions = new VrPanoramaView.Options();

    private PhotoVRAdapter adapter;

    private View view;

    public PhotoVRFragment(){

    }

    public static PhotoVRFragment newInstance(String title) {
        PhotoVRFragment f = new PhotoVRFragment();

        Bundle args = new Bundle();

        args.putString(KEY_TITLE, title);
        f.setArguments(args);

        return (f);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        this.view =  inflater.inflate(R.layout.fragment_photovr, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        listview = (ListView) view.findViewById(R.id.listview_vrphoto);
        mFab = (FloatingActionButton) view.findViewById(R.id.fab_vrphoto);

        if(adapter == null) {
            adapter = new PhotoVRAdapter(getContext(), getActivity().getApplication(), R.id.tv_title_photovr);
        }
        listview.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =null;
                switch(position){
                    case 0:
                        fileUri = Uri.parse("115.29.144.105/images/output/ustb_westdoor/ustb_westdoor.jpg");
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        intent.setData(fileUri);
                        intent.putExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
                        intent.setAction(Intent.ACTION_VIEW);
                        break;
                    case 1:
                        fileUri = Uri.parse("http://www.ninedreams.cn/images/output/ustb_playground/ustb_playground.jpg");
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        intent.setData(fileUri);
                        intent.putExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
                        intent.setAction(Intent.ACTION_VIEW);
                        break;
                    case 2:
                        fileUri = Uri.parse("http://www.ninedreams.cn/images/output/ustb_library/ustb_library.jpg");
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        intent.setData(fileUri);
                        intent.putExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
                        intent.setAction(Intent.ACTION_VIEW);
                        break;
                    case 3:
                        fileUri = Uri.parse("115.29.144.105/images/output/beijing1/beijing1.jpg");
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        intent.setData(fileUri);
                        intent.putExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
                        intent.setAction(Intent.ACTION_VIEW);
                        break;
                    case 4:
                        fileUri = Uri.parse("115.29.144.105/images/output/ustb_westdoor/ustb_westdoor.jpg");
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        intent.setData(fileUri);
                        intent.putExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
                        intent.setAction(Intent.ACTION_VIEW);
                        break;
                    case 5:
                        fileUri = Uri.parse("http://www.ninedreams.cn/images/output/ustb_playground/ustb_playground.jpg");
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        intent.setData(fileUri);
                        intent.putExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
                        intent.setAction(Intent.ACTION_VIEW);
                        break;
                    case 6:
                        fileUri = Uri.parse("http://www.ninedreams.cn/images/output/ustb_library/ustb_library.jpg");
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        intent.setData(fileUri);
                        intent.putExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
                        intent.setAction(Intent.ACTION_VIEW);
                    case 7:
                        fileUri = Uri.parse("http://www.ninedreams.cn/images/output/beijing1/beijing1.jpg");
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        intent.setData(fileUri);
                        intent.putExtra("inputType", VrPanoramaView.Options.TYPE_MONO);
                        intent.setAction(Intent.ACTION_VIEW);
                        break;
                    case 8:
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        break;
                    case 9:
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        break;
                    case 10:
                        intent = new Intent(getActivity(), PanoVRActivity.class);
                        break;
                    default:break;
                }

                startActivity(intent);
            }
        });

        listview.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem > mPreviousVisibleItem) {
                    mFab.hide(true);
                } else if (firstVisibleItem < mPreviousVisibleItem) {
                    mFab.show(true);
                }
                mPreviousVisibleItem = firstVisibleItem;
            }
        });

        mFab.hide(false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                mFab.show(true);
                mFab.setShowAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.show_from_bottom));
                mFab.setHideAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.hide_to_bottom));
            }
        }, 300);

    }






}
