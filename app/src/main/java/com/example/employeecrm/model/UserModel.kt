package com.example.employeecrm.model



data class LoginRequest(val email: String, val password: String)

data class LoginResponse(val success:Boolean, val message: String, val token: String, val user: User)
data class EmployeeDetails(val success:Boolean, val message: String, val data: Array<Employee>)
data class MyProfile(val success:Boolean, val message: String,  val user: User)

data class Success(val success: Boolean, val message: String)



data class User(
    val _id:String,
    val name:String,
    val email: String,
    val designation: String,
    val designationType:String,
//    val employeeId:String
)


// Singleton object to manage the login response globally
object LoginManager {
    var loginResponse: LoginResponse? = null
}


data class Employee(
    val _id: String,
    val employeeName: String,
    val gender: String,
    val employeeEmail: String,
    val password: String,
    val employeePhoneNumber: String,
    val dateOfBirth: String,
    val address: String,
    val postOffice: String,
    val policeStation: String,
    val city: String,
    val state: String,
    val pinNumber: String,
    val designation: String,
    val designationType: String,
    val department: String
)

//project data class


//new project data class
data class ProjectRequest(
    val projectName: String,
    val description: String,
    val projectStartDate: String,
    val projectEndDate: String,
    val managerId: String,
    val priority: String,
    val team:String,
    val websiteUrl: String,
    val isCompleted: Boolean,
    val isScrap: Boolean,
)

data class NewEmployee(
    val employeeName: String,
    val gender: String,
    val employeeEmail: String,
    val password: String,
    val employeePhoneNumber: String,
    val dateOfBirth: String,
    val address: String,
    val postOffice: String,
    val policeStation: String,
    val city: String,
    val state: String,
    val pinNumber: String,
    val designation: String,
    val designationType: String,
    val department: String
)

data class NewTeam(
    val teamName:String,
    val teamDescription:String,
    val adminProfile: String,
    val selectedManager: String,
    val selectedProject:String,
    val selectedMembers: String,
)







