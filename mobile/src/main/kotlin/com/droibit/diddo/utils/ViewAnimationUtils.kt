package com.droibit.diddo.utils

import android.view.View
import android.view.ViewAnimationUtils
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.ScaleAnimation


/**
 * ビューをアニメーションするためのユーティリティクラス
 */
final class ViewAnimationUtils private constructor() {

    companion object {

        /**
         * ビューの中心を基点にリップルエフェクトを表示する。
         */
        public fun animationCircularReveal(view: View, durationMillis: Long) {
            // get the center for the clipping circle
            val cx = (view.getLeft() + view.getRight()) / 2
            val cy = (view.getTop() + view.getBottom()) / 2

            // get the final radius for the clipping circle
            val finalRadius = Math.max(view.getWidth(), view.getHeight()).toFloat()

            view.setVisibility(View.VISIBLE)
            val animation = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
                .setDuration(durationMillis)
            animation.setInterpolator(AccelerateDecelerateInterpolator())
            animation.start()
        }

        /**
         * 0.0 ~ 1.0 の倍率でスケールアニメーションする。
         */
        public fun animationScaleUp(view: View, durationMillis: Long) {
            view.setVisibility(View.VISIBLE)

            val animation = ScaleAnimation(0f, 1f, 0f, 1f, (view.getWidth() / 2f), (view.getHeight() / 2f))
            animation.setDuration(durationMillis)
            animation.setInterpolator(AccelerateDecelerateInterpolator())
            animation.setFillAfter(true)

            view.startAnimation(animation)
        }
    }
}