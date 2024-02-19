package com.example.submissiongithubapp.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiongithubapp.databinding.ActivityFavoriteUserBinding
import com.example.submissiongithubapp.helper.ViewModelFactory
import com.example.submissiongithubapp.viewmodel.FavoriteUserViewModel

class FavoriteUserActivity : AppCompatActivity() {
    private lateinit var favUserViewModel: FavoriteUserViewModel

    private var _activityFavUserBinding: ActivityFavoriteUserBinding? = null
    private val binding get() = _activityFavUserBinding

    private lateinit var adapter: FavoriteUserAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _activityFavUserBinding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        setSupportActionBar(binding?.toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        // Initialize viewmodel
        favUserViewModel = obtainViewModel(this@FavoriteUserActivity)
        favUserViewModel.getAllFavUser().observe(this@FavoriteUserActivity) { favUsers ->
            favUsers?.let {
                adapter.setFavUsers(favUsers)
            }
        }

        // Initialize adapter
        adapter = FavoriteUserAdapter()
        with(binding) {
            this?.rvFavusers?.layoutManager = LinearLayoutManager(this@FavoriteUserActivity)
            this?.rvFavusers?.setHasFixedSize(true)
            this?.rvFavusers?.adapter = adapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavUserBinding = null
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                onBackPressedDispatcher.onBackPressed()
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun obtainViewModel(activity: AppCompatActivity): FavoriteUserViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory)[FavoriteUserViewModel::class.java]
    }
}