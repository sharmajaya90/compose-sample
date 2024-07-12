package com.service.techit.view.adminscreens

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.service.techit.model.QAInfoTechnology
import com.service.techit.utils.firebase.FirestoreHelper


@Composable
fun AddTechnologyScreen(navController: NavHostController) {
    var technology by remember { mutableStateOf("") }
    val context = LocalContext.current
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp).verticalScroll(scrollState),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Add New Technology",
            style = MaterialTheme.typography.h5,
            modifier = Modifier.padding(bottom = 16.dp)
        )

        OutlinedTextField(
            value = technology,
            onValueChange = { technology = it },
            label = { Text("Question") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                // Handle post action
                var isValid = true
                var msg = ""
                if(technology.isEmpty()){
                    isValid = false
                    msg = "Please Enter Valid Technology."
                }

                if(isValid){
                   val qaInfoTechnology = QAInfoTechnology(
                       id =technology.length,
                       title = technology,
                       name = technology.lowercase())
                    FirestoreHelper.addQAInfoTechnology(qaInfoTechnology, onSuccess = {
                        Toast.makeText(context, "New Technology Added successfully::${qaInfoTechnology.title}", Toast.LENGTH_SHORT).show()
                    }, onFailure = {
                        Toast.makeText(context, "New Technology failed to save::${qaInfoTechnology.title}", Toast.LENGTH_SHORT).show()
                    })
                }else{
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Post")
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                navController.navigate("landing")
            }
        ) {
            Text("Main Screen")
        }
    }
}
