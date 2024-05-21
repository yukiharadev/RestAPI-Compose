package com.example.retrofit.models.network

import com.example.retrofit.models.data.Post
import com.example.retrofit.ui.theme.RetrofitClient
import com.example.retrofit.ui.theme.RetrofitClient.retrofit
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import retrofit2.Call

class PostRepository {
fun getPostById(postId: String): Flow<Post> = flow {
    var id = postId
    while(true){
        val data = retrofit.getPost(id)
        emit(data)
        delay(2000L)
    }
}

    fun getAllPosts(): Flow<List<Post>> = flow {
        val r = RetrofitClient.retrofit.getAllPosts()
        emit(r)
    }.flowOn(Dispatchers.IO)


    fun createPost(post: Post): Flow<Call<Post>> = flow {
        val response = retrofit.createPost(post)
        emit(response)
    }.flowOn(Dispatchers.IO)

    suspend fun deletePost(id: String): Flow<Boolean> = flow {
        val response = retrofit.deletePost(id)
        emit(response.isSuccessful)
    }.flowOn(Dispatchers.IO)
}
