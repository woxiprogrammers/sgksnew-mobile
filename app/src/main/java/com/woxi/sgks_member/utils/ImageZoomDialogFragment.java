package com.woxi.sgks_member.utils;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.models.AccountDetailsItem;

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
    private Context mContext;

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
            }
        }

        if (getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        Toast.makeText(getActivity().getBaseContext(), "Double Touch or Pinch In/Out To Zoom", Toast.LENGTH_LONG).show();

        ImageView mIvAccountImage = (ImageView) mParentView.findViewById(R.id.ivAccountImage);
        ImageView mIvDialogClose = (ImageView) mParentView.findViewById(R.id.ivDialogClose);
        TextView mTvAccountName = (TextView) mParentView.findViewById(R.id.tvImageName);
        FloatingActionButton floatingImageDownloadButton = (FloatingActionButton) mParentView.findViewById(R.id.floatingImageDownloadButton);

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
            floatingImageDownloadButton.setVisibility(View.VISIBLE);
            mTvAccountName.setVisibility(View.GONE);
            strFinalImageUrl = strEventImageUrl;
        } else if (isForClassifiedGallery) {
            floatingImageDownloadButton.setVisibility(View.GONE);
            mTvAccountName.setVisibility(View.GONE);
            strFinalImageUrl = strEventImageUrl;
        } else {
            floatingImageDownloadButton.setVisibility(View.GONE);
            mTvAccountName.setVisibility(View.VISIBLE);
            mTvAccountName.setText(accountDetailsItem.getStrAccountName());
            strFinalImageUrl = accountDetailsItem.getStrAccountImageUrl();
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

        //Loading image from url.
        Glide.with(mContext)
                .load(strFinalImageUrl)
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_broken_image)
                .into(mIvAccountImage);
        return mParentView;
    }
}
