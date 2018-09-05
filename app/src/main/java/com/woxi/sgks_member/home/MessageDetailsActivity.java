package com.woxi.sgks_member.home;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.woxi.sgks_member.R;
import com.woxi.sgks_member.models.MessageDetailsItem;

public class MessageDetailsActivity extends AppCompatActivity {
    private Context mContext;
    private MessageDetailsItem mMessageDetailsItem;
    private TextView mTvNewsTitle, mTvNewsDate, mTvNewsDescription;
    private String TAG = "MessageDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        getSupportActionBar().setTitle(getString(R.string.message_details_title));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("currentNewsDetail")) {
                mMessageDetailsItem = (MessageDetailsItem) bundle.getSerializable("currentNewsDetail");
            }
        }

        //Calling function to initialize required views.
        initializeViews();
    }

    /**
     * <b>private void initializeViews()</b>
     * <p>This function is used to initialize required views.</p>
     * Created by - Rohit
     */
    private void initializeViews() {
        mContext = MessageDetailsActivity.this;
        mTvNewsTitle =  findViewById(R.id.tv_news_details_title);
        mTvNewsDate = findViewById(R.id.tv_news_details_created_date);
        mTvNewsDescription = findViewById(R.id.tv_news_details_description);

        if (mMessageDetailsItem.getMessageTitle() != null) {
            mTvNewsTitle.setText(mMessageDetailsItem.getMessageTitle().toUpperCase());
        }
        if (mMessageDetailsItem.getMessageCreateDate() != null) {
            mTvNewsDate.setText("Created On: " + mMessageDetailsItem.getMessageCreateDate());
        }
        if (mMessageDetailsItem.getMessageDescription() != null) {
            mTvNewsDescription.setText(mMessageDetailsItem.getMessageDescription());
        }

        //Loading image from url.
        ImageView mIvNewsImage = ((ImageView) findViewById(R.id.iv_news_details_image));
        String strUrl = mMessageDetailsItem.getMessageImage();
        Glide.with(mContext)
                .load(strUrl)
                .crossFade()
                .crossFade()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .placeholder(R.drawable.ic_place_holder)
                .error(R.drawable.ic_broken_image)
                .into(mIvNewsImage);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
