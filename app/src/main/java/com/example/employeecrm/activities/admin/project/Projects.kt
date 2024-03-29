package com.example.employeecrm.activities.admin.project


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast

import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.employeecrm.APIServices.Apis
import com.example.employeecrm.adapters.AllProjectAdapter
import com.example.employeecrm.base.BaseActivity
import com.example.employeecrm.constant.Constant
import com.example.employeecrm.databinding.ActivityProjectsBinding
import com.example.employeecrm.model.AllProject
import com.example.employeecrm.model.LoginManager
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class Projects : BaseActivity() {
    private lateinit var binding: ActivityProjectsBinding
    //for storing the token
    private lateinit var token: String
    // for all project
    private val allProject: MutableList<AllProject> = mutableListOf()

    // Base URL of your API
    private val BASE_URL = Constant.server
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProjectsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Checking if a login response is stored and accessing its properties
        val storedLoginResponse = LoginManager.loginResponse

        token = storedLoginResponse?.token ?: getAuthToken()
//        handle for get the project details
        getProjects()
    }

    private fun showProjectList(allProject: MutableList<AllProject>) {
        binding.rvProjectList.layoutManager = LinearLayoutManager(this@Projects,LinearLayoutManager.VERTICAL, false)
        binding.rvProjectList.setHasFixedSize(true)

        val adapter = AllProjectAdapter(this@Projects, allProject)
        binding.rvProjectList.adapter = adapter


        adapter.setOnClickListener(object :
        AllProjectAdapter.OnClickListener{
            override fun onCLick(position: Int, model: AllProject) {
                Toast.makeText(this@Projects, "clicked", Toast.LENGTH_LONG).show()
                startActivity(Intent(this@Projects, ProjectDetails::class.java).putExtra("projectName", model.projectName).putExtra("projectDescription", model.description))
            }
        })

    }

    private fun getProjects() {
       val retrofit = Retrofit.Builder()
           .baseUrl(BASE_URL)
           .addConverterFactory(GsonConverterFactory.create())
           .build()

        val apiServices = retrofit.create(Apis::class.java)

        lifecycleScope.launch {
            try {
                val response = apiServices.getProjectDetails("token=$token")
                if (response.isSuccessful) {
                    val projectResponse = response.body()
                    showToast("${projectResponse?.allProject}")
                    if (projectResponse != null) {
                        for(project in projectResponse.allProject){
//                            push all the project
                            allProject.add(project)
//                            for completed and in comleted project
                        }
                            showProjectList(allProject)


                    } else {
                        // Handle scenario where response body is null
                        Log.d("project error", "Empty response body")
                    }
                } else {
                    // Handle unsuccessful login (e.g., invalid credentials, server errors)
                    val errorBody = response.errorBody()?.string()
                    Log.d("project error", "Error: $errorBody")
                }
            } catch (e: IOException) {
                // Handle other exceptions
                Log.d("project error", "Error: ${e.message}")
            }
        }

    }
}