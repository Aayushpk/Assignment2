package com.example.assignment1

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.assignment1.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    // Hilt-provided ViewModel.
    private val viewModel: LoginViewModel by viewModels()

    // Choose your campus here: "footscray", "sydney" or "ort".
    private val campus = "sydney"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val usernameInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.enterStudioButton)
        val progress = findViewById<ProgressBar>(R.id.loginProgress)

        loginButton.setOnClickListener {
            viewModel.login(
                campus = campus,
                username = usernameInput.text.toString().trim(),
                password = passwordInput.text.toString().trim()
            )
        }

        // Observe UI state. repeatOnLifecycle would be ideal; lifecycleScope
        // + collect is sufficient and simpler for a single-screen Activity.
        lifecycleScope.launch {
            viewModel.uiState.collect { state ->
                progress.visibility = if (state.isLoading) View.VISIBLE else View.GONE
                loginButton.isEnabled = !state.isLoading

                // Error handling: show then clear so it doesn't re-fire.
                state.error?.let {
                    Toast.makeText(this@LoginActivity, it, Toast.LENGTH_LONG).show()
                    viewModel.errorShown()
                }

                // Success: pass the keypass forward to the Dashboard.
                state.keypass?.let { keypass ->
                    val intent = Intent(this@LoginActivity, MainActivity::class.java)
                    intent.putExtra(MainActivity.EXTRA_KEYPASS, keypass)
                    startActivity(intent)
                    finish() // remove login from the back stack
                }
            }
        }
    }
}
