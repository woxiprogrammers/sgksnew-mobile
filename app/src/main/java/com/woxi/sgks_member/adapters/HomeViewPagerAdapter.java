package com.woxi.sgks_member.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ImageSpan;

import com.woxi.sgks_member.R;
import com.woxi.sgks_member.home.ClassifiedHomeFragment;
import com.woxi.sgks_member.home.ClassifiedHomeNewFragment;
import com.woxi.sgks_member.home.CommitteeHomeFragment;
import com.woxi.sgks_member.home.EventHomeFragment;
import com.woxi.sgks_member.home.MemberHomeFragment;
import com.woxi.sgks_member.home.MemberHomeNewFragment;
import com.woxi.sgks_member.home.MessageHomeFragment;
import com.woxi.sgks_member.home.MessageHomeNewFragment;

/**
 * <b><b>public class HomeViewPagerAdapter extends FragmentStatePagerAdapter</b></b>
 * <p>This class is used as a Adapter for App Home Layout (ViewPager)</p>
 * Created by - Rohit.
 */
public class HomeViewPagerAdapter extends FragmentStatePagerAdapter {
    private Context mContext;
    private int[] imageResId = {
            R.drawable.ic_events,
            R.drawable.ic_committee,
            R.drawable.ic_members,
            R.drawable.ic_messages,
            R.drawable.ic_classified,
    };

    public HomeViewPagerAdapter(FragmentManager fragmentManager, Context mContext) {
        super(fragmentManager);
        this.mContext = mContext;
    }

    @Override
    public int getCount() {
        return imageResId.length;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return EventHomeFragment.newInstance();
            case 1:
                return CommitteeHomeFragment.newInstance();
            case 2:
                return MemberHomeNewFragment.newInstance();
            case 3:
                return MessageHomeNewFragment.newInstance();
            case 4:
                return ClassifiedHomeNewFragment.newInstance();
            default:
                return null;
        }
    }

    @Override
    public CharSequence getPageTitle(int position) {
        Drawable image = ContextCompat.getDrawable(mContext, imageResId[position]);
        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
        SpannableString sb = new SpannableString(" ");
        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return sb;
    }
}
