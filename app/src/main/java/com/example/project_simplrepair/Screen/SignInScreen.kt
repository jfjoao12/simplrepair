package com.example.project_simplrepair.Screen

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import com.example.project_simplrepair.MainActivity
import com.example.project_simplrepair.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignInScreen(
    context:Context,
    modifier: Modifier = Modifier
) {
    // state level variables
    var email by remember {mutableStateOf("")}
    var password by remember {mutableStateOf("")}
    var keyboardController = LocalSoftwareKeyboardController.current

    Box(
        modifier = Modifier
            .padding(25.dp)
    ) {

            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Simpl Repair logo",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
                alignment = Alignment.BottomCenter
            )



        Column(
            modifier = modifier
                .fillMaxSize(1f)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){




            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .semantics { contentDescription = "Sign in e-mail" },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Email)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding( 8.dp)
                    .semantics { contentDescription = "Sign in password" },
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Password)
            )
            Button(
                onClick = {
                    if (email.isBlank() || password.isBlank()) {
                        Toast.makeText(context, "Email or Password cannot be empty", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    keyboardController?.hide()
                    performsSignIn(email, password, context, keyboardController)

                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
                    .semantics { contentDescription = "Sign in button" }
            ) {
                Text("Sign In")
            }


        }
    }

}
private fun performsSignIn(
    email:String,
    password:String,
    context: Context,
    keyboardController: SoftwareKeyboardController?
){
    val auth = FirebaseAuth.getInstance()

    auth.signInWithEmailAndPassword(email,password)
        .addOnCompleteListener{task->
            if (task.isSuccessful){
                Toast.makeText(context, "Sign In Successful", Toast.LENGTH_SHORT).show()
                var intent = Intent(context, MainActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                intent.putExtra("userID", FirebaseAuth.getInstance().currentUser?.uid)
                context.startActivity(intent)
            }else{
                Toast.makeText(context, "Sign In Failed", Toast.LENGTH_LONG).show()
            }
            keyboardController?.hide()
        }
}