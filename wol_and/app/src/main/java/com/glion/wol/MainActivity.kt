package com.glion.wol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.glion.wol.ui.edit.EditScreen
import com.glion.wol.ui.main.TurnOnScreen
import com.glion.wol.ui.navigation.Edit
import com.glion.wol.ui.navigation.Main
import com.glion.wol.ui.theme.WOLTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
fun MainScreen() {
    WOLTheme {
        val snackbarHostState = remember { SnackbarHostState() }
        val navController = rememberNavController()

        NavHost(navController, startDestination = Main) {
            composable<Main> {
                TurnOnScreen(
                    sbHost = snackbarHostState,
                    goSetting = {
                        navController.navigate(route = Edit)
                    }
                )
            }
            composable<Edit> {
                EditScreen()
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}