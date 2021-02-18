package com.pkurkowski.spacextest.presentation

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.pkurkowski.spacextest.R
import com.pkurkowski.spacextest.databinding.ActivityMainBinding
import com.pkurkowski.spacextest.domain.CompanyData
import com.pkurkowski.spacextest.domain.LaunchData
import com.pkurkowski.spacextest.domain.Response
import com.pkurkowski.spacextest.presentation.adapter.LaunchesAdapter
import com.pkurkowski.spacextest.presentation.dialog.FilterLaunchesDialogFragment
import com.pkurkowski.spacextest.presentation.dialog.FilterUpdate
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModel()

    private val adapter = LaunchesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        viewModel.launchData.observe(this, { updateLaunchData(it.data) })
        viewModel.companyData.observe(this, { updateCompanyData(it) })

    }

    override fun onResume() {
        super.onResume()
        viewModel.start()

    }

    override fun onPause() {
        viewModel.stop()
        super.onPause()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.filter, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_filter) {
            FilterLaunchesDialogFragment().show(supportFragmentManager, FilterLaunchesDialogFragment.TAG)
        }
        return true
    }

    fun filterUpdate(update: FilterUpdate) {
            viewModel.filterUpdate(update)
    }

    private fun updateLaunchData(response: Response<List<LaunchData>>) {
        binding.launchesProgressBar.isVisible = response is Response.InProgress
        binding.launchesEmptyTitle.isVisible = response is Response.FailNoOfflineData

        when (response) {
            is Response.Success -> adapter.update(response.data)
            is Response.FailWithOfflineData -> {
                adapter.update(response.offlineData)
                showToast(R.string.title_failure_updating_launch_data)
            }
            is Response.FailNoOfflineData -> {
                showToast(R.string.title_failure_updating_launch_data)
            }
        }
    }

    private fun updateCompanyData(response: Response<CompanyData>) {
        binding.companyProgressBar.isVisible = response is Response.InProgress

        when (response) {
            is Response.Success -> binding.companyTextView.text = getCompanyString(response.data)
            is Response.FailWithOfflineData -> {
                binding.companyTextView.text = getCompanyString(response.offlineData)
                showToast(R.string.title_failure_updating_company)
            }
            is Response.FailNoOfflineData -> {
                showToast(R.string.title_failure_updating_company)
                binding.companyTextView.text = resources.getString(R.string.no_data)
            }
        }
    }

    private fun getCompanyString(company: CompanyData): String {
        return String.format(
            getString(R.string.company_description_string),
            company.name,
            company.founder,
            company.founded,
            company.employees,
            company.launchSites,
            company.valuation,
        )
    }

    private fun showToast(@StringRes id: Int) {
        Toast.makeText(this, resources.getString(id), Toast.LENGTH_SHORT).show()
    }

}