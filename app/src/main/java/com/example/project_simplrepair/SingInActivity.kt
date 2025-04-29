package com.example.project_simplrepair

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.compose.ProjectSimplRepairTheme
import com.example.project_simplrepair.Screens.SignInScreen


class SingInActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ProjectSimplRepairTheme{
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Red)
                ) { innerPadding ->
                    val context: Context = applicationContext

                    SignInScreen(
                        context = context,
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(20.dp))
                    )
                }
            }
        }
    }
}



