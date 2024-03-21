package com.bignerdranch.android.kalin_toussaint_onetouch

import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlin.random.Random



class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val context = LocalContext.current
            val (imageIndex, setImageIndex) = remember { mutableIntStateOf(0) }
            val (imageText, setImageText) = remember { mutableStateOf("") }

            // Retrieve stored values from SharedPreferences
            val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
            val storedImageIndex = sharedPreferences.getInt("imageIndex", 0)
            val storedImageText = sharedPreferences.getString("imageText", "") ?: ""

            // Update state with stored values
            setImageIndex(storedImageIndex)
            setImageText(storedImageText)

            ImageWithButton(
                currentImageIndex = mutableStateOf(imageIndex),
                imageText = mutableStateOf(imageText),
                context = context
            )

            // Show toast when stored values are retrieved
            LaunchedEffect(Unit) {
                showToast(context, "ImageIndex: $storedImageIndex, ImageText: $storedImageText retrieved from SharedPreferences")
            }
        }
    }
}

@Composable
fun ImageWithButton(
    currentImageIndex: MutableState<Int>,
    imageText: MutableState<String>,
    context: Context
) {
    val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4,
        R.drawable.image5,
        R.drawable.image6,
        R.drawable.image7,
        R.drawable.image8
    )

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(id = images[currentImageIndex.value]),
            contentDescription = null,
            modifier = Modifier
                .size(200.dp)
                .padding(16.dp)
        )

        Button(onClick = {
            val newIndex = Random.nextInt(images.size)
            currentImageIndex.value = newIndex
            saveState(context, currentImageIndex.value, imageText.value) // Save state when button is pressed
            showToast(context, "Image and text saved!") // Show Toast message
        }) {
            Text("Change Image")
        }
        Spacer(modifier = Modifier.height(16.dp))

        TextField(
            value = imageText.value,
            onValueChange = { newImageText ->
                imageText.value = newImageText
            },
            label = { Text("Enter a rating for the image above on a scale of 1-10") },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Function to save state to SharedPreferences
private fun saveState(context: Context, imageIndex: Int, imageText: String) {
    val sharedPreferences = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
    sharedPreferences.edit().apply {
        putInt("imageIndex", imageIndex)
        putString("imageText", imageText)
        apply()
    }
}

// Function to display Toast message
private fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val imageIndex = mutableStateOf(0)
    val imageText = mutableStateOf("")
    val context = LocalContext.current
    ImageWithButton(imageIndex, imageText, context)
}