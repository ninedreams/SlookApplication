package cn.panorama.slook.ui;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.mikepenz.fastadapter.utils.RecyclerViewCacheUtil;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.iconics.IconicsDrawable;
import com.mikepenz.itemanimators.AlphaCrossFadeAnimator;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.holder.StringHolder;
import com.mikepenz.materialdrawer.interfaces.OnCheckedChangeListener;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ExpandableDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileSettingDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.ToggleDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.mikepenz.materialdrawer.model.interfaces.Nameable;
import com.mikepenz.octicons_typeface_library.Octicons;

import java.util.ArrayList;

import cn.panorama.slook.adapter.FragmentAdapter;
import cn.panorama.slook.utils.BottomNavigatorView;


public class MainActivity extends AppCompatActivity implements BottomNavigatorView.OnBottomNavigatorViewItemClickListener {


    //底部
    private static final int DEFAULT_POSITION = 0;
    private FragmentNavigator mNavigator;
    private BottomNavigatorView bottomNavigatorView;

    //顶部
    Toolbar toolbar;
    private static final int PROFILE_SETTING = 100000;
    private MaterialSearchView searchView;

    //save our header or result
    private AccountHeader headerResult = null;
    private Drawer result = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //底部
        mNavigator = new FragmentNavigator(getSupportFragmentManager(), new FragmentAdapter(), R.id.fragment_container);
        mNavigator.setDefaultPosition(DEFAULT_POSITION);
        mNavigator.onCreate(savedInstanceState);

        bottomNavigatorView = (BottomNavigatorView) findViewById(R.id.bottomNavigatorView);
        if (bottomNavigatorView != null) {
            bottomNavigatorView.setOnBottomNavigatorViewItemClickListener(this);
        }
        setCurrentTab(mNavigator.getCurrentPosition());

        //Remove line to test RTL support
        //getWindow().getDecorView().setLayoutDirection(View.LAYOUT_DIRECTION_RTL);

        // Handle Toolbar
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        searchView = (MaterialSearchView) this.findViewById(R.id.search_view_main);
        searchView.setVoiceSearch(true);
        searchView.setCursorDrawable(R.drawable.custom_cursor);
        searchView.setSuggestions(getResources().getStringArray(R.array.query_suggestions));
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //Do some magic
                return false;
            }
        });

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                //Do some magic
            }

            @Override
            public void onSearchViewClosed() {
                //Do some magic
            }
        });


        // Create a few sample profile
        // NOTE you have to define the loader logic too. See the CustomApplication for more details
        final IProfile profile = new ProfileDrawerItem().withName("ninedreams").withEmail("xingyaoma@gmail.com").withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100);

        // Create the AccountHeader
        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.header)
                .addProfiles(
                        profile,
                        //don't ask but google uses 14dp for the add account icon in gmail but 20dp for the normal icons (like manage account)
                        new ProfileSettingDrawerItem().withName("Add Account").withDescription("Add new GitHub Account").withIcon(new IconicsDrawable(this, GoogleMaterial.Icon.gmd_plus_one).actionBar().paddingDp(5).colorRes(R.color.material_drawer_primary_text)).withIdentifier(PROFILE_SETTING),
                        new ProfileSettingDrawerItem().withName("Manage Account").withIcon(GoogleMaterial.Icon.gmd_settings).withIdentifier(100001)
                )
                .withOnAccountHeaderListener(new AccountHeader.OnAccountHeaderListener() {
                    @Override
                    public boolean onProfileChanged(View view, IProfile profile, boolean current) {
                        //sample usage of the onProfileChanged listener
                        //if the clicked item has the identifier 1 add a new profile ;)
                        if (profile instanceof IDrawerItem && profile.getIdentifier() == PROFILE_SETTING) {
                            int count = 100 + headerResult.getProfiles().size() + 1;
                            IProfile newProfile = new ProfileDrawerItem().withNameShown(true).withName("Batman" + count).withEmail("batman" + count + "@gmail.com").withIcon(R.drawable.profile).withIdentifier(count);
                            if (headerResult.getProfiles() != null) {
                                //we know that there are 2 setting elements. set the new profile above them ;)
                                headerResult.addProfile(newProfile, headerResult.getProfiles().size() - 2);
                            } else {
                                headerResult.addProfiles(newProfile);
                            }
                        }

                        //false if you have not consumed the event and it should close the drawer
                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .build();

        //Create the drawer
        result = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withHasStableIds(true)
                .withItemAnimator(new AlphaCrossFadeAnimator())
                .withAccountHeader(headerResult) //set the AccountHeader we created earlier for the header
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.vr).withIcon(GoogleMaterial.Icon.gmd_ac_unit).withIdentifier(0).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.buytickets).withIcon(GoogleMaterial.Icon.gmd_ac_unit).withIdentifier(1).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.frineds).withIcon(FontAwesome.Icon.faw_home).withIdentifier(2).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.share).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(3).withSelectable(false),
                        new PrimaryDrawerItem().withName(R.string.collect).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(4).withSelectable(false),
                        new SectionDrawerItem().withName(R.string.navigation_settings),
                        new ExpandableDrawerItem().withName(R.string.language).withIcon(GoogleMaterial.Icon.gmd_battery_std).withIdentifier(6).withSelectable(false).withSubItems(
                                new SecondaryDrawerItem().withName(R.string.chinese).withSelectable(true).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_airline_seat_recline_normal).withIdentifier(2000),
                                new SecondaryDrawerItem().withName(R.string.english).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_airplay).withIdentifier(2001),
                                new SecondaryDrawerItem().withName(R.string.german).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_airplay).withIdentifier(2002),
                                new SecondaryDrawerItem().withName(R.string.french).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_airplay).withIdentifier(2003),
                                new SecondaryDrawerItem().withName(R.string.japanese).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_airplay).withIdentifier(2004),
                                new SecondaryDrawerItem().withName(R.string.korean).withLevel(2).withIcon(GoogleMaterial.Icon.gmd_airplay).withIdentifier(2005)
                        ),
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_open_source).withIcon(FontAwesome.Icon.faw_github).withIdentifier(7).withSelectable(false),
                        //new SecondaryDrawerItem().withName(R.string.drawer_item_contact).withIcon(GoogleMaterial.Icon.gmd_format_color_fill).withIdentifier(8).withTag("Bullhorn"),
                        new DividerDrawerItem(),
                        new SwitchDrawerItem().withName(R.string.navigation_wifi_download).withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        new ToggleDrawerItem().withName(R.string.navigation_light_night_module).withIcon(Octicons.Icon.oct_tools).withChecked(false).withOnCheckedChangeListener(onCheckedChangeListener),
                        new PrimaryDrawerItem().withName(R.string.feedback).withIcon(FontAwesome.Icon.faw_gamepad).withIdentifier(5).withSelectable(false)
                        //new SecondarySwitchDrawerItem().withName("Secondary switch").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener),
                        //new SecondaryToggleDrawerItem().withName("Secondary toggle").withIcon(Octicons.Icon.oct_tools).withChecked(true).withOnCheckedChangeListener(onCheckedChangeListener)
                ) // add the items we want to use with our Drawer
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        //check if the drawerItem is set.
                        //there are different reasons for the drawerItem to be null
                        //--> click on the header
                        //--> click on the footer
                        //those items don't contain a drawerItem
                        skipActivity(view, position, drawerItem);

                        return false;
                    }
                })
                .withSavedInstance(savedInstanceState)
                .withShowDrawerOnFirstLaunch(true)
                .build();

        //if you have many different types of DrawerItems you can magically pre-cache those items to get a better scroll performance
        //make sure to init the cache after the DrawerBuilder was created as this will first clear the cache to make sure no old elements are in
        //RecyclerViewCacheUtil.getInstance().withCacheSize(2).init(result);
        new RecyclerViewCacheUtil<IDrawerItem>().withCacheSize(2).apply(result.getRecyclerView(), result.getDrawerItems());

        //only set the active selection or active profile if we do not recreate the activity
        if (savedInstanceState == null) {
            // set the selection to the item with the identifier 11
            result.setSelection(21, false);

            //set the active profile
            headerResult.setActiveProfile(profile);
        }

        result.updateBadge(4, new StringHolder(10 + ""));


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MaterialSearchView.REQUEST_VOICE && resultCode == RESULT_OK) {
            ArrayList<String> matches = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            if (matches != null && matches.size() > 0) {
                String searchWrd = matches.get(0);
                if (!TextUtils.isEmpty(searchWrd)) {
                    searchView.setQuery(searchWrd, false);
                }
            }

            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);

        return true;
    }

    private OnCheckedChangeListener onCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem drawerItem, CompoundButton buttonView, boolean isChecked) {
            if (drawerItem instanceof Nameable) {
                Log.i("material-drawer", "DrawerItem: " + ((Nameable) drawerItem).getName() + " - toggleChecked: " + isChecked);
            } else {
                Log.i("material-drawer", "toggleChecked: " + isChecked);
            }
        }
    };

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //add the values which need to be saved from the drawer to the bundle
        outState = result.saveInstanceState(outState);
        //add the values which need to be saved from the accountHeader to the bundle
        outState = headerResult.saveInstanceState(outState);
        //add the values form fragmentnavigator
        mNavigator.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onBackPressed() {
        //handle the back press :D close the drawer first and if the drawer is closed close the activity
        if (result != null && result.isDrawerOpen()) {
            result.closeDrawer();
        }
        else if(searchView.isSearchOpen()) {
            searchView.closeSearch();
        }
        else
        {
            super.onBackPressed();
        }

    }


    /*
    **返回addDrawerItems所需要的DrawerItems组
     */
//    public PrimaryDrawerItem setDrawerItems(){
//
//    }

    private void skipActivity(View view, int position, IDrawerItem drawerItem){
        if (drawerItem != null) {
            Intent intent = null;

            if (drawerItem.getIdentifier() == 1) {
                intent = new Intent(MainActivity.this, BuyTicketsActivity.class);
            } else if (drawerItem.getIdentifier() == 2) {
                intent = new Intent(MainActivity.this, FriendActivity.class);
            } else if (drawerItem.getIdentifier() == 3) {
                intent = new Intent(MainActivity.this, SharedActivity.class);
            } else if (drawerItem.getIdentifier() == 4) {
                intent = new Intent(MainActivity.this, CollectActivity.class);
            } else if (drawerItem.getIdentifier() == 5) {
                intent = new Intent(MainActivity.this, FeedbackActivity.class);
            } else if (drawerItem.getIdentifier() == 2000) {
                //intent = new Intent(DrawerActivity.this, EmbeddedDrawerActivity.class);
            } else if (drawerItem.getIdentifier() == 2001) {
                //intent = new Intent(DrawerActivity.this, FullscreenDrawerActivity.class);
            } else if (drawerItem.getIdentifier() == 2002) {
                //intent = new Intent(DrawerActivity.this, CustomContainerActivity.class);
            } else if (drawerItem.getIdentifier() == 2003) {
                //intent = new Intent(DrawerActivity.this, MenuDrawerActivity.class);
            } else if (drawerItem.getIdentifier() == 2004) {
                //intent = new Intent(DrawerActivity.this, MiniDrawerActivity.class);
            } else if (drawerItem.getIdentifier() == 2005) {
                //intent = new Intent(DrawerActivity.this, FragmentActivity.class);
            } else if (drawerItem.getIdentifier() == 13) {
                //intent = new Intent(DrawerActivity.this, CollapsingToolbarActivity.class);
            } else if (drawerItem.getIdentifier() == 14) {
                //intent = new Intent(DrawerActivity.this, PersistentDrawerActivity.class);
            } else if (drawerItem.getIdentifier() == 15) {
                //intent = new Intent(DrawerActivity.this, CrossfadeDrawerLayoutActvitiy.class);
            } else if (drawerItem.getIdentifier() == 20) {
                //intent = new LibsBuilder()
                //        .withFields(R.string.class.getFields())
                 //       .withActivityStyle(Libs.ActivityStyle.LIGHT_DARK_TOOLBAR)
                 //       .intent(DrawerActivity.this);
            }else if (drawerItem.getIdentifier() == 0) {
                intent = new Intent(MainActivity.this, MixActivity.class);
            }
            if (intent != null) {
                MainActivity.this.startActivity(intent);
            }
        }
    }


    /*
    *底部navigation的点击事件，切换
     */
    @Override
    public void onBottomNavigatorViewItemClick(int position, View view) {
        setCurrentTab(position);
    }

    private void setCurrentTab(int position) {
        mNavigator.showFragment(position);
        bottomNavigatorView.select(position);
    }


}
