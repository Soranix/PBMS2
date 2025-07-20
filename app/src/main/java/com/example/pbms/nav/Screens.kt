package com.example.pbms.nav

// navigation routes
sealed class Screen(val route: String){
    object HomeScreen: Screen("home_screen")
    object EditScreen: Screen("edit_screen/{bookId}"){
        fun withId(bookId:Int) = "edit_screen/$bookId"
    }
    object AddScreen: Screen("add_screen")
}
