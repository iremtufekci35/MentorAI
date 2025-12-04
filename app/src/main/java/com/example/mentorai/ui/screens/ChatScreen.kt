package com.example.mentorai.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import com.google.firebase.Firebase
import com.google.firebase.ai.ai
import com.google.firebase.ai.type.GenerativeBackend

data class ChatMessage(val text: String, val isUser: Boolean)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(category: String) {
    var userInput by remember { mutableStateOf("") }
    val messages = remember { mutableStateListOf<ChatMessage>() }
    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val keyboardController = LocalSoftwareKeyboardController.current

    Column(modifier = Modifier.fillMaxSize()) {

        TopAppBar(
            title = { Text("$category Chat") },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.surface,
                titleContentColor = MaterialTheme.colorScheme.onSurface
            )
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            reverseLayout = true,
            state = listState
        ) {
            items(messages.reversed()) { message ->
                AnimatedVisibility(
                    visible = true,
                    enter = fadeIn()
                ) {
                    ChatBubble(message)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = userInput,
                onValueChange = { userInput = it },
                modifier = Modifier.weight(1f),
                placeholder = { Text("Mesaj yaz...") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Send
                ),
                keyboardActions = KeyboardActions(
                    onSend = {
                        if (userInput.isNotBlank()) {
                            sendMessage(
                                userInput,
                                messages,
                                category,
                                scope,
                                listState
                            )
                            userInput = ""
                            keyboardController?.hide()
                        }
                    }
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            Button(
                onClick = {
                    if (userInput.isNotBlank()) {
                        sendMessage(userInput, messages, category, scope, listState)
                        userInput = ""
                        keyboardController?.hide()
                    }
                },
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
            ) {
                Icon(Icons.Default.Send, contentDescription = "Gönder", tint = Color.White)
            }
        }
    }
}

private fun sendMessage(
    text: String,
    messages: MutableList<ChatMessage>,
    category: String,
    scope: CoroutineScope,
    listState: androidx.compose.foundation.lazy.LazyListState
) {
    messages.add(ChatMessage(text, true))
    val inputCopy = text
    scope.launch {
        val aiReply = getAiResponse(category, inputCopy)
        messages.add(ChatMessage(aiReply, false))
        listState.animateScrollToItem(0)
    }
}

@Composable
fun ChatBubble(message: ChatMessage) {
    val bubbleColor = if (message.isUser)
        MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)
    else
        MaterialTheme.colorScheme.secondary.copy(alpha = 0.3f)

    val alignment = if (message.isUser) Alignment.End else Alignment.Start
    val shape = if (message.isUser)
        RoundedCornerShape(16.dp, 0.dp, 16.dp, 16.dp)
    else
        RoundedCornerShape(0.dp, 16.dp, 16.dp, 16.dp)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(bubbleColor, shape)
                .padding(12.dp)
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = message.text,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

suspend fun getAiResponse(category: String, message: String): String {
    val model = Firebase.ai(backend = GenerativeBackend.googleAI())
        .generativeModel("gemini-2.5-flash")

    val prompt =
        "Kategorisi '$category' olan bir kullanıcı sorusuna cevap ver: \"$message\". Sadece metin ile yanıt ver."

    val response = model.generateContent(prompt)
    return response.text ?: "AI yanıtı alınamadı."
}
