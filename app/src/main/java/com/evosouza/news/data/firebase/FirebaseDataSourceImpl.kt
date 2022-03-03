package com.evosouza.news.data.firebase

import com.evosouza.news.data.model.SubjectsModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.lang.IllegalStateException
import kotlin.coroutines.suspendCoroutine

class FirebaseDataSourceImpl: FirebaseDataSource {

    private var dataBase: DatabaseReference = Firebase.database.reference

    override suspend fun getValues(): SubjectsModel = suspendCoroutine { continuation ->
        dataBase.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val subjects = snapshot.getValue(SubjectsModel::class.java)
                subjects?.let {
                    continuation.resumeWith( Result.success(it) )
                }
            }

            override fun onCancelled(error: DatabaseError) {
                continuation.resumeWith(Result.failure(IllegalStateException(error.message)))
            }
        })
    }
}