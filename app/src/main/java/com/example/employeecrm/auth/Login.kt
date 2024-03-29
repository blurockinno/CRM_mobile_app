package com.example.employeecrm.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.activities.admin.dashboard.Dashboard
import com.example.employeecrm.activities.splashscreen.SplashScreen
import com.example.employeecrm.constant.Constant
import com.example.employeecrm.databinding.ActivityLoginBinding
import com.example.employeecrm.model.LoginManager
import com.example.employeecrm.model.LoginRequest
import com.example.employeecrm.model.LoginResponse
import com.example.employeecrm.model.User
import kotlinx.coroutines.launch

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Login : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val BASE_URL = "${Constant.server}api/v1/users/" // Remove '/users/login' from the base URL

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Handle sign-in button click
        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()
            handleOnSignIn(email, password)
        }
    }

    // Function to handle the sign-in logic
    private fun handleOnSignIn(email: String, password: String) {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val apiService = retrofit.create(Apis::class.java)

        // Inside a coroutine scope (e.g., using lifecycleScope.launch)
        lifecycleScope.launch {
            try {
                val response = apiService.auth(LoginRequest(email, password))
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        // Handle successful login response
                       val user = User(loginResponse.user._id, loginResponse.user.name, loginResponse.user.email, loginResponse.user.designation,loginResponse.user.designationType)

                        val receivedLoginResponse = LoginResponse(
                            loginResponse.success,
                            loginResponse.message,
                            loginResponse.token,
                            user = User(loginResponse.user._id, loginResponse.user.name, loginResponse.user.email, loginResponse.user.designation,loginResponse.user.designationType)
                        )

                        // Storing the login response in the LoginManager
                        LoginManager.loginResponse = receivedLoginResponse

                        if(loginResponse.success){
                            // Save the token to SharedPreferences or another secure storage
                            saveAuthToken(loginResponse.token, user)
                        }

                        //after successful login user redirected to their dashboard
                        startActivity(Intent(this@Login, SplashScreen::class.java))
                        finish()

                    } else {
                        // Handle scenario where response body is null
                        Log.d("LoginError", "Empty response body")
                    }
                } else {
                    // Handle unsuccessful login (e.g., invalid credentials, server errors)
                    val errorBody = response.errorBody()?.string()
                    Log.d("LoginError", "Error else: $errorBody")
                }
            } catch (e: IOException) {
                // Handle network issues or I/O problems
                Log.d("LoginError", "Network error: ${e.message}")
            } catch (e: Exception) {
                // Handle other exceptions
                Log.d("LoginError", "Error: ${e.message}")
            }
        }
    }
    //todo to correct the conflict adminId and employeeid
    private fun saveAuthToken(token: String, user: User, ) {
        val sharedPreferences = getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit();
        editor.putString("authToken", token)
        editor.putString("designationType", user.designationType)
        editor.putString("employeeId", user._id)

        editor.apply()

//        editor.putString("employeeId", user.employeeId



    }
}
