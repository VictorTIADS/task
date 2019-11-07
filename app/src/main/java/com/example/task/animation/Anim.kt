package com.example.task.animation

import android.animation.Animator
import android.view.View
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.recyclerview.widget.SimpleItemAnimator

fun View.startAniminHidingComponent(ended: () -> Unit) {
    this.animate()
        .setInterpolator(object : FastOutSlowInInterpolator() {})
        .setDuration(300)
        .alpha(0f)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                ended()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })

}

fun View.startAniminMovingComponent(ended: () -> Unit) {
    this.animate()
        .setInterpolator(object : FastOutSlowInInterpolator() {})
        .setDuration(2000)
        .translationY(200f)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                ended()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })
}

fun View.startAniminShowingComponentLabels(ended: () -> Unit) {
    this.animate()
        .setInterpolator(object : FastOutSlowInInterpolator() {})
        .setDuration(2000)
        .alpha(1f)
        .translationY(200f)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                ended()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })

}
fun View.startAniminShowingComponentLoader(ended: () -> Unit) {
    this.animate()
        .setInterpolator(object : FastOutSlowInInterpolator() {})
        .setDuration(2000)
        .alpha(1f)
        .translationY(200f)
        .setListener(object : Animator.AnimatorListener {
            override fun onAnimationRepeat(p0: Animator?) {

            }

            override fun onAnimationEnd(p0: Animator?) {
                ended()
            }

            override fun onAnimationCancel(p0: Animator?) {

            }

            override fun onAnimationStart(p0: Animator?) {

            }
        })

}

fun View.visibleGone() {
    this.visibility = View.GONE
}


