package com.velosobr.coroutinesmasterclass

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.velosobr.coroutinesmasterclass.ui.theme.CoroutinesMasterClassTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        ioDefaultDispatcher()

        setContent {
            CoroutinesMasterClassTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }

    }

    @Composable
    fun Greeting(name: String, modifier: Modifier = Modifier) {
        Text(
            text = "Hello $name!",
            modifier = modifier
        )
    }

    @Preview(showBackground = true)
    @Composable
    fun GreetingPreview() {
        CoroutinesMasterClassTheme {
            Greeting("Android")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    private fun ioDefaultDispatcher() {
        val threads = hashMapOf<Long, String>()
        val job = GlobalScope.launch(Dispatchers.Default) {
            repeat(100) {
                launch {
                    threads[Thread.currentThread().id] = Thread.currentThread().name

                    (1..100_000).map {
                        it * it
                    }
                }
            }
        }

        GlobalScope.launch {
            val timeMillis = measureTimeMillis {
                job.join()
            }
            Log.d("MainActivity", "Launched  ${threads.keys.size} threads in $timeMillis ms.")
        }
    }

    private suspend fun mainDispatcher(){
        withContext(Dispatchers.Main){
            // do something on the main thread as soon as possible after the current task

            withContext(Dispatchers.Main.immediate){
                // do something on the main thread immediately, without waiting for the current task
            }
        }
    }
}