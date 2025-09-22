package com.glion.wol

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.glion.wol.ui.edit.EditScreen
import com.glion.wol.ui.main.TurnOnScreen
import com.glion.wol.ui.navigation.Edit
import com.glion.wol.ui.navigation.Main
import com.glion.wol.ui.navigation.Splash
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

        Scaffold(
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
            // 화면 하단에 뜨는 Snackbar 가 키보드에 의해 가려지지 않도록 화면의 실제 Bottom 영역에 뜨도록 지정
            modifier = Modifier.windowInsetsPadding(WindowInsets.ime)
        ) { _ ->
            NavHost(navController, startDestination = Main){
                composable<Splash> {

                }
                composable<Main> {
                    TurnOnScreen(
                        sbHost = snackbarHostState,
                        goSetting = {
                            navController.navigate(route = Edit)
                        }
                    )
                }
                composable<Edit> {
                    EditScreen(
                        sbHost = snackbarHostState
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    MainScreen()
}