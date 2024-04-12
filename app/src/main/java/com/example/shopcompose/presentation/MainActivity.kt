@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.shopcompose.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.shopcompose.data.data_structure.ClothesItem
import com.example.shopcompose.domain.utils.Resource
import com.example.shopcompose.presentation.destinations.DetailsScreenDestination
import com.example.shopcompose.presentation.destinations.MainScreenDestination
import com.example.shopcompose.presentation.destinations.PurchasePageDestination
import com.example.shopcompose.presentation.theme.ShopComposeTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.skydoves.landscapist.glide.GlideImage
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ShopComposeTheme {

                DestinationsNavHost(navGraph = NavGraphs.root)

            }
        }
    }
}

@Destination
@RootNavGraph(start = true)
@Composable
fun MainScreen(
    viewModel: ShoppingListViewModel = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator
) {
    val listState = viewModel.scrollState
    val scope = rememberCoroutineScope()
    Scaffold(
        topBar = {
            AppsTopAppBar("Fake Store", navigator)
        },
        content = {
            LazyColumnMain(navigator = navigator, padding = it, listState = listState)
            val showButton by remember {
                derivedStateOf {
                    listState.firstVisibleItemIndex > 0 && !listState.isScrollInProgress
                }
            }
            AnimatedVisibility(
                visible = showButton,
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                ScrollToTopButton(onClick = {
                    scope.launch {
                        listState.animateScrollToItem(0)
                    }
                })
            }
        },
    )
}

// @Preview(showBackground = true)
@Composable
fun CategoryChip(
    list: List<ClothesItem>, viewModel: ShoppingListViewModel, listState: LazyListState
) {

    val coroutineScope = rememberCoroutineScope()

    val listPrice = listOf(
        99999999, 25, 50, 100, 150, 200, 250, 300, 350, 400
    )

    val displayList = mutableListOf<String>()
    list.forEach {
        displayList.add(it.category.replaceFirstChar { char ->
            char.uppercase()
        })
    }
    displayList.add(0, "All")
    val displayList1 = displayList.toSet().toList()

    fun screenScrollUpdate() {
        coroutineScope.launch {
            listState.animateScrollToItem(1)
            listState.animateScrollToItem(0)
        }
    }

    Column {
        Text(text = "Sort by:", modifier = Modifier.fillMaxWidth(), textAlign = TextAlign.Center)
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Price:", textAlign = TextAlign.Center)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(listPrice) {
                    Card(modifier = Modifier.padding(horizontal = 8.dp), onClick = {
                        viewModel.updatePrice(it)
                        viewModel.priceState
                        viewModel.updateCat("All")
                        screenScrollUpdate()
                        Log.i(
                            "DebuggingCategoryChipsPrice", viewModel.priceState.value.toString()
                        )
                    }) {
                        if (it == 99999999) {

                            Text(
                                text = "All",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        } else {
                            Text(
                                text = "Under $${it}",
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                            )
                        }
                    }
                }
            }
        }
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Category:", textAlign = TextAlign.Center)
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(displayList1) {
                    Card(modifier = Modifier.padding(horizontal = 4.dp), onClick = {
                        viewModel.updateCat(it)
                        viewModel.catState
                        viewModel.updatePrice(999999)
                        screenScrollUpdate()
                        Log.i("DebuggingCategoryChipsCat", viewModel.catState.toString())
                    }) {
                        Text(
                            text = it,
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                        )
                    }
                }
            }
        }
    }
}


@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun LazyColumnMain(
    viewModel: ShoppingListViewModel = viewModel(LocalContext.current as ComponentActivity),
    navigator: DestinationsNavigator,
    padding: PaddingValues,
    listState: LazyListState
) {
    val state by viewModel.itemsList.collectAsState()
    val list = state.data?.asReversed()
    Log.i("DebugNetworkLazyState", list.toString())

    when (state) {
        is Resource.Success -> {
            list?.let {
                Column(modifier = Modifier.padding(padding)) {

                    CategoryChip(list, viewModel, listState)

                    LazyColumn(
                        modifier = Modifier.fillMaxHeight(), state = listState
                    ) {
                        items(list) {
                            if (viewModel.catState.value == "All" && it.price < viewModel.priceState.value) {
                                AllItems(it, navigator)
                            } else if (it.category == viewModel.catState.value.replaceFirstChar { char ->
                                    char.lowercase()
                                } && it.price < viewModel.priceState.value) {
                                SortedItems(navigator, it)
                            }
                        }
                    }
                }
            }
        }

        is Resource.Loading -> {
            ShowLoadingIndicator()
            Log.i("DebugNetworkMainLoading", "Loading")
        }

        is Resource.Error -> {
            Log.i("DebugNetworkMainError", "Error")
            state.message?.let { ShowErrorMessage(message = it) }
            LaunchedEffect(key1 = "") {
                delay(5000)
                viewModel.getData()
                Log.i("MainScreen", "reloading after error")
            }
        }
    }
}

@Composable
private fun SortedItems(
    navigator: DestinationsNavigator, it: ClothesItem
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        onClick = {
            navigator.navigate(
                DetailsScreenDestination(
                    name = it.title,
                    image = it.image,
                    description = it.description,
                    price = it.price,
                    category = it.category
                )
            )
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            GlideImage(
                imageModel = it.image,
                modifier = Modifier
                    .width(130.dp)
                    .padding(10.dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier) {
                Text(
                    text = it.title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(
                        top = 10.dp, end = 10.dp
                    ), maxLines = 2
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${it.rating.rate} /5", fontSize = 12.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Stars",
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(
                    text = it.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        top = 4.dp, end = 10.dp
                    ),
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 10.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = it.category,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = "$${it.price}",
                            textAlign = TextAlign.Right,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun AllItems(it: ClothesItem, navigator: DestinationsNavigator) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 5.dp),
        onClick = {
            navigator.navigate(
                DetailsScreenDestination(
                    name = it.title,
                    image = it.image,
                    description = it.description,
                    price = it.price,
                    category = it.category
                )
            )
        }) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
        ) {
            GlideImage(
                imageModel = it.image,
                modifier = Modifier
                    .width(130.dp)
                    .padding(10.dp),
                contentScale = ContentScale.Fit
            )
            Column(modifier = Modifier) {
                Text(
                    text = it.title, fontWeight = FontWeight.Bold, modifier = Modifier.padding(
                        top = 10.dp, end = 10.dp
                    ), maxLines = 2
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${it.rating.rate} /5", fontSize = 12.sp
                    )
                    Icon(
                        imageVector = Icons.Filled.Star,
                        contentDescription = "Stars",
                        modifier = Modifier.size(12.dp)
                    )
                }
                Text(
                    text = it.description,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(
                        top = 4.dp, end = 10.dp
                    ),
                    fontSize = 14.sp,
                    lineHeight = 18.sp
                )
                Row(
                    modifier = Modifier
                        .fillMaxHeight()
                        .padding(bottom = 10.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = it.category,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier,
                            fontWeight = FontWeight.Light
                        )
                    }
                    Box(
                        contentAlignment = Alignment.BottomCenter,
                        modifier = Modifier.fillMaxHeight()
                    ) {
                        Text(
                            text = "$${it.price}",
                            textAlign = TextAlign.Right,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(end = 10.dp),
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@Destination
@Composable
fun DetailsScreen(
    navigator: DestinationsNavigator,
    name: String,
    image: String,
    description: String,
    price: Double,
    category: String
) {
    Scaffold(
        topBar = {
            AppsTopAppBar("Item details", navigator)
        },
        content = {
            DetailsPage(
                navigator = navigator, padding = it, name, image, description, price, category
            )
        },
    )
}

@Destination
@Composable
fun DetailsPage(
    navigator: DestinationsNavigator,
    padding: PaddingValues,
    name: String,
    image: String,
    description: String,
    price: Double,
    category: String,
) {

    Box(
        contentAlignment = Alignment.Center, modifier = Modifier
            .fillMaxSize()
            .padding(padding)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .fillMaxHeight()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                contentAlignment = Alignment.Center
            ) {
                GlideImage(
                    imageModel = image,
                    modifier = Modifier.padding(10.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                text = name.replace("+", " "),
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 10.dp, end = 10.dp),
                maxLines = 2,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = category.replace("+", " "), fontWeight = FontWeight.Light)
            Text(
                text = description.replace("+", " "),
                maxLines = 3,
                modifier = Modifier.padding(top = 10.dp, end = 10.dp),
                fontSize = 12.sp,
                lineHeight = 12.sp
            )
            Spacer(modifier = Modifier.height(8.dp))
            Box(
                contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = "$${price}",
                    textAlign = TextAlign.Right,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp),
                    fontWeight = FontWeight.Bold
                )
            }
            Button(onClick = { navigator.navigate(PurchasePageDestination()) }) {
                Text(text = "Purchase")
            }
        }
    }
}

@Destination
@Composable
fun PurchasePage(navigator: DestinationsNavigator) {
    Scaffold(topBar = {
        AppsTopAppBar("Purchase not implemented", navigator)
    }, content = {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it), contentAlignment = Alignment.Center
        ) {
            Column {
                androidx.compose.material3.Text(
                    text = "Purchase Page",
                    style = MaterialTheme.typography.displayMedium,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                Box(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = { navigator.navigate(MainScreenDestination()) },
                        modifier = Modifier
                            .align(Alignment.Center)
                            .fillMaxWidth(0.4f),
                    ) {
                        Text(text = "Go Main Screen")
                    }
                }
            }
        }
    })
}

@Composable
fun ShowLoadingIndicator() {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Destination
@Composable
fun ShowErrorMessage(
    viewModel: ShoppingListViewModel = viewModel(LocalContext.current as ComponentActivity),
    message: String
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column {
            androidx.compose.material3.Text(
                text = "$message, it will reload soon",
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center
            )
            Box(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = { viewModel.getData() },
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxWidth(0.3f),
                ) {
                    Text(text = "Reload")
                }
            }
        }
    }
}

@Composable
fun ScrollToTopButton(onClick: () -> Unit) {
    Box(
        Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp), Alignment.BottomCenter
    ) {
        Button(
            onClick = { onClick() },
            modifier = Modifier
                .shadow(10.dp, shape = CircleShape)
                .size(65.dp),
            colors = ButtonDefaults.buttonColors(
                contentColor = MaterialTheme.colorScheme.primary
            ),
        ) {
            Icon(
                Icons.Filled.KeyboardArrowUp,
                "arrow up",
            )
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun ForPreview() {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = "3.6 /5", fontSize = 12.sp)
        Icon(
            imageVector = Icons.Filled.Star,
            contentDescription = "Stars",
            modifier = Modifier.size(12.dp)
        )
    }
}
