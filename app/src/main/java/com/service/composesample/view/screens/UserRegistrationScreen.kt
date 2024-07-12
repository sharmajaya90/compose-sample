package com.service.composesample.view.screens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.service.composesample.model.UserInfo
import com.service.composesample.model.database.dao.UserInfoDao
import com.service.composesample.utils.firebase.FirestoreHelper
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import retrofit2.Retrofit


@Composable
fun UserRegistrationScreen(navController: NavHostController, retrofit: Retrofit, userInfoDao: UserInfoDao) {
    val userInfo by remember { mutableStateOf(UserInfo(name = "", email = "")) }
    val isRegisterOnline by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val state = rememberCollapsingToolbarScaffoldState()

    LaunchedEffect(Unit) {

    }
    val scrollState = rememberScrollState()
    CollapsingToolbarScaffold(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            Row {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.align(Alignment.CenterVertically)) {
                    Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "Back")
                }
                Text(
                    textAlign = TextAlign.Left,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    text = if(isRegisterOnline)"Online Register User" else "Offline Register User",
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black,
                    modifier = Modifier.padding(15.dp)
                )

            }
        }

    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp).verticalScroll(scrollState),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = userInfo.name,
                onValueChange = { userInfo.name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = userInfo.email,
                onValueChange = { userInfo.email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = userInfo.address,
                onValueChange = { userInfo.address = it },
                label = { Text("Address") },
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 200.dp) // Set a minimum height
                    .padding(bottom = 16.dp),
                keyboardOptions = KeyboardOptions.Default.copy(
                    keyboardType = KeyboardType.Text
                ),
                maxLines = Int.MAX_VALUE, // Allow unlimited lines
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    // Handle post action
                    var isValid = true
                    var msg = ""
                    if (userInfo.name.isEmpty()) {
                        isValid = false
                        msg = "Please Enter Valid user name."
                    }else if (userInfo.email.isEmpty()) {
                        isValid = false
                        msg = "Please Enter Valid email id."
                    }

                    if (isValid) {
                        FirestoreHelper.addUserInfo(userInfo, onSuccess = {
                            Toast.makeText(
                                context,
                                "QA Added successfully::${userInfo.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }, onFailure = {
                            Toast.makeText(
                                context,
                                "UserInfo failed to added::${userInfo.name}",
                                Toast.LENGTH_SHORT
                            ).show()
                        })
                    } else {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Register User")
            }
        }
    }
}