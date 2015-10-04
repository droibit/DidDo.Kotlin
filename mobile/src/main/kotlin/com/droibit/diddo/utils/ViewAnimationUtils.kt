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
            val cx = (view.left + view.right) / 2
            val cy = (view.top + view.bottom) / 2

            // get the final radius for the clipping circle
            val finalRadius = Math.max(view.width, view.height).toFloat()

            view.visibility = View.VISIBLE
            val animation = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0f, finalRadius)
                .setDuration(durationMillis)
            animation.interpolator = AccelerateDecelerateInterpolator()
            animation.start()
        }

        /**
         * 0.0 ~ 1.0 の倍率でスケールアニメーションする。
         */
        public fun animationScaleUp(view: View, durationMillis: Long) {
            view.visibility = View.VISIBLE

            val animation = ScaleAnimation(0f, 1f, 0f, 1f, (view.width / 2f), (view.height / 2f))
            animation.duration = durationMillis
            animation.interpolator = AccelerateDecelerateInterpolator()
            animation.fillAfter = true

            view.startAnimation(animation)
        }
    }
}