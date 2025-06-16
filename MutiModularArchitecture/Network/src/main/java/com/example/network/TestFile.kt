package com.example.network

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun TestFile(){
    Text("hello from module 2")
}
//In multimodule this hides the fn in that module it self
@Composable
internal fun TestFunction(){
    Text("Internal function")
}