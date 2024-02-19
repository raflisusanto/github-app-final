package com.example.submissiongithubapp.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.submissiongithubapp.R
import com.example.submissiongithubapp.SettingPreferences
import com.example.submissiongithubapp.data.response.ItemsItem
import com.example.submissiongithubapp.dataStore
import com.example.submissiongithubapp.databinding.ActivityMainBinding
import com.example.submissiongithubapp.helper.PrefViewModelFactory
import com.example.submissiongithubapp.viewmodel.SettingsViewModel
import com.example.submissiongithubapp.viewmodel.UsersViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var usersViewModel : UsersViewModel
    private lateinit var settingsViewModel : SettingsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        initializeSettingsViewModel()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializeToolbar()

        // Initialize RecyclerView layout
        val layoutManager = LinearLayoutManager(this@MainActivity)
        binding.rvUsers.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this@MainActivity, layoutManager.orientation)
        binding.rvUsers.addItemDecoration(itemDecoration)

        // Initialize MainViewModel observer
        initializeUsersViewModel()
        initializeSearchView()
    }

    private fun setUsersData(usersList: List<ItemsItem>) {
        val adapter = UserAdapter()
        adapter.submitList(usersList)
        binding.rvUsers.adapter = adapter

        if (usersList.isEmpty()) binding.textEmpty.visibility = View.VISIBLE
        else binding.textEmpty.visibility = View.GONE
    }

    private fun showLoading(isLoading: Boolean) {
        binding.textEmpty.visibility = View.GONE

        if (isLoading) binding.progressBar.visibility = View.VISIBLE
        else binding.progressBar.visibility = View.GONE
    }

    private fun initializeSettingsViewModel() {
        val pref = SettingPreferences.getInstance(application.dataStore)
        val factory = PrefViewModelFactory.getInstance(pref)
        settingsViewModel = ViewModelProvider(this@MainActivity, factory)[SettingsViewModel::class.java]

        settingsViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    private fun initializeUsersViewModel() {
        usersViewModel = ViewModelProvider(this@MainActivity, ViewModelProvider.NewInstanceFactory())[UsersViewModel::class.java]

        usersViewModel.listUsers.observe(this@MainActivity) { listUsers ->
            setUsersData(listUsers)
        }

        usersViewModel.isLoading.observe(this@MainActivity) {
            showLoading(it)
        }

        // Error message handling
        usersViewModel.errorToast.observe(this@MainActivity) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun initializeSearchView() {
        with(binding) {
            searchView.setupWithSearchBar(searchBar)
            searchView
                .editText
                .setOnEditorActionListener { _, _, _ ->
                    searchBar.setText(searchView.text)
                    searchView.hide()

                    // Update search text in ViewModel to hit API again
                    usersViewModel.setSearchText(searchView.text.toString())

                    false
                }
        }
    }

    private fun initializeToolbar() {
        with(binding) {
            toolbar.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_theme -> {
                        startActivity(Intent(this@MainActivity, SettingsActivity::class.java))
                        true
                    }
                    R.id.action_favorite -> {
                        startActivity(Intent(this@MainActivity, FavoriteUserActivity::class.java))
                        true
                    }
                    else -> false
                }
            }
        }
    }
}