package com.example.pbms.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.pbms.model.data.Book
import com.example.pbms.nav.Screen
import com.example.pbms.viewmodel.HomeViewModel



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = viewModel()
) {
    val books by viewModel.books.collectAsState()
    viewModel.loadBooks()

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "My Book", fontWeight = FontWeight.Bold) },
            )
        },
        // '+' button. Navigates to the Add screen
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate(Screen.AddScreen.route)},
                containerColor = Color.Black,
                contentColor = Color.White
                ) {
                Text("+")
            }
        }
    ) { padding ->
        if (books.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Text("No books yet. Tap + to add one.")
            }
        } else {
            LazyColumn(
                contentPadding = padding,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                items(books) { book ->
                    BookItem(
                        book = book,
                        onClick = { navController.navigate(Screen.EditScreen.withId(book.id)) }
                        )
                }
            }
        }
    }
}
@Composable
fun BookItem(book: Book, onClick: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth().clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = book.title, style = MaterialTheme.typography.titleMedium)
            Text(text = "Author: ${book.author}", style = MaterialTheme.typography.bodyMedium)
            book.genre?.let {
                Text(text = "Genre: $it", style = MaterialTheme.typography.bodySmall)
            }
            Text(
                text = "Added: ${book.formatDate(book.dateAdded)}",
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = "Progress: ${book.percentage}%",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

