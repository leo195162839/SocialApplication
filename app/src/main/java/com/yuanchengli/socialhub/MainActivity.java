package com.yuanchengli.socialhub;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.CookieManager;
import android.webkit.ValueCallback;
import android.widget.CompoundButton;


public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private DrawerLayout mDrawer;
    private NavigationView nvDrawer;
    private ViewPager viewPager;
    private SocialTabFragmentStatePagerAdapter mAdapter;
    private Boolean isFbNotificationQuickLink = false;
    private Boolean isDMQuickLink = false;
    private Boolean isMomentQuickLink = false;
    private Boolean isNotificationQuickLink = false;
    private CompoundButton switchView;
    private MenuItem switchItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        checkDayNightMode();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle("Facebook");
        titleColorSetting();

        mDrawer = findViewById(R.id.drawer_layout);
        nvDrawer = findViewById(R.id.nvView);

        switchItem = nvDrawer.getMenu().findItem(R.id.night_mode);
        switchView = (CompoundButton)MenuItemCompat.getActionView(switchItem);
        initNavDrawerToggle();


    }


    @SuppressWarnings("deprecation")
    private void initNavDrawerToggle() {
        // Initialization
        viewPager = findViewById(R.id.pager);


        mAdapter = new SocialTabFragmentStatePagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(mAdapter);
        viewPager.setOffscreenPageLimit(2);

        //Initializing the navigation drawer
        nvDrawer.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.facebook:
                                viewPager.setCurrentItem(0,true);
                                Log.d("facebook", "touched");
                                break;
                            case R.id.twitter:
                                viewPager.setCurrentItem(1, true);
                                Log.d("twitter", "touched");
                                break;
                            case R.id.linkedin:
                                viewPager.setCurrentItem(2,true);
                                Log.d("Linkedin", "touched");
                                break;
                            case R.id.fbnotification:
                                isFbNotificationQuickLink = true;
                                viewPager.setCurrentItem(0,true);
                                Log.d("facebook notifications", "touched");
                                break;
                            case R.id.moments:
                                isMomentQuickLink = true;
                                viewPager.setCurrentItem(1, true);
                                Log.d("twitter", "notifications touched");
                                break;
                            case R.id.notifications:
                                isNotificationQuickLink = true;
                                viewPager.setCurrentItem(1, true);
                                Log.d("twitter", "moments touched");
                                break;
                            case R.id.dm:
                                isDMQuickLink = true;
                                viewPager.setCurrentItem(1, true);
                                Log.d("twitter", "dm touched");
                                break;
                            case R.id.clear_cache:
                                Log.d("setting", "Clear cache");
                                android.webkit.CookieManager cookieManager = CookieManager.getInstance();

                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    cookieManager.removeAllCookies(new ValueCallback<Boolean>() {
                                        // a callback which is executed when the cookies have been removed
                                        @Override
                                        public void onReceiveValue(Boolean aBoolean) {
                                            Log.d("test", "Cookie removed: " + aBoolean);
                                        }
                                    });
                                } else cookieManager.removeAllCookie();
                        }

                        mDrawer.closeDrawers();

                        return true;
                    }
                });


        // Initialize switch button
        Log.d("111 before changed", Boolean.toString(switchView.isChecked()));
        switchView.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Log.d("111 changed", Boolean.toString(switchView.isChecked()));
                if (switchView.isShown()) {
                    if (isChecked) {
                        Log.d("night mode 1", Boolean.toString(isChecked));
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                            MainActivity.this.recreate();
                            Log.d("theme", "night mode checked");
                        }
                    } else {
                        Log.d("night mode 2", Boolean.toString(isChecked));
                        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_NO) {
                            MainActivity.this.recreate();
                            Log.d("theme", "night mode unchecked");
                        }
                    }
                } else {
                    Log.d("111 changed", "changed by something else????");
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (position == 0) {
                    toolbar.setTitle("Facebook");
                }else if (position == 1) {
                    toolbar.setTitle("Twitter");
                }else {
                    toolbar.setTitle("Linkedin");
                }
            }

            @Override
            public void onPageSelected(int position) {
                //Fragment fragment = ((SocialTabFragmentStatePagerAdapter)viewPager.getAdapter()).getFragment(position);
                Fragment fragment = mAdapter.getFragment(position);
                Log.d("fragment3", Integer.toString(position));
                Log.d("fragment3", Boolean.toString(isDMQuickLink));

                if (position == 0) {
                    toolbar.setTitle("Facebook");
                    facebookQuickLink(fragment, isFbNotificationQuickLink);
                }else if (position == 1) {
                    toolbar.setTitle("Twitter");
                    twitterQuickLink(fragment, isMomentQuickLink, isNotificationQuickLink, isDMQuickLink);
                }else {
                    toolbar.setTitle("Linkedin");
                }

                Log.d("page", "onPageSelected: " + position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawer.openDrawer(GravityCompat.START);
                return true;
        }


        return super.onOptionsItemSelected(item);
    }

    private void checkDayNightMode() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme);
            Log.d("theme","change to dark");
        } else {
            setTheme(R.style.AppTheme);
            Log.d("theme","change to light");

        }
    }

    private void titleColorSetting() {
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            toolbar.setTitleTextColor(Color.WHITE);
            Log.d("theme2","change to dark");
        } else {
            toolbar.setTitleTextColor(Color.BLACK);
            Log.d("theme2","change to light");

        }
    }

    private void facebookQuickLink(Fragment fragment,boolean isNotification) {
        if (isNotification) {
            if (fragment instanceof SocialTabFragment) {
                ((SocialTabFragment) fragment).fbNotificationQuickLink();
            }
        }

        //reset flags
        isFbNotificationQuickLink= false;
    }

    private void twitterQuickLink(Fragment fragment, boolean isMoment, boolean isNotification, boolean isDM) {
        if (isMoment) {
            if (fragment instanceof SocialTabFragment) {
                ((SocialTabFragment) fragment).momentQuickLink();
            }
        } else if (isNotification) {
            if (fragment instanceof SocialTabFragment) {
                ((SocialTabFragment) fragment).notificationQuickLink();
            }
        } else if (isDM) {
            if (fragment instanceof SocialTabFragment) {
                ((SocialTabFragment) fragment).dmQuickLink();
            }
        }

        //reset flags
        isMomentQuickLink = false;
        isNotificationQuickLink = false;
        isDMQuickLink = false;
    }


}
