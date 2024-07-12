package com.service.techit.view.adminscreens

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
import com.service.techit.model.QAInfo
import com.service.techit.model.database.dao.QAInfoDao
import com.service.techit.utils.firebase.FirestoreHelper
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import retrofit2.Retrofit


@Composable
fun QuestionAnswerPostScreen(key: String?=null,navController: NavHostController, retrofit: Retrofit, provideQAInfoDao: QAInfoDao) {
    var question by remember { mutableStateOf("") }
    var selectedQuestionType by remember { mutableStateOf("") }
    var answer by remember { mutableStateOf("") }
    val context = LocalContext.current

    var imageUriList by remember { mutableStateOf(listOf<String>()) }
    val state = rememberCollapsingToolbarScaffoldState()

    LaunchedEffect(Unit) {
        key?.let {
          val qaInfo =  FirestoreHelper.getQAInfoById(key)
            question = qaInfo?.title?:""
            selectedQuestionType = qaInfo?.type?:""
            answer = qaInfo?.detailed?:""
        }
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
                    text = if(key.isNullOrEmpty()){"Add QAInfo"}else{"Update QAInfo"},
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
                value = question,
                onValueChange = { question = it },
                label = { Text("Question") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = selectedQuestionType,
                onValueChange = { selectedQuestionType = it },
                label = { Text("Question Type") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = answer,
                onValueChange = { answer = it },
                label = { Text("Answer") },
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
                    if (question.isEmpty()) {
                        isValid = false
                        msg = "Please Enter Valid question."
                    } else if (selectedQuestionType.isEmpty()) {
                        isValid = false
                        msg = "Please Enter Valid selected type."
                    } else if (answer.isEmpty()) {
                        isValid = false
                        msg = "Please Enter Valid Answer detailed."
                    }

                    if (isValid) {
                        key?.let {
                            val qaInfo = QAInfo(
                                id = "",
                                key = key,
                                title = question,
                                type = selectedQuestionType,
                                detailed = answer
                            )
                            FirestoreHelper.updateQAInfo(qaInfo, onSuccess = {
                                Toast.makeText(
                                    context,
                                    "QA Updated successfully::${qaInfo.title}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }, onFailure = {
                                Toast.makeText(
                                    context,
                                    "QA failed to update::${qaInfo.title}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        } ?: kotlin.run {
                            val qaInfo = QAInfo(
                                id = "",
                                title = question,
                                type = selectedQuestionType,
                                detailed = answer
                            )
                            FirestoreHelper.addQAInfo(qaInfo, onSuccess = {
                                Toast.makeText(
                                    context,
                                    "QA Added successfully::${qaInfo.title}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }, onFailure = {
                                Toast.makeText(
                                    context,
                                    "QA failed to added::${qaInfo.title}",
                                    Toast.LENGTH_SHORT
                                ).show()
                            })
                        }
                    } else {
                        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if(key.isNullOrEmpty()){"Post"}else{"Update"})
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("landing")
                }
            ) {
                Text("Main Screen")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = {
                    navController.navigate("post_technology")
                }
            ) {
                Text("Add Technology")
            }
        }
    }
}