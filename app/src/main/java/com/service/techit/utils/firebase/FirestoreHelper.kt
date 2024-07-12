package com.service.techit.utils.firebase

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import com.service.techit.model.QAInfo
import com.service.techit.model.QAInfoTechnology
import com.service.techit.model.TechITStyles
import kotlinx.coroutines.tasks.await

object FirestoreHelper {
    private val db = FirebaseFirestore.getInstance()
    var collectionUrlPath:String = ""
    private var technologyUrlPath:String = "techit_technology"

    fun addQAInfoTechnology(qaInfoTechnology: QAInfoTechnology, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(technologyUrlPath)
            .add(qaInfoTechnology)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun addQAInfo(qa: QAInfo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionUrlPath)
            .add(qa)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    suspend fun getQAInfoList(): List<QAInfo> {
        return try {
            val result = db.collection(collectionUrlPath)
                .get()
                .await()
            result.documents.mapNotNull { data->
                val qaInfoData = data.toObject(QAInfo::class.java)
                qaInfoData?.key = data.id
                qaInfoData
            }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun getQAInfoTechnologies(): List<QAInfoTechnology> {
        return try {
            val result = db.collection(technologyUrlPath)
                .get()
                .await()
            result.documents.mapNotNull { it.toObject(QAInfoTechnology::class.java) }
        } catch (e: Exception) {
            emptyList()
        }
    }
    suspend fun provideTechITStyles(): TechITStyles? {
        return try {
            val result = db.collection("techit_styles")
                .get()
                .await()
            result.documents.get(0).toObject(TechITStyles::class.java)
        } catch (e: Exception) {
            TechITStyles()
        }
    }
    fun updateQAInfo(qaInfo: QAInfo, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionUrlPath).document(qaInfo.key?:"")
            .update(mapOf("id" to "${qaInfo.id}","key" to "","title" to "${qaInfo.title}","type" to "${qaInfo.type}","detailed" to "${qaInfo.detailed}"))
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    fun deleteQAInfo(qaId: String, onSuccess: () -> Unit, onFailure: (Exception) -> Unit) {
        db.collection(collectionUrlPath).document(qaId)
            .delete()
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onFailure(it) }
    }

    suspend fun getQAInfoById(qaId: String): QAInfo? {
        val result = db.collection(collectionUrlPath).document(qaId).get().await()
       return result.toObject(QAInfo::class.java)
    }
}