package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.assignment1.ui.dashboard.DashboardViewModel
import com.example.assignment1.ui.dashboard.EntityAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel: DashboardViewModel by viewModels()

    private lateinit var adapter: EntityAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recycler = findViewById<RecyclerView>(R.id.entitiesRecyclerView)
        val progress = findViewById<ProgressBar>(R.id.dashboardProgress)
        val totalText = findViewById<TextView>(R.id.entityTotalText)

        // On click, pass the whole Entity (Serializable) to DetailsActivity.
        adapter = EntityAdapter { entity ->
            val intent = Intent(this, DetailsActivity::class.java)
            intent.putExtra(DetailsActivity.EXTRA_ENTITY, entity)
            startActivity(intent)
        }
        recycler.layoutManager = LinearLayoutManager(this)
        recycler.adapter = adapter

        // Trigger the network call using the keypass handed over at login.
        val keypass = intent.getStringExtra(EXTRA_KEYPASS).orEmpty()
        viewModel.loadDashboard(keypass)

        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                adapter.submitList(state.entities)
                totalText.text = getString(R.string.entity_total_fmt, state.entityTotal)
                state.error?.let {
                    Toast.makeText(this@MainActivity, it, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        const val EXTRA_KEYPASS = "extra_keypass"
    }
}
