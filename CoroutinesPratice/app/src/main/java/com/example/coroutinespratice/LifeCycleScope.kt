package com.example.coroutinespratice

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable


@Serializable
object SCREENB


@Serializable
object SCREENA


@Composable
fun ScreenB(){
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(35.dp))
        Text("Screen B")
    }
}


@Composable
fun ScreenA(navController: NavController){
  val coroutinescope = rememberCoroutineScope()
    Column(modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(35.dp))
        Text("Screen a")
        Spacer(modifier = Modifier.height(35.dp))
        Button(onClick = {
              //using global bad we use coroutine scope here
               coroutinescope.launch(Dispatchers.Main) {
                    while (true){
                        delay(5000)
                        Log.d("COROUTINE","Global screen a running task ...")//runs till app is in ram
                        //bad pratice
                    }
                }
                coroutinescope.launch(Dispatchers.Main)  {
                    delay(5000)
                    navController.navigate(SCREENB)
                }



        }) {
            Text("naviagte")
        }

    }
}

@Composable
fun MainAppImplForLifeCycle() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = SCREENA ){
        composable<SCREENA>{
            ScreenA(navController)

        }
        composable<SCREENB> {
            ScreenB()
        }
    }
}