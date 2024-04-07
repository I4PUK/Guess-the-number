package com.example.guessthenumber

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.system.exitProcess

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun GameScreen(viewModel: GameViewModel = viewModel()) {
    val gameUiState = viewModel.uiState.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.app_name),
                        textAlign = TextAlign.Center
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            )
        },
        content = {
            Column(modifier = Modifier.padding(it)){
                GameContent(
                    userGuess = viewModel.userGuess,
                    gameState = gameUiState.value,
                    submitButtonOnClick = { viewModel.checkValue() },
                    onGameReset = { viewModel.resetGame() },
                    updateUserGuess = { viewModel.updateUserGuess(it) },
                    modifier = Modifier.padding(it)
                )
            }
        },
    )

}


@Composable
fun GameContent(
    userGuess: String,
    gameState: GameState?,
    updateUserGuess: (String) -> Unit,
    submitButtonOnClick: () -> Unit,
    onGameReset: () -> Unit,
    modifier: Modifier = Modifier
) {
    gameState.let { state ->
        when (state) {
            is GameState.WinState -> Dialog(
                onDismissRequest = onGameReset,
                properties = DialogProperties(),
                content = {
                    CustomDialog(
                        modifier = modifier,
                        onDismiss = { exitProcess(-1) },
                        onConfirm = onGameReset
                    )
                },
            )

            is GameState.AttemptState -> ScreenContent(
                textFieldText = userGuess,
                state = state,
                submitButtonOnClick = submitButtonOnClick,
                onTextFieldValueChange = updateUserGuess
            )

            null -> Text("Null state", modifier = modifier)
        }
    }
}

@Composable
fun ScreenContent(
    textFieldText: String,
    submitButtonOnClick: () -> Unit,
    onTextFieldValueChange: (String) -> Unit,
    state: GameState.AttemptState,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = stringResource(id = R.string.task_description),
            textAlign = TextAlign.Center,
            modifier = modifier
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = textFieldText,
            onValueChange = onTextFieldValueChange,
            placeholder = { Text(stringResource(id = R.string.placeholder)) },
            maxLines = 1,
            keyboardOptions = KeyboardOptions(
                autoCorrect = false,
                keyboardType = KeyboardType.NumberPassword
            ),
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(16.dp))
        ElevatedButton(
            modifier = Modifier.fillMaxWidth(),
            onClick = submitButtonOnClick,
            content = { Text(text = stringResource(R.string.submit)) },
        )
        Spacer(modifier = Modifier.height(16.dp))
        ListItem(
            headlineContent = { },
            leadingContent = { Text(text = stringResource(R.string.hint)) },
            trailingContent = { Text(text = state.hintText) },
        )
        ListItem(
            headlineContent = { },
            leadingContent = { Text(text = stringResource(id = R.string.attempts)) },
            trailingContent = { Text(text = "${state.attempts}") }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameScreenPreview() {
    GameScreen()
}
