package com.arkavyapar.Constant

import android.app.Activity
import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat.getSystemService
import com.arkavyapar.App
import com.arkavyapar.R


class Animation {
    companion object obj {

        fun editText_Sh(view: View) {
            view.startAnimation(AnimationUtils.loadAnimation(App.instance, R.anim.shake));
            AndroidDeviceVibrate()
        }

        private fun AndroidDeviceVibrate() { // Android Device Vibration
            try {
                val vibrator = App.instance?.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrator.vibrate(
                        VibrationEffect.createOneShot(
                            200,
                            VibrationEffect.DEFAULT_AMPLITUDE
                        )
                    )
                } else {
                    vibrator.vibrate(200)
                }
            } catch (e: Exception) {
            }
        }

        fun setAnimation(ctx:Activity) {
            if (Build.VERSION.SDK_INT > 20) {
             var slide = Slide()
                slide.setSlideEdge(Gravity.RIGHT);
                slide.setDuration(500);
                slide.setInterpolator( AccelerateDecelerateInterpolator ());
              //  ctx.getWindow().setExitTransition(slide);
                ctx.getWindow().setEnterTransition(slide);
            }
        }




            /* public void setAnimation()
             {
                 if (Build.VERSION.SDK_INT > 20) {
                     Slide slide = new Slide();

                 }

             }*/
    }
}