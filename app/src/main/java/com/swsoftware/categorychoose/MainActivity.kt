package com.swsoftware.categorychoose

import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle

import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.swsoftware.categorychoose.ui.theme.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val categories = mutableStateListOf(
            "Юмор",
            "Еда",
            "Кино",
            "Рестораны",
            "Спорт",
            "Рецепты",
            "Работа",
            "Прогулки",
            "Новости",
            "Политика"
        )
        val chosenCategoriesList = mutableStateListOf<String>()

        setContent {
            CategoryChooseTheme {
                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colors.background)
                    .padding(0.dp, 40.dp, 0.dp, 10.dp)) {
                    ChangeSystemUiColor()
                    Header(chosenCategoriesList.isEmpty())
                    Spacer(modifier = Modifier.height(20.dp))
                    LazyColumn {
                        items(categories, key = { categoryItem ->
                            categoryItem.hashCode()
                        }) { category ->
                            CategoryItem(text = category) { isChosen ->
                                if (isChosen) {
                                    chosenCategoriesList.add(category)
                                } else {
                                    chosenCategoriesList.remove(category)
                                }
                            }
                            // divider
                            Spacer(modifier = Modifier.fillMaxWidth()
                                .height(2.dp)
                                .background(MaterialTheme.colors.primaryVariant))
                        }
                    }
                }
            }
        }
    }
}


// change status bar depending on system theme
@Composable
fun ChangeSystemUiColor() {
    val systemUiController = rememberSystemUiController()
    systemUiController.setStatusBarColor(MaterialTheme.colors.background, darkIcons = !isSystemInDarkTheme())
}


// header with hint and buttons
@Composable
fun Header(continueButtonDisabled: Boolean) {
    val greetingColor = MaterialTheme.colors.onSecondary.copy(alpha = .5F)
    Column(modifier = Modifier
        .fillMaxWidth()
        .padding(10.dp, 0.dp)) {
        Text(text = stringResource(id = R.string.greeting),
            style = TextStyle(
                color = greetingColor,
                fontSize = 18.sp,
            )
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row {
            val buttonsModifier = Modifier
                .weight(.5f)
                .height(50.dp)
            LaterButton(buttonsModifier)
            Spacer(modifier = Modifier.width(10.dp))
            ContinueButton(buttonsModifier, continueButtonDisabled)
        }
    }
}