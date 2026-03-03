package com.smartnotes.notepadplusplus.ui.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.smartnotes.notepadplusplus.R
import com.smartnotes.notepadplusplus.databinding.ActivitySplashBinding
import com.smartnotes.notepadplusplus.ui.noteslist.NotesListActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySplashBinding
    private var hasNavigated = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            val fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            binding.tvAppName.startAnimation(fadeIn)
            binding.tvTagline.startAnimation(fadeIn)
        }

        // lifecycleScope is cancelled automatically when Activity is destroyed
        lifecycleScope.launch {
            delay(1500L)
            navigateToMain()
        }
    }

    override fun onResume() {
        super.onResume()
        // If already navigated (e.g. back-stack return), go immediately
        if (hasNavigated) navigateToMain()
    }

    private fun navigateToMain() {
        if (!hasNavigated && !isFinishing && !isDestroyed) {
            hasNavigated = true
            startActivity(Intent(this, NotesListActivity::class.java))
            finish()
        }
    }
}
