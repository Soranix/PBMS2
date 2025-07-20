package com.example.pbms.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonDefaults.buttonColors
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pbms.nav.Screen
import com.example.pbms.viewmodel.EditViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    bookId: Int,
    navController: NavController,
    viewModel: EditViewModel = viewModel()
) {

    val book by viewModel.book.collectAsState()

    // Load the book once
    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    // Show a loading placeholder
    if (book == null) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        return
    }

    // Editable fields.
    // !! will throw NullPointerException
    var title by remember { mutableStateOf(book!!.title) }
    var author by remember { mutableStateOf(book!!.author) }
    var genre by remember { mutableStateOf(book!!.genre ?: "") }
    var totalPages by remember { mutableStateOf(book!!.totalPages.toString()) }
    var currentProgress by remember { mutableStateOf(book!!.currentProgress.toString()) }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text("Edit Book", fontWeight = FontWeight.Bold)
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = author,
                    onValueChange = { author = it },
                    label = { Text("Author") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = genre,
                    onValueChange = { genre = it },
                    label = { Text("Genre (optional)") },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = totalPages,
                    onValueChange = { totalPages = it.filter { c -> c.isDigit() } },
                    label = { Text("Total Pages") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = currentProgress,
                    onValueChange = { currentProgress = it.filter { c -> c.isDigit() } },
                    label = { Text("Current Progress") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            viewModel.deleteBook(book!!) {
                                navController.popBackStack()
                            }
                        },
                        colors = buttonColors(containerColor = Color.Red, contentColor = Color.White)
                    ) {
                        Text(text = "Delete")
                    }

                    Button(
                        onClick = {
                            val updatedBook = book!!.copy(
                                title = title.trim(),
                                author = author.trim(),
                                genre = genre.trim().ifEmpty { null },
                                totalPages = totalPages.toIntOrNull() ?: 0,
                                currentProgress = currentProgress.toIntOrNull() ?: 0
                            )
                            viewModel.updateBook(updatedBook) {
                                navController.popBackStack()
                            }
                        },
                        colors = buttonColors(containerColor = Color.Black, contentColor = Color.White)
                    ) {
                        Text("Save")
                    }
                }
            }
        }
    )
}
