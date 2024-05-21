package com.example.retrofit

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.retrofit.models.data.Post
import com.example.retrofit.models.network.PostRepository
import com.example.retrofit.ui.theme.RetrofitTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {

    private val postRepository = PostRepository()

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            //get all
            var posts by remember { mutableStateOf(listOf<Post>()) }

            //get by Id
            var id  by remember { mutableStateOf("") }
            var post by remember { mutableStateOf(Post()) }

            //post form
            var idForm by remember { mutableStateOf("") }
            var userId by remember { mutableStateOf("") }
            var title by remember { mutableStateOf("") }
            var body by remember { mutableStateOf("") }

            var idRemove by remember { mutableStateOf("") }

            val scope = rememberCoroutineScope()
            scope.launch {
                postRepository.getAllPosts()
                    .collect { data: List<Post> -> posts = data }
            }
            RetrofitTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column {

                        Row {
                            TextField(value = idRemove, onValueChange = { idRemove = it }, modifier = Modifier.padding(8.dp))
                            Button(onClick = {
                                scope.launch {
                                    postRepository.deletePost(idRemove).first()
                                }
                            }) {
                                Text("Remove Post by ID")
                            }
                        }
                        Row {
                            TextField(value = id, onValueChange = { id = it }, modifier = Modifier.padding(8.dp))
                            Button(onClick = {
                                scope.launch {
                                    post = postRepository.getPostById(id).first()
                                }
                            }) {
                                Text("Get Post by ID")
                            }
                        }

                        //BUG
                        PostForm(
                            idForm = idForm,
                            userId = userId,
                            title = title,
                            body = body,
                            onIdFormChanged = { idForm = it },
                            onUserIdChanged = { userId = it },
                            onTitleChanged = { title = it },
                            onBodyChanged = { body = it }
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = {
                            val post = Post(
                                id = idForm,
                                userId = userId,
                                title = title,
                                body = body
                            )
                            scope.launch {
                                postRepository.createPost(post)
                            }
                        }) {
                            Text("Post Data")
                        }
                        LazyColumn(modifier = Modifier.padding(innerPadding)) {
                            items( listOf(post) ) {
                                Card(modifier = Modifier.padding(8.dp)) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "ID: ${it.id}", modifier = Modifier.padding(8.dp))
                                        Text(text = "User ID: ${it.userId}", modifier = Modifier.padding(8.dp))
                                        Text(text = "Title: ${it.title}", modifier = Modifier.padding(8.dp))
                                        Text(text = "Body: ${it.body}", modifier = Modifier.padding(8.dp))
                                    }
                                }
                            }
                            items(posts) { post ->
                                Card(modifier = Modifier.padding(8.dp)) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Text(text = "ID: ${post.id}", modifier = Modifier.padding(8.dp))
                                        Text(text = "User ID: ${post.userId}", modifier = Modifier.padding(8.dp))
                                        Text(text = "Title: ${post.title}", modifier = Modifier.padding(8.dp))
                                        Text(text = "Body: ${post.body}", modifier = Modifier.padding(8.dp))
                                    }
                                }

                            }
                            //remove byID

                        }
                    }
                }
            }
        }
    }
}
@Composable
fun PostForm(
    idForm: String,
    userId: String,
    title: String,
    body: String,
    onIdFormChanged: (String) -> Unit,
    onUserIdChanged: (String) -> Unit,
    onTitleChanged: (String) -> Unit,
    onBodyChanged: (String) -> Unit
) {
    TextField(
        value = idForm,
        onValueChange = onIdFormChanged,
        label = { Text("ID") },
        modifier = Modifier.fillMaxWidth()
    )
    TextField(
        value = userId,
        onValueChange = onUserIdChanged,
        label = { Text("userId") },
        modifier = Modifier.fillMaxWidth()
    )
    TextField(
        value = title,
        onValueChange = onTitleChanged,
        label = { Text("Title") },
        modifier = Modifier.fillMaxWidth()
    )
TextField(
        value = body,
        onValueChange = onBodyChanged,
        label = { Text("Body") },
        modifier = Modifier.fillMaxWidth()
    )}
