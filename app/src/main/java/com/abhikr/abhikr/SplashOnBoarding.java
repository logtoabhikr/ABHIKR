package com.abhikr.abhikr;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewTreeObserver;
import android.view.animation.AccelerateInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.scottyab.rootbeer.RootBeer;

public class SplashOnBoarding extends AppCompatActivity {
LinearLayout Splash_lay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash__reveal);
        Splash_lay=findViewById(R.id.Splash_lay);
        if (savedInstanceState == null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Splash_lay.setVisibility(View.INVISIBLE);

          /*  revealX = (Splash_lay.x + Splash_lay.width / 2).toInt() // taking from this way not working
            revealY = (Splash_lay.y + Splash_lay.height / 2).toInt()*/


            ViewTreeObserver viewTreeObserver = Splash_lay.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        revealActivity();
                        Splash_lay.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
            }
        } else {
            Splash_lay.setVisibility(View.VISIBLE);
        }
    }
    //x: Int, y: Int
    protected void revealActivity() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            float finalRadius =(float) (Math.max(Splash_lay.getWidth(), Splash_lay.getHeight()) * 1.1);

            // create the animator for this view (the start radius is zero)
            //val circularReveal = ViewAnimationUtils.createCircularReveal(Splash_lay, x, y, 0f, finalRadius)
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(Splash_lay, Splash_lay.getWidth() / 2, Splash_lay.getHeight() / 2, 0f, finalRadius);

            /*  val cx = Splash_lay.getWidth() / 2
              val cy = Splash_lay.getHeight() / 2

              val finalRadius = Math.max(Splash_lay.getWidth(), Splash_lay.getHeight())

              // create the animator for this view (the start radius is zero)
              val circularReveal = ViewAnimationUtils.createCircularReveal(Splash_lay, cx, cy, 0f, finalRadius.toFloat())*/
            circularReveal.setDuration(1100);
            circularReveal.setInterpolator(new AccelerateInterpolator());
            // make the view visible and start the animation
            Splash_lay.setVisibility(View.VISIBLE);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    //Splash_lay.setVisibility(View.INVISIBLE)
                    RootBeer rootBeer=new RootBeer(SplashOnBoarding.this);
                    if (rootBeer.isRooted()) {
                        Toast.makeText(SplashOnBoarding.this, "ABHIKR Works on Non-Rooted devices only!", Toast.LENGTH_LONG).show();
                        supportFinishAfterTransition();
                    }
                    else {
                        startActivity(new Intent(SplashOnBoarding.this, Home.class), ActivityOptions.makeSceneTransitionAnimation(SplashOnBoarding.this).toBundle());
                        finishAfterTransition();
                    }
                }
            });
            circularReveal.start();
        } else {
            RootBeer rootBeer=new RootBeer(SplashOnBoarding.this);
            if (rootBeer.isRooted()) {
                Toast.makeText(SplashOnBoarding.this, "ABHIKR Works on Non-Rooted devices only!", Toast.LENGTH_LONG).show();
                supportFinishAfterTransition();
            }
            else {
                startActivity(new Intent(SplashOnBoarding.this, Home.class), ActivityOptions.makeSceneTransitionAnimation(SplashOnBoarding.this).toBundle());
                finishAfterTransition();
            }
        }
    }
  /*  fun presentActivity(view: View) {
        val options = ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, "transition")
        val revealX = (view.x + view.width / 2).toInt()
        val revealY = (view.y + view.height / 2).toInt()

        val intent = Intent(this, SplashActivity2::class.java)
        intent.putExtra("EXTRA_CIRCULAR_REVEAL_X", revealX)
        intent.putExtra("EXTRA_CIRCULAR_REVEAL_Y", revealY)

        ActivityCompat.startActivity(this, intent, options.toBundle())
    }

    override fun onResume() {
        super.onResume()
        presentActivity(view = card_logo)
    }

    private fun reveal()
 {
     // previously visible view

// Check if the runtime version is at least Lollipop
     if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
         // get the center for the clipping circle
         val cx = wpt_cardview_splash.getWidth() / 2
         val cy = wpt_cardview_splash.getHeight() / 2

         // get the initial radius for the clipping circle
         val initialRadius = Math.hypot(cx.toDouble(), cy.toDouble()).toFloat()

         // create the animation (the final radius is zero)
         val anim = ViewAnimationUtils.createCircularReveal(wpt_cardview_splash, cx, cy, initialRadius, 0f)

         // make the view invisible when the animation is done
         anim.addListener(object : AnimatorListenerAdapter() {
             override fun onAnimationEnd(animation: Animator) {
                 super.onAnimationEnd(animation)
                 wpt_cardview_splash.setVisibility(View.INVISIBLE)
             }
         })

         // start the animation
         anim.start()
     } else {
         // set the view to visible without a circular reveal animation below Lollipop
         wpt_cardview_splash.setVisibility(View.VISIBLE)
     }
 }
    private fun ShowEnterAnimation() {
        val transition = TransitionInflater.from(this).inflateTransition(R.transition.fabtransition)
        window.sharedElementEnterTransition = transition

        transition.addListener(object : Transition.TransitionListener {
            override fun onTransitionStart(transition: Transition) {
                wpt_cardview_splash.setVisibility(View.GONE)
            }

            override fun onTransitionEnd(transition: Transition) {
                transition.removeListener(this)
                animateRevealShow()
                //reveal()
            }

            override fun onTransitionCancel(transition: Transition) {

            }

            override fun onTransitionPause(transition: Transition) {

            }

            override fun onTransitionResume(transition: Transition) {

            }


        })
    }

    fun animateRevealShow() {
        val mAnimator = ViewAnimationUtils.createCircularReveal(wpt_cardview_splash, wpt_cardview_splash.getWidth() / 2, 0, (card_logo!!.getWidth() / 2).toFloat(), wpt_cardview_splash!!.getHeight().toFloat())
        mAnimator.duration = 500
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
            }

            override fun onAnimationStart(animation: Animator) {
                wpt_cardview_splash.setVisibility(View.VISIBLE)
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }
    fun animateRevealClose() {
        val finalRadius = (Math.max(wpt_cardview_splash.getWidth(), wpt_cardview_splash.getHeight()) * 1.1).toFloat()
        val mAnimator = ViewAnimationUtils.createCircularReveal(wpt_cardview_splash, wpt_cardview_splash!!.getWidth() / 2, wpt_cardview_splash!!.getWidth() / 2, 0f, finalRadius)
        mAnimator.duration = 1000
        mAnimator.interpolator = AccelerateInterpolator()
        mAnimator.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                wpt_cardview_splash.setVisibility(View.INVISIBLE)
                super.onAnimationEnd(animation)
                card_logo.setImageResource(R.mipmap.ic_launcher)
                super@SplashScreen.onBackPressed()
                //super@SplashScreen.onStart()
            }

            override fun onAnimationStart(animation: Animator) {
                super.onAnimationStart(animation)
            }
        })
        mAnimator.start()
    }
    override fun onBackPressed() {
        animateRevealClose()
    }*/
}
