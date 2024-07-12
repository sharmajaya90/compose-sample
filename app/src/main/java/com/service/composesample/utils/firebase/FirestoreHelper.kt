package com.service.composesample.utils.firebase

import com.google.firebase.firestore.FirebaseFirestore
import com.service.composesample.model.UserInfo
import kotlinx.coroutines.tasks.await

object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    var collectionUrlPath:String = "user_info"

    fun addUserInfo(userInfo: UserInfo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionUrlPath)
            .add(userInfo)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    suspend fun getUserInfoList(): List<UserInfo> {
        return try {
            val result = db.collection(collectionUrlPath)
                .get()
                .await()
            result.documents.mapNotNull { data->
                val qaInfoData = data.toObject(UserInfo::class.java)
                qaInfoData?.key = data.id
                qaInfoData
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    fun updateUserInfo(userInfo: UserInfo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionUrlPath).document(userInfo.key?:"")
            .update(mapOf("id" to "${userInfo.key}","key" to "","name" to "${userInfo.name}","email" to "${userInfo.email}","address" to "${userInfo.address}","image" to "${userInfo.image}"))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteUserInfo(userId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionUrlPath).document(userId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }
}