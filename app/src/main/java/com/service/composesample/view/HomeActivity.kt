package com.service.composesample.view

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.service.composesample.extensions.coreComponent
import com.service.composesample.model.UserInfo
import com.service.composesample.model.database.dao.UserInfoDao
import com.service.composesample.utils.firebase.FirestoreHelper
import com.service.composesample.view.di.DaggerCommonUIComponent
import com.service.composesample.view.screens.LoginScreen
import com.service.composesample.view.screens.UserInfoDetailScreen
import com.service.composesample.view.screens.UserInfoListingScreen
import com.service.composesample.view.screens.UserRegistrationScreen
import retrofit2.Retrofit
import javax.inject.Inject

class HomeActivity : ComponentActivity() {
    @Inject
    lateinit var provideUserInfoDao: UserInfoDao
    @Inject
    lateinit var retrofit: Retrofit

    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerCommonUIComponent.builder().coreComponent(coreComponent()).build().inject(this)
        super.onCreate(savedInstanceState)
        //Enable FLAG_SECURE to prevent screenshots and screen recording
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContent {
            Surface() {
                ComposeApp()
            }
        }
    }



    @Composable
    fun ComposeApp() {
        val navController = rememberNavController()
        NavHost(navController = navController, startDestination = "listing") {
            composable("landing") { LoginScreen(navController=navController, retrofit = retrofit,userInfoDao=provideUserInfoDao) }
            composable("register") { UserRegistrationScreen(userInfo = null, navController=navController, retrofit = retrofit,userInfoDao=provideUserInfoDao) }
            composable("user_edited/{userInfo}", arguments = listOf(navArgument("userInfo") {  type = NavType.StringType })){backStackEntry ->
                val data = backStackEntry.arguments?.getString("userInfo")
                val userInfo = Gson().fromJson(data,UserInfo::class.java)
                UserRegistrationScreen(userInfo= userInfo,navController=navController, retrofit = retrofit,userInfoDao=provideUserInfoDao)
            }
            composable("listing") {
                UserInfoListingScreen(navController=navController, retrofit = retrofit,userInfoDao=provideUserInfoDao)
            }
            composable("detailed/{userInfo}", arguments = listOf(navArgument("userInfo") {  type = NavType.StringType })){backStackEntry ->
                val data = backStackEntry.arguments?.getString("userInfo")
                val userInfo = Gson().fromJson(data,UserInfo::class.java)
                UserInfoDetailScreen(userInfo= userInfo,navController=navController,retrofit = retrofit)
            }
        }
    }
}

