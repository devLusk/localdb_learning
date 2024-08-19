package com.example.roomexample.feature

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.room.Room
import com.example.roomexample.resource.User
import com.example.roomexample.resource.UserDatabase
import com.example.roomexample.ui.theme.LightGrey
import com.example.roomexample.ui.theme.RoomExampleTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.prefs.Preferences

@Composable
fun MainHeaderSection(modifier: Modifier = Modifier) {
    Text(
        text = "Adding Data",
        fontSize = 25.sp,
        fontWeight = FontWeight.Bold,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun InputSection(
    userName: String,
    onUserNameChange: (String) -> Unit,
    lastName: String,
    onLastNameChange: (String) -> Unit,
    userEmail: String,
    onUserEmailChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row {
        TextField(
            value = userName,
            onValueChange = onUserNameChange,
            label = { Text(text = "Name") },
            modifier = Modifier
                .weight(0.5f)
                .padding(end = 8.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        TextField(
            value = lastName,
            onValueChange = onLastNameChange,
            label = { Text(text = "Last Name") },
            modifier = Modifier
                .weight(0.5f)
                .padding(bottom = 8.dp)
        )
    }
    TextField(
        value = userEmail,
        onValueChange = onUserEmailChange,
        label = { Text(text = "Email") },
        modifier = Modifier
            .fillMaxWidth()
    )
}

@Composable
fun ButtonsSection(
    onAddClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onFilterClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onAddClick,
        shape = RoundedCornerShape(5.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = "Add")
    }
    Row {
        Button(
            onClick = onDeleteClick,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier
                .weight(.5f)
                .padding(end = 8.dp)
        ) {
            Text(text = "Delete")
        }
        Button(
            onClick = onFilterClick,
            shape = RoundedCornerShape(5.dp),
            modifier = Modifier.weight(.5f)
        ) {
            Text(text = "Filter")
        }
    }
}

@Composable
fun ItemsSections(
    users: List<User>,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(5.dp))
            .height(300.dp)
            .background(LightGrey)
            .verticalScroll(scrollState)
    ) {
        users.forEach { user ->
            Text(
                text = "Name: ${user.firstName} | Last Name: ${user.lastName} | Email: ${user.email}",
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    val scope = rememberCoroutineScope()
    val (userName, onUserNameChange) = remember { mutableStateOf("") }
    val (lastName, onLastNameChange) = remember { mutableStateOf("") }
    val (userEmail, onUserEmailChange) = remember { mutableStateOf("") }
    val (users, setUsers) = remember { mutableStateOf(listOf<User>()) }

    val applicationContext = LocalContext.current

    val db = Room.databaseBuilder(
        applicationContext,
        UserDatabase::class.java, "database-user"
    ).build()

    val userDao = db.userDao()

    LaunchedEffect(Unit) {
        val newUsers = userDao.getAll()
        setUsers(newUsers)
    }

    Column(
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 23.dp)
    ) {
        MainHeaderSection()
        Spacer(modifier = Modifier.height(20.dp))
        InputSection(
            userName = userName,
            onUserNameChange = onUserNameChange,
            lastName = lastName,
            onLastNameChange = onLastNameChange,
            userEmail = userEmail,
            onUserEmailChange = onUserEmailChange
        )
        Spacer(modifier = Modifier.height(20.dp))
        ButtonsSection(
            onAddClick = {
                scope.launch {
                    val addUser = User(firstName = userName, lastName = lastName, email = userEmail)
                    userDao.insertUser(addUser)

                    val users = userDao.getAll()
                    setUsers(users)
                }
            },
            onDeleteClick = {
                scope.launch {

                }
            },
            onFilterClick = {
                scope.launch {

                }
            }
        )
        Spacer(modifier = Modifier.height(20.dp))
        ItemsSections(users = users)
    }
}

@Preview(showSystemUi = true)
@Composable
private fun MainScreenPreview() {
    RoomExampleTheme {
        MainScreen()
    }
}