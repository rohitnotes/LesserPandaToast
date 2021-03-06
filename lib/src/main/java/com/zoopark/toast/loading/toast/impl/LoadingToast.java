package com.zoopark.toast.loading.toast.impl;

import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nasduck.lib.R;
import com.zoopark.toast.loading.config.LoadingToastConfig;
import com.zoopark.toast.loading.toast.BaseToast;
import com.zoopark.toast.utils.LesserDensityUtils;

public class LoadingToast extends BaseToast {

    private LinearLayout mLayoutContainer;
    private ImageView mIvImage;
    private TextView mTvTitle;

    private LoadingToastConfig mConfig;

    public LoadingToast() {}

    public static LoadingToast newInstance(LoadingToastConfig config){
        LoadingToast loadingToast = new LoadingToast();
        Bundle args = new Bundle();
        args.putParcelable("config", config);
        loadingToast.setArguments(args);
        return loadingToast;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mConfig = getArguments().getParcelable("config");
        }
    }

    @Override
    public int initView(@Nullable Bundle savedInstanceState) {
        return R.layout.toast_text_image;
    }

    @Override
    public void initData(@Nullable Bundle savedInstanceState) {

        mLayoutContainer = mView.findViewById(R.id.container);
        mIvImage = mView.findViewById(R.id.iv_image);
        mTvTitle = mView.findViewById(R.id.tv_title);

        updateUI(mConfig);
    }

    public void updateUI(LoadingToastConfig config) {

        // Title
        if (TextUtils.isEmpty(config.getText())) {
            mTvTitle.setText("");
            mTvTitle.setVisibility(View.GONE);
        } else {
            mTvTitle.setText(config.getText());
            mTvTitle.setTextSize(config.getTextSize());
            mTvTitle.setTextColor(getResources().getColor(config.getTextColor()));
            mTvTitle.setVisibility(View.VISIBLE);
        }

        // Image
        mIvImage.clearAnimation();
        if (config.getImage() != null) {
            mIvImage.setVisibility(View.VISIBLE);
            mIvImage.setImageResource(config.getImage());
        } else {
            mIvImage.setVisibility(View.GONE);
            mIvImage.setImageDrawable(null);
        }

        if (config.getImage() != null && !TextUtils.isEmpty(config.getText())) {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mTvTitle.getLayoutParams();
            lp.topMargin = LesserDensityUtils.dp2px(getContext(), 16);
            mTvTitle.setLayoutParams(lp);
        } else {
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) mTvTitle.getLayoutParams();
            lp.topMargin = 0;
            mTvTitle.setLayoutParams(lp);
        }

        if (config.getAnim() != null) { // Set Animation
            Animation animation = AnimationUtils.loadAnimation(getContext(), config.getAnim());
            mIvImage.setAnimation(animation);
        }

        // Padding
        mLayoutContainer.setPadding(LesserDensityUtils.dp2px(mContext, config.getPaddingLeft()),
                LesserDensityUtils.dp2px(mContext, config.getPaddingTop()),
                LesserDensityUtils.dp2px(mContext, config.getPaddingRight()),
                LesserDensityUtils.dp2px(mContext, config.getPaddingBottom()));

        // Corner Radius && Background Color
        GradientDrawable drawable = new GradientDrawable();
        drawable.setCornerRadius(LesserDensityUtils.dp2px(mContext, config.getCornerRadius()));
        drawable.setColor(mContext.getResources().getColor(config.getBgColor()));
        mLayoutContainer.setBackground(drawable);
    }
}
