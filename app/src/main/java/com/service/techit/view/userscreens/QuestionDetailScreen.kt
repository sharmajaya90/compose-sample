package com.service.techit.view.userscreens

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.service.techit.model.QAInfo
import com.service.techit.model.database.dao.QAInfoDao
import com.service.techit.utils.CommonConstant
import com.service.techit.viewmodel.BackgroundViewModel
import kotlinx.coroutines.launch
import me.onebone.toolbar.CollapsingToolbarScaffold
import me.onebone.toolbar.ScrollStrategy
import me.onebone.toolbar.rememberCollapsingToolbarScaffoldState
import retrofit2.Retrofit

@OptIn( ExperimentalPagerApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun QuestionDetailScreen(technology:String,itemPosition:Int, navController: NavHostController, retrofit: Retrofit, provideQAInfoDao: QAInfoDao, backgroundViewModel: BackgroundViewModel = viewModel()) {
    val headerColor = backgroundViewModel.headerColor.collectAsState()
    val contentColor = backgroundViewModel.contentColor.collectAsState()
    val coroutineScope = rememberCoroutineScope()
    var qaLists by remember { mutableStateOf(listOf<QAInfo>()) }
    var qaInfo:QAInfo? by remember { mutableStateOf(QAInfo()) }
    var position:Int by remember { mutableIntStateOf(0) }
    var pageTitle by remember { mutableStateOf("") }
    val pagerState = rememberPagerState()
    val state = rememberCollapsingToolbarScaffoldState()
    LaunchedEffect(Unit) {
        position = itemPosition
        if(CommonConstant.technologies.containsKey(technology)){
            qaLists = (CommonConstant.technologies.get(technology) as? ArrayList)?: arrayListOf()
        }
        coroutineScope.launch {
            pagerState.scrollToPage(position)
        }
    }
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
                    text = pageTitle,
                    overflow = TextOverflow.Ellipsis,
                    color = headerColor.value,
                    modifier = Modifier.padding(15.dp)
                )

            }
        }

    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            HorizontalPager(
                count = qaLists.size,
                state = pagerState,
                userScrollEnabled = true
            ) { page ->
                qaInfo = qaLists.get(page)
                pageTitle = qaInfo?.title ?: ""
                val scrollState = rememberScrollState()
                // Our page content
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = "*${qaInfo?.title}",
                        style = MaterialTheme.typography.h5,
                        fontWeight = FontWeight.Bold,
                        color = contentColor.value
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = qaInfo?.detailed ?: "",
                        style = MaterialTheme.typography.body1,
                        color = contentColor.value
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }

}