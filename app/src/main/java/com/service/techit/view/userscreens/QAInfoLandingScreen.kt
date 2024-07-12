package com.service.techit.view.userscreens


import android.annotation.SuppressLint
import android.content.Intent
import android.speech.RecognizerIntent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.ScrollableTabRow
import androidx.compose.material.Tab
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Info
import androidx.compose.material3.ListItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
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
import com.service.techit.viewmodel.BackgroundViewModel
import com.service.techit.viewmodel.QAInfoViewModel
import com.service.techit.viewmodel.factory.TechITViewModelFactory
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import retrofit2.Retrofit


@OptIn( ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun QAInfoLandingScreen(technologyList:List<QAInfoTechnology>, navController: NavHostController, retrofit: Retrofit, provideQAInfoDao: QAInfoDao, backgroundViewModel: BackgroundViewModel = viewModel()) {
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

    val state = rememberCollapsingToolbarScaffoldState()
    var searchQuery by remember { mutableStateOf("") }
    val filteredItems = qaLists.filter { it.title?.contains(searchQuery,true) == true }
    var selectedTabIndex by remember { mutableStateOf(0) }
    val speechResultListener = rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        val intentResult = result.data
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

    CollapsingToolbarScaffold(
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
                                items(filteredItems.size) { position ->
                                    filteredItems.getOrNull(position)?.let { qaInfo ->
                                        Card(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .clickable {
                                                    navController.navigate("qaDetailed/${technology.name},${position}")
                                                }
                                                .padding(0.dp, 0.dp, 0.dp, 10.dp),
                                            shape = RoundedCornerShape(10.dp),
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
                                                    .background(Color.Transparent,
                                                        shape = RoundedCornerShape(10.dp)
                                                    ),

                                            )
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

            if (isLoading) {
                Dialog(
                    onDismissRequest = { isLoading},
                    DialogProperties(dismissOnBackPress = false, dismissOnClickOutside = false)
                ) {
                    Box(
                        contentAlignment= Alignment.Center,
                        modifier = Modifier
                            .size(100.dp)
                            .background(White, shape = RoundedCornerShape(8.dp))
                    ) {
                        CircularProgressIndicator()
                    }
                }
            }
        }
    }
}

