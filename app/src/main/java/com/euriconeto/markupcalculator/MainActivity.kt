package com.euriconeto.markupcalculator

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.euriconeto.markupcalculator.di.appModule
import com.euriconeto.markupcalculator.di.dataBaseModule
import com.euriconeto.markupcalculator.ui.MarkupApp
import com.euriconeto.markupcalculator.ui.theme.MarkupCalculatorTheme
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MarkupCalculatorTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    MarkupApp()
                }
            }
        }
    }
}
