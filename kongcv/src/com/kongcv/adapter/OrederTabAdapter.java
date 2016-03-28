package com.kongcv.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;



public class OrederTabAdapter extends FragmentPagerAdapter {
	private List<Fragment> mFragments;
	private int currentItem;

	public OrederTabAdapter(FragmentManager fm, List<Fragment> mFragments,int currentItem) {
		super(fm);
		this.mFragments = mFragments;
		this.currentItem=currentItem;
	}
	public Fragment getItem(int position) {
		return mFragments.get(position);
	}

	public int getCount() {
		return mFragments.size();
	}
}
