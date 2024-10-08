package com.euriconeto.markupcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.euriconeto.markupcalculator.ui.di.sharedModule
import com.euriconeto.markupcalculator.ui.screen.MarkupScreen
import com.euriconeto.markupcalculator.ui.theme.MarkupCalculatorTheme
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startKoin {
            modules(sharedModule)
        }
        enableEdgeToEdge()
        setContent {
            MarkupCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MarkupScreen()
                }
            }
        }
    }
}
