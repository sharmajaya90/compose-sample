package com.service.techit.view.adminscreens
import android.annotation.SuppressLint
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.service.techit.extensions.truncateText
import com.service.techit.model.QAInfo
import com.service.techit.model.QAInfoTechnology
import com.service.techit.model.database.dao.QAInfoDao
import com.service.techit.utils.CommonConstant
import com.service.techit.utils.firebase.FirestoreHelper
import com.service.techit.view.helperui.NoDataFoundScreen
import com.service.techit.view.userscreens.SearchBar
import com.service.techit.viewmodel.BackgroundViewModel
import com.service.techit.viewmodel.QAInfoViewModel
import com.service.techit.viewmodel.factory.TechITViewModelFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.ToolbarWithFabScaffold
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import retrofit2.Retrofit


@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun AdminQAInfoListingScreen(technologyList:List<QAInfoTechnology>, navController: NavHostController, retrofit: Retrofit, provideQAInfoDao: QAInfoDao, backgroundViewModel: BackgroundViewModel = viewModel()) {
    var technology by remember { mutableStateOf(QAInfoTechnology()) }
    val pagerState = rememberPagerState()
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val headerColor = backgroundViewModel.headerColor.collectAsState()
    val language = backgroundViewModel.language.collectAsState()
    var qaLists by remember { mutableStateOf(listOf<QAInfo>()) }
    val qaInfoViewModel: QAInfoViewModel = viewModel(factory = TechITViewModelFactory(retrofit=retrofit, qaInfoDao = provideQAInfoDao))
    //val qaLists by qaInfoViewModel.firestoreQAInfoList.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var isActionIntent by remember { mutableStateOf(false) }

    val state = rememberCollapsingToolbarScaffoldState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = qaLists.filter { it.title?.contains(searchQuery,true) == true }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val speechResultListener = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {result ->
        val intentResult = result?.data
        if (null != intentResult) {
            val suggestionListener: ArrayList<String>? =
                intentResult.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val topPick = suggestionListener?.getOrNull(0)
            searchQuery = topPick?:""
        }
    }
    LaunchedEffect(Unit) {
        runBlocking {
            technologyList.getOrNull(0)?.let {
                technology = it
            }
        }
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
                    text = technology.title?:"Technology",
                    color = headerColor.value,
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
                technologyList.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = {
                            Text(text = title.title?:"",
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
                count = technologyList.size,
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                LaunchedEffect(selectedTabIndex){
                    technology = technologyList[selectedTabIndex]
                    if(CommonConstant.technologies.containsKey(technology.name)){
                        qaLists = (CommonConstant.technologies.get(technology.name) as? ArrayList)?: arrayListOf()
                    }else {
                        FirestoreHelper.collectionUrlPath = "techit_qainfo_${technology.name}"
                        coroutineScope.async {
                            isLoading = true
                            qaLists = FirestoreHelper.getQAInfoList()
                            CommonConstant.technologies.put(
                                technology.name ?: "",
                                qaLists as ArrayList<QAInfo>
                            )
                            isLoading = false
                        }
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
                                filteredItems.getOrNull(index)?.let { qaInfo ->
                                    SwipeBox(
                                        onDelete = {
                                            isActionIntent = true
                                        },
                                        onEdit = {
                                            val qaInfoKey = qaInfo.key
                                            navController.navigate("qaEdited/${qaInfoKey}")
                                        },
                                        modifier = Modifier
                                            .animateItemPlacement()
                                            .padding(5.dp),
                                    ) {
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    val position = index
                                                    navController.navigate("qaDetailed/${technology.name},${position}")
                                                },
                                            shape = RoundedCornerShape(10.dp),
                                            backgroundColor = Color.Transparent,
                                            elevation = 5.dp

                                        ) {
                                            ListItem(headlineContent = { Text(text = qaInfo.title ?: "", fontWeight = FontWeight.Bold) },
                                                supportingContent = {
                                                    Text(
                                                        text = qaInfo.detailed?.truncateText(
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
                        } else if (qaLists.isEmpty()) {
                            item {
                                NoDataFoundScreen("${language.value.noData}")
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
