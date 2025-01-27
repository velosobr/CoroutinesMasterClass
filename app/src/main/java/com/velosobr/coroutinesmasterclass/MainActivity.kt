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
import androidx.lifecycle.lifecycleScope
import com.velosobr.coroutinesmasterclass.ui.theme.CoroutinesMasterClassTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.system.measureTimeMillis

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        birdSounds()

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

    private suspend fun mainDispatcher() {
        withContext(Dispatchers.Main) {
            // do something on the main thread as soon as possible after the current task

            withContext(Dispatchers.Main.immediate) {
                // do something on the main thread immediately, without waiting for the current task
            }
        }
    }

    private fun unconfinedDispatcher() {
        GlobalScope.launch {
            withContext(Dispatchers.Main) {
                Log.d("Thread", "Thread: ${Thread.currentThread().name}")
                withContext(Dispatchers.Unconfined) {
                    Log.d("Thread", "Thread: ${Thread.currentThread().name}")
                    withContext(Dispatchers.IO) {
                        Log.d("Thread", "Thread: ${Thread.currentThread().name}")
                    }
                    Log.d("Thread", "Thread: ${Thread.currentThread().name}")
                }
            }
        }

    }

    /**
     * [Easy] Assignment #1
     *
     * Each morning, you wake up to the sound of birds. Over time, you’ve noticed three unique bird
     * sounds, each repeating at a different pace. One bird makes a sound every second, the other
     * every 2 seconds, and the last every 3 seconds.
     *
     * Instructions
     * Recreate the timing of each bird’s sounds using a single coroutine for each bird. Each
     * coroutine should only print four times before completing.
     *
     * The first bird makes a “Coo” sound.
     * The second bird makes a “Caw” sound.
     * The last bird makes a “Chirp” sound.
     *
     * Hint: Assignment #1
     *
     * Ensure you launch new coroutines inside GlobalScope and call join() to keep the program
     * alive until all the coroutines have been completed.
     */

    private fun birdSounds() {
        lifecycleScope.launch(Dispatchers.IO) {
            launch {
                repeat(4) {
                    Log.d(
                        "Bird Sounds", "Coo-${it + 1} \n" +
                                " Thread: ${Thread.currentThread().name}"
                    )
                    delay(1000)
                }
            }
            launch {
                repeat(4) {
                    Log.d("Bird Sounds", "Caw-${it + 1} \n Thread: ${Thread.currentThread().name}")
                    delay(2000)
                }
            }
            launch {
                repeat(4) {
                    Log.d(
                        "Bird Sounds", "Chirp-${it + 1} \n" +
                                " Thread: ${Thread.currentThread().name}"
                    )
                    delay(3000)
                }
            }

        }
    }
}
