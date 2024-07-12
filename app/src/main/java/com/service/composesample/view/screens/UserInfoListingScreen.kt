package com.service.composesample.view.screens
import android.annotation.SuppressLint
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.service.composesample.R
import com.service.composesample.extensions.truncateText
import com.service.composesample.model.UserInfo
import com.service.composesample.utils.firebase.FirestoreHelper
import com.service.composesample.view.helperui.NoDataFoundScreen
import kotlinx.coroutines.runBlocking
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.ToolbarWithFabScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import java.util.Locale


@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun UserInfoListingScreen(navController: NavHostController) {
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    var userLists by remember { mutableStateOf(listOf<UserInfo>()) }
    val state = rememberCollapsingToolbarScaffoldState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = userLists.filter { it.name.contains(searchQuery,true) }
    var selectedTabIndex by remember { mutableIntStateOf(0) }
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
                    navController.navigate("post_qa")
                }
            ) {
                Icon(Icons.Filled.Add,"")
            }
        }
    ) {
        Column {
            SearchBar(
                query = searchQuery,
                onQueryChange = { searchQuery = it },
                onSearch = {

                },
                speechResultListener
            )

            ScrollableTabRow(
                selectedTabIndex = selectedTabIndex,
                edgePadding = 0.dp,
                backgroundColor = Color.Transparent,
                contentColor = Color.Blue,
                indicator = { tabPositions ->
                    // Hide default indicator
                },
                divider = { /* No divider */ }
            ) {
                arrayListOf<String>().forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(text = title?:"",
                                color = Color.White,
                                modifier = Modifier
                                    .background(
                                        if (selectedTabIndex == index) Color.DarkGray else Color.Transparent,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .padding(vertical = 8.dp, horizontal = 15.dp)

                            )}
                    )
                }
            }

            HorizontalPager(
                count = 3,
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                LaunchedEffect(selectedTabIndex){
                    val selectedTab = arrayListOf("Online","Offline")[selectedTabIndex]
                    runBlocking {
                        userLists = if(selectedTab.equals("Online")) FirestoreHelper.getUserInfoList() else FirestoreHelper.getUserInfoList()
                    }
                }
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Transparent),
                    contentAlignment = Alignment.Center
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(10.dp)
                            .background(Color.Transparent)
                    ) {
                        if (filteredItems.isNotEmpty()) {
                            items(filteredItems.size) { index ->
                                filteredItems.getOrNull(index)?.let { userInfo ->
                                    SwipeBox(
                                        onDelete = {
                                        },
                                        onEdit = {
                                            val userInfoKey = userInfo.key
                                            navController.navigate("userEdited/${userInfoKey}")
                                        },
                                        modifier = Modifier
                                            .animateItemPlacement()
                                            .padding(5.dp),
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    navController.navigate("user_detailed/${userInfo.key}")
                                                },
                                            shape = RoundedCornerShape(10.dp),
                                            backgroundColor = Color.Transparent,
                                            elevation = 5.dp

                                        ) {
                                            ListItem(headlineContent = { Text(text = userInfo.name ?: "", fontWeight = FontWeight.Bold) },
                                                supportingContent = {
                                                    Text(
                                                        text = userInfo.address.truncateText(
                                                            50
                                                        ) ?: ""
                                                    )
                                                },
                                                leadingContent = {
                                                    Icon(
                                                        imageVector = Icons.Rounded.Info,
                                                        contentDescription = null,
                                                        modifier = Modifier.padding(5.dp)
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
            .padding(15.dp,5.dp,15.dp,5.dp),
        leadingIcon = {  Icon(imageVector = Icons.Default.Search, contentDescription = "Search Icon",modifier = Modifier.padding(5.dp,0.dp,0.dp,0.dp)) },
        placeholder = {
            Text(text = "Search...")
        },
        trailingIcon = {
            Image(painterResource(R.drawable.ic_aibuilder_speak_text),"Speak Icon",modifier = Modifier.padding(5.dp,0.dp,0.dp,0.dp).clickable {
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to type your query")
            try {
                speechResultListener.launch(intent)
            } catch (e: Exception) {
                e.printStackTrace()
            }

        }) },
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