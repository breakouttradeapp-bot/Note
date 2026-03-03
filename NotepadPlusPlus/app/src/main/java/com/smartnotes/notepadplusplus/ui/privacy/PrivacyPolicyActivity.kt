package com.smartnotes.notepadplusplus.ui.privacy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.smartnotes.notepadplusplus.R
import com.smartnotes.notepadplusplus.databinding.ActivityPrivacyBinding

class PrivacyPolicyActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPrivacyBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = getString(R.string.privacy_policy)
        binding.toolbar.setNavigationOnClickListener { finish() }
        binding.tvContent.text = getString(R.string.privacy_policy_content)
    }
}
