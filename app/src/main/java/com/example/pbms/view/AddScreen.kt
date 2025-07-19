package com.example.pbms.view

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
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
import com.example.pbms.viewmodel.AddViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.math.exp


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

                // Genre dropdown
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

                Button(
                    onClick = {
                        viewModel.addBook(
                            title = title,
                            author = author,
                            genre = genre,
                            totalPages = totalPages,
                            currentProgress = currentProgress
                        ) {
                            navController.popBackStack()
                        }
                    },
                    modifier = Modifier.align(Alignment.End),
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