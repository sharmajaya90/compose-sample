package com.service.composesample.view.screens
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.speech.RecognizerIntent
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.auth.User
import com.google.gson.Gson
import com.service.composesample.R
import com.service.composesample.extensions.truncateText
import com.service.composesample.model.UserInfo
import com.service.composesample.model.database.dao.UserInfoDao
import com.service.composesample.model.toUser
import com.service.composesample.utils.firebase.FirestoreHelper
import com.service.composesample.view.helperui.NoDataFoundScreen
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.ToolbarWithFabScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import retrofit2.Retrofit
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserInfoListingScreen(navController: NavHostController, retrofit: Retrofit, userInfoDao: UserInfoDao) {
    var userLists by remember { mutableStateOf(listOf<UserInfo>()) }
    var selectedPosition by remember { mutableIntStateOf(0) }
    val context = LocalContext.current
    val state = rememberCollapsingToolbarScaffoldState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = userLists.filter { it.name.contains(searchQuery,true) }
    val speechResultListener = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        val intentResult = result.data
        if (null != intentResult) {
            val suggestionListener: ArrayList<String>? =
                intentResult.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val topPick = suggestionListener?.getOrNull(0)
            searchQuery = topPick?:""
        }
    }

    LaunchedEffect(Unit) {
        userInfoDao.fetchAllRegisterUsers().collect{
            userLists = it.map {
                it.toUser()
            }
        }
    }

    var showDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    if (showDialog) {
        ImagePickerDialog(
            onDismiss = { showDialog = false },
            onImagePicked = { uri ->
                showDialog = false
                imageUri = uri
            }
        )
    }


    var confirmationDialog by remember { mutableStateOf(false) }
    var actionIntent by remember { mutableStateOf<String>("") }

    if (confirmationDialog) {
        ConfirmationDialog(
            onDismiss = { confirmationDialog = false },
            onIntentSelected = { action ->
                confirmationDialog = false
                actionIntent = action
                if(action == "ok"){
                    if( userInfoDao.deleteUserInfoWithEmail(userLists.get(selectedPosition).email) > 0){
                        Toast.makeText(context,"User ${userLists.get(selectedPosition).name} Deleted Successfully",Toast.LENGTH_SHORT).show()
                    }else{
                        Toast.makeText(context,"User not Deleted Yet",Toast.LENGTH_SHORT).show()
                    }
                }

            }
        )
    }


    ToolbarWithFabScaffold(
        modifier = Modifier
            .fillMaxSize(),
        state = state,
        scrollStrategy = ScrollStrategy.EnterAlways,
        toolbar = {
            Row(modifier = Modifier
                .fillMaxWidth()) {
                Text(
                    textAlign = TextAlign.Center,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    text = "User List",
                    color = Color.Black,
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth(),
                ) }
        },
        fab = {
            FloatingActionButton(
                onClick = {
                    // Handle guest login
                    navController.navigate("register")
                }
            ) {
                Icon(Icons.Filled.Add,"")
            }
        }
    ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent),
                contentAlignment = Alignment.Center
            ) {

                Column {
                    SearchBar(
                        query = searchQuery,
                        onQueryChange = { searchQuery = it },
                        onSearch = {

                        },
                        speechResultListener
                    )
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .background(Color.Transparent)
                    ) {
                        if (filteredItems?.isNotEmpty() == true) {
                            items(filteredItems.size) { index ->
                                filteredItems.getOrNull(index)?.let { userInfo ->
                                    SwipeBox(
                                        onDelete = {
                                            selectedPosition = index
                                            confirmationDialog = true
                                            showDialog = false
                                        },
                                        onEdit = {
                                            val userInfo = Gson().toJson(userInfo)
                                            navController.navigate("user_edited/$userInfo")
                                        },
                                        modifier = Modifier
                                            .animateItemPlacement()
                                            .padding(5.dp),
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    val userInfo = Gson().toJson(userInfo)
                                                    navController.navigate("detailed/$userInfo")
                                                },
                                            shape = RoundedCornerShape(10.dp),
                                            backgroundColor = Color.Transparent,
                                            elevation = 5.dp

                                        ) {
                                            ListItem(
                                                headlineContent = {
                                                    Text(
                                                        text = userInfo.name ?: "",
                                                        fontWeight = FontWeight.Bold
                                                    )
                                                },
                                                supportingContent = {
                                                    Text(
                                                        text = userInfo.email.truncateText(
                                                            50
                                                        ) ?: ""
                                                    )
                                                },
                                                leadingContent = {
                                                    ProfileImageView(
                                                        imageUri = imageUri,
                                                        onEditClick = { /* Handle edit click */
                                                            showDialog = true
                                                        }
                                                    )
                                                },
                                                modifier = Modifier
                                                    .background(
                                                        shape = RoundedCornerShape(10.dp),
                                                        color = Color.Transparent
                                                    ),
                                            )
                                        }
                                    }
                                }
                            }
                        } else if (filteredItems.isEmpty()) {
                            item {
                                NoDataFoundScreen("No Data Found!!")
                            }
                        }
                    }
                }

            }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SwipeBox(
    modifier: Modifier = Modifier,
    onDelete: () -> Unit,
    onEdit: () -> Unit,
    content: @Composable () -> Unit
) {
    val swipeState = rememberSwipeToDismissBoxState()

    lateinit var icon: ImageVector
    lateinit var alignment: Alignment
    var color: Color = Color.Transparent


    when (swipeState.dismissDirection) {
        SwipeToDismissBoxValue.EndToStart -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = Color.Red
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            icon = Icons.Outlined.Edit
            alignment = Alignment.CenterStart
            color = Color.Green.copy(alpha = 0.7f) // You can generate theme for successContainer in themeBuilder
        }

        SwipeToDismissBoxValue.Settled -> {
            icon = Icons.Outlined.Delete
            alignment = Alignment.CenterEnd
            color = Color.Transparent
        }
    }

    SwipeToDismissBox(
        modifier = modifier.animateContentSize(),
        state = swipeState,
        backgroundContent = {
            Box(
                contentAlignment = alignment,
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
            ) {
                Icon(
                    modifier = Modifier.minimumInteractiveComponentSize(),
                    imageVector = icon, contentDescription = null
                )
            }
        }
    ) {
        content()
    }

    when (swipeState.currentValue) {
        SwipeToDismissBoxValue.EndToStart -> {
            onDelete()
        }

        SwipeToDismissBoxValue.StartToEnd -> {
            LaunchedEffect(swipeState) {
                onEdit()
                swipeState.snapTo(SwipeToDismissBoxValue.Settled)
            }
        }

        SwipeToDismissBoxValue.Settled -> {
        }
    }
}


@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    speechResultListener: ActivityResultLauncher<Intent>
) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color.Transparent,
            )
            .padding(15.dp, 5.dp, 15.dp, 5.dp),
        leadingIcon = {  Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon",modifier = Modifier.padding(5.dp,0.dp,0.dp,0.dp)) },
        placeholder = {
            Text(text = "Search...")
        },
        singleLine = true,
        keyboardOptions = KeyboardOptions.Default.copy(
            imeAction = ImeAction.Search
        ),
        keyboardActions = KeyboardActions(
            onSearch = {
                onSearch()
            }
        ),
    )

}


@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun ImagePickerDialog(
    onDismiss: () -> Unit,
    onImagePicked: (Uri) -> Unit
) {
    val context = LocalContext.current
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    var cameraUri by remember { mutableStateOf<Uri?>(null) }
    var bitmap by remember { mutableStateOf<Bitmap?>(null) }
    val cameraPermissionState = rememberPermissionState(permission = android.Manifest.permission.CAMERA)
    val writeExternalStoragePermissionState = rememberPermissionState(permission = android.Manifest.permission.WRITE_EXTERNAL_STORAGE)


    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            onImagePicked(it)
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, it)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, it)
                ImageDecoder.decodeBitmap(source)
            }

        }
    }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri = cameraUri
            cameraUri?.let {
                onImagePicked(it)
            }
            bitmap = if (Build.VERSION.SDK_INT < 28) {
                MediaStore.Images.Media.getBitmap(context.contentResolver, cameraUri)
            } else {
                val source = cameraUri?.let {
                    ImageDecoder.createSource(context.contentResolver,
                        it
                    )
                }
                source?.let{ ImageDecoder.decodeBitmap(source) }
            }
        }
    }


    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Select Image", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    when {
                        cameraPermissionState.status.isGranted -> {
                            val photoFile: File? = try {
                                createImageFile(context)
                            } catch (ex: IOException) {
                                // Error occurred while creating the File
                                null
                            }
                            // Continue only if the File was successfully created
                            photoFile?.also {
                                val photoURI: Uri = FileProvider.getUriForFile(
                                    context,
                                    "${context.packageName}.provider",
                                    it
                                )
                                cameraUri = photoURI
                                cameraLauncher.launch(photoURI)
                            }

                        }
                        cameraPermissionState.status.shouldShowRationale || writeExternalStoragePermissionState.status.shouldShowRationale -> {
                            Toast.makeText(context, "Camera and storage permission are required to take pictures", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            cameraPermissionState.launchPermissionRequest()
                            writeExternalStoragePermissionState.launchPermissionRequest()
                        }
                    }
                }) {
                    Text("Camera")
                }
                Spacer(modifier = Modifier
                    .height(5.dp)
                    .padding(5.dp))
                Button(onClick = {
                    galleryLauncher.launch("image/*")
                }) {
                    Text("Gallery")
                }
            }
        }
    )
}

@Composable
fun ConfirmationDialog(
    onDismiss: () -> Unit,
    onIntentSelected: (String) -> Unit
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text("Select Image", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
        buttons = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(onClick = {
                    onIntentSelected("cancel")
                }) {
                    Text("Cancel")
                }
                Spacer(modifier = Modifier
                    .height(5.dp)
                    .padding(5.dp))
                Button(onClick = {
                    onIntentSelected("ok")
                }) {
                    Text("OK")
                }
            }
        }
    )
}

@Throws(IOException::class)
private fun createImageFile(context: Context): File {
    // Create an image file name
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
    return File.createTempFile(
        "JPEG_${timeStamp}_", /* prefix */
        ".jpg", /* suffix */
        storageDir /* directory */
    ).apply {
        // Save a file: path for use with ACTION_VIEW intents
        Log.e("absolutePath:",absolutePath)
    }
}


@Composable
fun ProfileImageView(imageUri: Uri?, onEditClick: () -> Unit) {
    val context = LocalContext.current
    val painter = rememberImagePainter(
        data = imageUri,
        builder = {
            crossfade(true)
            placeholder(R.drawable.android)
            error(R.drawable.android)
            transformations(CircleCropTransformation())
        }
    )


    Box(
        modifier = Modifier
            .size(65.dp)
    ) {
        Image(
            painter = painter,
            contentDescription = null,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        Icon(
            imageVector = Icons.Default.Edit,
            contentDescription = "Edit",
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(24.dp)
                .clip(CircleShape)
                .background(Color.White)
                .clickable { onEditClick() }
                .padding(4.dp)
        )
    }
}

suspend fun uploadBitmapToFirestore(bitmap: Bitmap) {
    val byteArray = bitmapToByteArray(bitmap)
    val db = FirebaseFirestore.getInstance()
    val collection = db.collection("user_images")

    try {
        collection.add(mapOf("bitmapData" to byteArray)).await()
    } catch (e: Exception) {
        // Handle error
    }
}

suspend fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
    val stream = ByteArrayOutputStream()
    bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
    return stream.toByteArray()
}