package com.example.pbms.view

import android.app.DatePickerDialog
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pbms.viewmodel.AddViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    navController: NavController,
    viewModel: AddViewModel = viewModel()
){
    var title by remember { mutableStateOf("") }
    var author by remember { mutableStateOf("") }
    var genre by remember { mutableStateOf("") }
    var totalPages by remember { mutableStateOf("") }
    var currentProgress by remember { mutableStateOf("") }

    // this is everything I need for selecting a date via a calendar
    val context = LocalContext.current
    val calendar = remember { Calendar.getInstance() }

    var selectedDate by remember { mutableStateOf("") } // Show to user
    var dateTimestamp by remember { mutableLongStateOf(System.currentTimeMillis()) } // Save to DB

    val datePickerDialog = DatePickerDialog(
        context,
        { _, year, month, dayOfMonth ->
            calendar.set(year, month, dayOfMonth)
            dateTimestamp = calendar.timeInMillis
            val format = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
            selectedDate = format.format(calendar.time)
        },
        calendar.get(Calendar.YEAR),
        calendar.get(Calendar.MONTH),
        calendar.get(Calendar.DAY_OF_MONTH)
    )

    // for genre drop down menu. It was buggy so I removed it
    /*var expanded by remember { mutableStateOf(false) }
    var selectedGenre by remember { mutableStateOf("") }
    val genreOptions = listOf(
        "Fiction", "Non-fiction", "Fantasy", "Biography", "Science", "History", "Comedy", "Action", "Romance"
    )

     */

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Add Book", fontWeight = FontWeight.Bold) },
                // back button
                navigationIcon = {
                    IconButton(onClick = {navController.popBackStack() }) {
                        Icon(imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back"
                        )
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

                Box(
                    modifier = Modifier
                        .fillMaxWidth()

                ) {
                    OutlinedTextField(
                        value = genre,
                        onValueChange = { genre = it},
                        label = { Text("Genre") },
                        modifier = Modifier
                            .fillMaxWidth()
                    )
                }

                OutlinedTextField(
                    value = totalPages,
                    // that filter will keep only the numbers within the string
                    onValueChange = { totalPages = it.filter { c -> c.isDigit() } },
                    label = { Text("Total Pages") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = currentProgress,
                    onValueChange = { currentProgress = it.filter { c -> c.isDigit() } },
                    label = { Text("Current Progress") },
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth()
                )
                Row(modifier = Modifier.fillMaxWidth().padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Button(
                        onClick = { datePickerDialog.show() },
                        modifier = Modifier,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = if (selectedDate.isEmpty()) "Pick Date" else selectedDate)
                    }

                }
                    // this button will not appear unless title and author are not blank.
                    Button(
                        onClick =
                        {
                            viewModel.addBook(
                                title = title,
                                author = author,
                                genre = genre,
                                totalPages = totalPages,
                                currentProgress = currentProgress,
                                dateAdded = dateTimestamp,

                            ) {
                                navController.popBackStack() // onSuccess
                            }
                        },
                        modifier = Modifier,
                        enabled = title.isNotBlank() && author.isNotBlank(),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Black,
                            contentColor = Color.White
                        )

                    ) {
                        Text("Save")
                    }
                }
            }
    )
}
