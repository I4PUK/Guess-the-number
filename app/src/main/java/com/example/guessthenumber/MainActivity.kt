package com.example.guessthenumber

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.guessthenumber.ui.theme.GuessTheNumberTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            GuessTheNumberTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ScaffoldApp()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScaffoldApp(){
    val randomValue = (0..100).random()
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Guess the number", textAlign = TextAlign.Center) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
                )
        },
        content = {
            ScaffoldContent(randomValue, Modifier.padding(it))
        },
    )
}

@Composable
fun CustomDialogUI(modifier: Modifier, onConfirm: () -> Unit, onDismiss: () -> Unit) = Column(
        modifier
            .background(Color.White)) {

        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Congratulations! You've won!\n",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 5.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.labelLarge,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "Do you want to try again?",
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Row(
            Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            horizontalArrangement = Arrangement.SpaceAround) {

            Button(onClick = onDismiss, modifier = Modifier
            ) {
                Text(
                    "Not Now",
                    color = Color.White,
                )
            }

            Button(onClick = onConfirm) {
                    Text(
                        "Yes, let's go",
                        color = Color.White,
                    )
            }
        }
}

@Composable
fun ScaffoldContent(randomValue: Int, modifier: Modifier = Modifier) {
    var localRandomValue by remember {
        mutableIntStateOf(randomValue)
    }

    var textFieldText by remember{ mutableStateOf("") }
    var attempts by remember {
        mutableIntStateOf(0)
    }
    var hintText by remember {
        mutableStateOf("")
    }

    var isOpenDialog by remember { mutableStateOf(false) }

    val onClickFunc = {
        val parsedValue = textFieldText.toIntOrNull()
        val valuesDifference = parsedValue?.minus(localRandomValue)

        attempts++

        print(valuesDifference.toString())
        if (valuesDifference != null) {
            if (valuesDifference > 0){
                hintText = "Your number is more"
            }
            if (valuesDifference == 0){
                isOpenDialog = true
                hintText = ""
            }
            if (valuesDifference < 0){
                hintText = "Your number is less"
            }
        } else {
            hintText = "Your number is incorrect"
        }
    }

    if (isOpenDialog) {
        Dialog(
            onDismissRequest = { isOpenDialog = false },
            properties = DialogProperties(),
            content = {
                CustomDialogUI(
                    modifier = modifier,
                    onDismiss = { isOpenDialog = false },
                    onConfirm = {
                        isOpenDialog = false
                        attempts = 0
                        localRandomValue = (0..100).random()
                    }
                ) },
            )
    }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "I'm thinking of a number between 1 and 100.\nCan you guess it?",
            textAlign = TextAlign.Center,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = textFieldText,
            onValueChange = {newValue: String -> textFieldText = newValue},
            placeholder = {
                Text("Enter your guess")
            },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(autoCorrect = false, keyboardType = KeyboardType.NumberPassword),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = onClickFunc,
            content = { Text(text = "Submit")},
        )
        Spacer(modifier = Modifier.height(16.dp))
        androidx.compose.material3.ListItem(
            headlineContent = { /*TODO*/ },
            leadingContent = {
                Text(text = "Hint")
            },
            trailingContent = {
                Text(
                    text = hintText
                )
            },
        )
        androidx.compose.material3.ListItem(
            headlineContent = { /*TODO*/ },
            leadingContent = {
                Text(text = "Attempts")
            },
            trailingContent = {
                Text(text = attempts.toString())
            }
        )
        androidx.compose.material3.ListItem(
            headlineContent = { /*TODO*/ },
            leadingContent = {
                Text(text = "Guessed number: ")
            },
            trailingContent = {
                Text(text = localRandomValue.toString())
            }
        )
    }
}