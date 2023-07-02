package net.limbuserendipity.customjuicecompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.ui.Modifier
import net.limbuserendipity.customjuicecompose.ui.screen.ClearPaperScreen
import net.limbuserendipity.customjuicecompose.ui.screen.JuiceScreen
import net.limbuserendipity.customjuicecompose.ui.screen.TestScreen
import net.limbuserendipity.customjuicecompose.ui.theme.CustomJuiceComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CustomJuiceComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    JuiceScreen()
                }
            }
        }
    }
}