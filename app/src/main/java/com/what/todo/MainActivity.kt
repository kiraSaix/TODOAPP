package com.what.todo

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.what.todo.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val animatorSets = mutableListOf<AnimatorSet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding.root)

        // Set up background
        val backgroundView = layoutInflater.inflate(R.layout.cute_background, null)
        binding.backgroundContainer.addView(backgroundView)

        // Start animations for all background elements with delay
        startBackgroundAnimations(backgroundView)

        // Set up navigation
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        binding.bottomNavigation.setupWithNavController(navController)
    }

    private fun startBackgroundAnimations(backgroundView: View) {
        val views = listOf(
            backgroundView.findViewById<View>(R.id.shape1),
            backgroundView.findViewById<View>(R.id.star1),
            backgroundView.findViewById<View>(R.id.heart1),
            backgroundView.findViewById<View>(R.id.shape2),
            backgroundView.findViewById<View>(R.id.star2)
        )

        views.forEachIndexed { index, view ->
            view?.let {
                val duration = 3000L + (index * 500L)
                
                // Scale animation
                val scaleX = ObjectAnimator.ofFloat(it, View.SCALE_X, 1f, 1.2f).apply {
                    this.duration = duration
                    repeatMode = ObjectAnimator.REVERSE
                    repeatCount = ObjectAnimator.INFINITE
                    startDelay = (index * 150L) // Add staggered start delay
                }
                
                val scaleY = ObjectAnimator.ofFloat(it, View.SCALE_Y, 1f, 1.2f).apply {
                    this.duration = duration
                    repeatMode = ObjectAnimator.REVERSE
                    repeatCount = ObjectAnimator.INFINITE
                    startDelay = (index * 150L)
                }
                
                // Rotation animation
                val rotation = ObjectAnimator.ofFloat(it, View.ROTATION, 0f, 360f).apply {
                    this.duration = duration * 2
                    repeatMode = ObjectAnimator.RESTART
                    repeatCount = ObjectAnimator.INFINITE
                    startDelay = (index * 150L)
                }
                
                // Alpha animation
                val alpha = ObjectAnimator.ofFloat(it, View.ALPHA, 0.4f, 0.6f).apply {
                    this.duration = duration
                    repeatMode = ObjectAnimator.REVERSE
                    repeatCount = ObjectAnimator.INFINITE
                    startDelay = (index * 150L)
                }

                val animatorSet = AnimatorSet().apply {
                    playTogether(scaleX, scaleY, rotation, alpha)
                    interpolator = AccelerateDecelerateInterpolator()
                }
                
                animatorSets.add(animatorSet)
                animatorSet.start()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        // Clean up animations
        animatorSets.forEach { it.cancel() }
        animatorSets.clear()
    }
}