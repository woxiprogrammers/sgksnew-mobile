package com.woxi.sgkks_member.utils;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.woxi.sgkks_member.R;
import com.woxi.sgkks_member.home.EventAndClassifiedDetailActivity;
import com.woxi.sgkks_member.interfaces.AppConstants;
import com.woxi.sgkks_member.models.AccountDetailsItem;
import com.woxi.sgkks_member.models.AccountImages;

import java.util.ArrayList;
import java.util.List;

import me.relex.circleindicator.CircleIndicator;

/**
 * <b>public class ImageZoomDialogFragment extends DialogFragment</b>
 * <p>This class is used as Dialog Fragment to show Account Image with Zoom Support</p>
 * Created by Rohit.
 */
public class ImageZoomDialogFragment extends DialogFragment {
    public static final int WRITE_STORAGE_PERMISSION_CODE = 111;
    private AccountDetailsItem accountDetailsItem;
    private boolean isFromEventList;
    private String strEventImageUrl;
    public static String strFinalImageUrl = "";
    private boolean isForClassifiedGallery;
    private boolean isFromHomeBuzz;
    private Context mContext;
    private ArrayList<AccountImages> accountImages = new ArrayList<>();
    private List<View> mViewList;
    private PagerAdapter pagerAdapter;
    private CircleIndicator indicator;
    ViewPager vpAccount;

    public static ImageZoomDialogFragment newInstance(AccountDetailsItem accountDetailsItem1) {
        Bundle args = new Bundle();
        args.putSerializable("accountDetailsItem", accountDetailsItem1);
        ImageZoomDialogFragment fragment = new ImageZoomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageZoomDialogFragment newInstance(String strImageUrl) {
        Bundle args = new Bundle();
        args.putString("eventImageUrl", strImageUrl);
        ImageZoomDialogFragment fragment = new ImageZoomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageZoomDialogFragment newInstance(String strImageUrl, int buzzId) {
        Bundle args = new Bundle();
        args.putString("buzzImageUrl", strImageUrl);
        args.putInt("buzzId",buzzId);
        ImageZoomDialogFragment fragment = new ImageZoomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public static ImageZoomDialogFragment newInstance(boolean isForClassifiedGallery, String strImageUrl) {
        Bundle args = new Bundle();
        args.putString("classifiedImageUrl", strImageUrl);
        args.putBoolean("isForClassifiedGallery", isForClassifiedGallery);
        ImageZoomDialogFragment fragment = new ImageZoomDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Dialog_FullScreen);
        initializePagerAdapter();
    }

    @Override
    public void onResume() {
        // Get existing layout params for the window
        if (getDialog().getWindow() != null) {
            ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
            // Assign window properties to fill the parent
            params.width = WindowManager.LayoutParams.MATCH_PARENT;
            params.height = WindowManager.LayoutParams.MATCH_PARENT;
            getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        }
        // Call super onResume after sizing
        super.onResume();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mParentView = inflater.inflate(R.layout.fragment_account_image_zoom, container, false);
        mContext = getActivity();
        Bundle args = getArguments();
        if (args != null) {
            if (args.containsKey("accountDetailsItem")) {
                accountDetailsItem = (AccountDetailsItem) args.getSerializable("accountDetailsItem");
                isFromEventList = false;
                isForClassifiedGallery = false;
            } else if (args.containsKey("eventImageUrl")) {
                strEventImageUrl = args.getString("eventImageUrl");
                isFromEventList = true;
                isForClassifiedGallery = false;
            } else if (args.containsKey("classifiedImageUrl")) {
                strEventImageUrl = args.getString("classifiedImageUrl");
                isForClassifiedGallery = args.getBoolean("isForClassifiedGallery");
                isForClassifiedGallery = true; //Make it true here
                isFromEventList = false;
            } else if (args.containsKey("buzzImageUrl")) {
                strEventImageUrl = args.getString("buzzImageUrl");
                isForClassifiedGallery = args.getBoolean("isForClassifiedGallery");
                isForClassifiedGallery = false; //Make it true here
                isFromEventList = false;
                isFromHomeBuzz = true;
                if(args.containsKey("buzzId")){
                    int buzzId = args.getInt("buzzId");
                    Log.i("@@@", "onCreateView IMAGE FRAGMENT BUZZ ID: "+buzzId);
                    /*-------------------TEMP CODE-------------------*/
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt(String.valueOf(AppConstants.PREFS_BUZZ_ID),buzzId);
                    editor.apply();
                    /*-------------------TEMP CODE-------------------*/
                }
            }

        }

        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        //Toast.makeText(getActivity().getBaseContext(), "Double Touch or Pinch In/Out To Zoom", Toast.LENGTH_LONG).show();

        ImageView mIvAccountImage = mParentView.findViewById(R.id.ivAccountImage);
        vpAccount = mParentView.findViewById(R.id.vpAccountImage);
        indicator = mParentView.findViewById(R.id.indicator);
        ImageView mIvDialogClose = mParentView.findViewById(R.id.ivDialogClose);
        TextView mTvAccountName = mParentView.findViewById(R.id.tvImageName);
        TextView mTcAccountDescription = mParentView.findViewById(R.id.tvDescription);
        FloatingActionButton floatingImageDownloadButton = mParentView.findViewById(R.id.floatingImageDownloadButton);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mIvDialogClose.setElevation(4);
        }
        mIvDialogClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        if (isFromEventList) {
            Toast.makeText(getActivity().getBaseContext(), "Double Touch or Pinch In/Out To Zoom", Toast.LENGTH_LONG).show();
            floatingImageDownloadButton.setVisibility(View.VISIBLE);
            mTvAccountName.setVisibility(View.GONE);
            mIvAccountImage.setVisibility(View.VISIBLE);
            strFinalImageUrl = strEventImageUrl;
            //Loading image from url.
            Glide.with(mContext)
                    .load(strFinalImageUrl)
                    .crossFade()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_broken_image)
                    .into(mIvAccountImage);
        } else if (isFromHomeBuzz) {
            Toast.makeText(getActivity().getBaseContext(), "Double Touch or Pinch In/Out To Zoom", Toast.LENGTH_LONG).show();
            floatingImageDownloadButton.setVisibility(View.GONE);
            mTvAccountName.setVisibility(View.GONE);
            mIvAccountImage.setVisibility(View.VISIBLE);
            strFinalImageUrl = strEventImageUrl;
            Log.i("@@@", "onCreateView: IMAGE URL: "+strEventImageUrl);
            //Loading image from url.
            Glide.with(mContext)
                    .load(strFinalImageUrl)
                    .crossFade()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_broken_image)
                    .into(mIvAccountImage);
        } else if (isForClassifiedGallery) {
            Toast.makeText(getActivity().getBaseContext(), "Double Touch or Pinch In/Out To Zoom", Toast.LENGTH_LONG).show();
            mIvAccountImage.setVisibility(View.VISIBLE);
            floatingImageDownloadButton.setVisibility(View.GONE);
            mTvAccountName.setVisibility(View.GONE);
            strFinalImageUrl = strEventImageUrl;
            Glide.with(mContext)
                    .load(strFinalImageUrl)
                    .crossFade()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .placeholder(R.drawable.ic_place_holder)
                    .error(R.drawable.ic_broken_image)
                    .into(mIvAccountImage);
        } else {
            mViewList = new ArrayList<>();
            floatingImageDownloadButton.setVisibility(View.GONE);
            mTvAccountName.setVisibility(View.VISIBLE);
            mIvAccountImage.setVisibility(View.VISIBLE);
            mTvAccountName.setText(accountDetailsItem.getStrAccountName());
            mTcAccountDescription.setText(accountDetailsItem.getStrAccountDescription());
            strFinalImageUrl = accountDetailsItem.getImagesList().get(0).getImagePath();
            int accountDetailItemSize = accountDetailsItem.getImagesList().size();
            for (int i = 0; i < accountDetailItemSize; i++) {
                ImageView view = new ImageView(mContext);
                view.setScaleType(ImageView.ScaleType.FIT_CENTER);
                view.setAdjustViewBounds(true);
                Glide.with(mContext)
                        .load(accountDetailsItem.getImagesList().get(i).getImagePath())
                        .crossFade()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .placeholder(R.drawable.ic_place_holder)
                        .error(R.drawable.ic_broken_image)
                        .into(view);
                mViewList.add(view);
            }
            vpAccount.setAdapter(pagerAdapter);
            pagerAdapter.registerDataSetObserver(indicator.getDataSetObserver());
            indicator.setViewPager(vpAccount);
        }

        floatingImageDownloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, WRITE_STORAGE_PERMISSION_CODE);
                } else if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    if (getActivity() != null) {
                        ((EventAndClassifiedDetailActivity) getActivity()).downloadImageFromUrl(strFinalImageUrl);
                        Toast.makeText(mContext, "Download Started", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        return mParentView;
    }
    private void initializePagerAdapter() {
        pagerAdapter = new PagerAdapter() {
            @Override
            public boolean isViewFromObject(View arg0, Object arg1) {
                return arg0 == arg1;
            }

            @Override
            public int getCount() {
                return mViewList.size();
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(mViewList.get(position));
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return "title";
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(mViewList.get(position));
                return mViewList.get(position);
            }
        };
    }

}

