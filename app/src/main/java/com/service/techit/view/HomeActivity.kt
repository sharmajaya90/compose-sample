package com.service.techit.view

import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.service.techit.extensions.colorStringToComposeColor
import com.service.techit.extensions.coreComponent
import com.service.techit.model.QAInfoTechnology
import com.service.techit.model.TechITStyles
import com.service.techit.model.database.dao.QAInfoDao
import com.service.techit.model.database.dao.UserInfoDao
import com.service.techit.utils.AppTheme
import com.service.techit.utils.firebase.FirestoreHelper
import com.service.techit.view.di.DaggerCommonUIComponent
import com.service.techit.view.userscreens.QAInfoLandingScreen
import com.service.techit.view.userscreens.QuestionDetailScreen
import com.service.techit.view.adminscreens.AddTechnologyScreen
import com.service.techit.view.adminscreens.AdminQAInfoListingScreen
import com.service.techit.view.helperui.LoginScreen
import com.service.techit.view.adminscreens.QuestionAnswerPostScreen
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import javax.inject.Inject

class HomeActivity : ComponentActivity() {
    @Inject
    lateinit var provideUserInfoDao: UserInfoDao
    @Inject
    lateinit var provideQAInfoDao: QAInfoDao
    @Inject
    lateinit var retrofit: Retrofit

    val techitStyle:TechITStyles? by lazy {
        runBlocking {
            FirestoreHelper.provideTechITStyles()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        DaggerCommonUIComponent.builder().coreComponent(coreComponent()).build().inject(this)
        super.onCreate(savedInstanceState)
        //Enable FLAG_SECURE to prevent screenshots and screen recording
        window.setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE)

        setContent {
            AppTheme {
                Surface(color = techitStyle?.backgroundColor?.colorStringToComposeColor()?: Color.White) {
                   TechITApp()
                }
            }
        }
    }



    @Composable
    fun TechITApp() {
        val navController = rememberNavController()
        var technologyList by remember { mutableStateOf(listOf<QAInfoTechnology>()) }
        runBlocking {
            technologyList =  FirestoreHelper.getQAInfoTechnologies()
        }
        NavHost(navController = navController, startDestination = "landingAdmin") {
            composable("landing") {
                QAInfoLandingScreen(technologyList= technologyList, navController=navController, retrofit = retrofit,provideQAInfoDao=provideQAInfoDao)
            }
            composable("landingAdmin") {
                AdminQAInfoListingScreen(technologyList= technologyList, navController=navController, retrofit = retrofit,provideQAInfoDao=provideQAInfoDao)
            }
            composable("login") { LoginScreen(navController)}
            composable("post_qa") { QuestionAnswerPostScreen(navController=navController, retrofit = retrofit,provideQAInfoDao=provideQAInfoDao) }
            composable("post_technology") { AddTechnologyScreen(navController=navController) }
            composable("qaEdited/{qaInfoKey}",
                arguments = listOf(navArgument("qaInfoKey") {  type = NavType.StringType })
            ) { backStackEntry ->
                val qaInfo = backStackEntry.arguments?.getString("qaInfoKey")
                QuestionAnswerPostScreen(qaInfo?:"",navController=navController, retrofit = retrofit,provideQAInfoDao=provideQAInfoDao)
            }
            composable("qaDetailed/{technology},{position}",
                arguments = listOf(navArgument("technology") {  type = NavType.StringType },navArgument("position") {  type = NavType.IntType })
            ) { backStackEntry ->
                val position = backStackEntry.arguments?.getInt("position")?:0
                val technology = backStackEntry.arguments?.getString("technology")?:"android"
                QuestionDetailScreen(technology,position,navController= navController,retrofit = retrofit,provideQAInfoDao=provideQAInfoDao)
            }
        }
    }
}

