package com.example.pbms.view

import android.app.DatePickerDialog
import androidx.compose.foundation.gestures.snapping.SnapPosition
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.pbms.nav.Screen
import com.example.pbms.viewmodel.EditViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditScreen(
    bookId: Int,
    navController: NavController,
    viewModel: EditViewModel = viewModel()
) {
    // Load the book once
    LaunchedEffect(bookId) {
        viewModel.loadBook(bookId)
    }

    val book by viewModel.book.collectAsState()
    // Show a loading placeholder otherwise the app would crash. viewModel needs to load and update the stateflow
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

    // edit date variables
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    val format = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }

    var selectedDate by remember { mutableStateOf("") }
    var dateTimestamp by remember { mutableLongStateOf(System.currentTimeMillis()) }

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            dateTimestamp = calendar.timeInMillis
            selectedDate = format.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )


    /*LaunchedEffect(book){
        book?.let{
            calendar.timeInMillis = it.dateAdded
            selectedDate = format.format(calendar.time)
            dateTimestamp = it.dateAdded
        }
    }
     */


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

                Spacer(modifier = Modifier.height(16.dp))

                Button(onClick = { datePickerDialog.show() },
                    modifier = Modifier.fillMaxWidth(),
                    colors = buttonColors(containerColor = Color.Black, contentColor = Color.White)) {
                    Text(text = if (selectedDate.isNotEmpty()) selectedDate else "Select Date")
                }

                Spacer(modifier = Modifier.height(16.dp))

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
                                currentProgress = currentProgress.toIntOrNull() ?: 0,
                                dateAdded = dateTimestamp
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
