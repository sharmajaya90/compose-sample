package com.service.composesample.view

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.service.composesample.extensions.coreComponent
import com.service.composesample.model.database.dao.UserInfoDao
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
        NavHost(navController = navController, startDestination = "landing") {
            composable("landing") { LoginScreen(navController=navController, retrofit = retrofit,userInfoDao=provideUserInfoDao) }
            composable("register_user") { UserRegistrationScreen(navController=navController, retrofit = retrofit,userInfoDao=provideUserInfoDao) }
            composable("register_listing") { UserInfoListingScreen(navController=navController) }
            composable("user_detailed", arguments = listOf(navArgument("key") {  type = NavType.StringType })){backStackEntry ->
                val userKey = backStackEntry.arguments?.getString("key")
                UserInfoDetailScreen(navController=navController,userKey = userKey,retrofit = retrofit)
            }
        }
    }
}

