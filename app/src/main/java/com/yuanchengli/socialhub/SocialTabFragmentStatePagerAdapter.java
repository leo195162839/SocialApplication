package com.yuanchengli.socialhub;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;
import android.util.SparseArray;
import android.view.ViewGroup;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

public class SocialTabFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private final SparseArray<WeakReference<Fragment>> instantiatedFragments = new SparseArray<>();
    private Map<Integer,String> mFragmentTags;


    public SocialTabFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        mFragmentTags = new HashMap<>();
    }

    @Override
    public Fragment getItem(int index) {

        switch (index) {
            case 0:
                SocialTabFragment facebookFragment = new SocialTabFragment();
                facebookFragment.setRetainInstance(true);
                facebookFragment.url = "https://www.facebook.com/login/";
                return facebookFragment;
            case 1:
                SocialTabFragment twitterFragment = new SocialTabFragment();
                twitterFragment.setRetainInstance(true);
                twitterFragment.url = "https://mobile.twitter.com/";
                return twitterFragment;
            case 2:
                SocialTabFragment linkedinFragment = new SocialTabFragment();
                linkedinFragment.setRetainInstance(true);
                linkedinFragment.url = "https://www.linkedin.com/";
                return linkedinFragment;
            default:
                facebookFragment = new SocialTabFragment();
                facebookFragment.setRetainInstance(true);
                facebookFragment.url = "https://www.facebook.com/login/";
                return facebookFragment;
        }
    }

    @Override
    public int getCount() {
        // get item count - equal to number of tabs
        return 3;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return "Facebook";
        } else if (position == 1) {
            return "Linkedin";
        } else {
            return "Twitter";
        }
    }

    @Override
    public Object instantiateItem(final ViewGroup container, final int position) {
        final Fragment fragment = (Fragment) super.instantiateItem(container, position);
        instantiatedFragments.put(position, new WeakReference<>(fragment));
        return fragment;
    }

    @Override
    public void destroyItem(final ViewGroup container, final int position, final Object object) {
        instantiatedFragments.remove(position);
        super.destroyItem(container, position, object);
    }

    @Nullable
    public Fragment getFragment(final int position) {
        Log.d("fragment2 getfragment position", Integer.toString(position));
        final WeakReference<Fragment> wr = instantiatedFragments.get(position);
        if (wr != null) {
            return wr.get();
        } else {
            return null;
        }
    }


}
