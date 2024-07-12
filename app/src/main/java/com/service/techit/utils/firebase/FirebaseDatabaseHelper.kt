package com.service.techit.utils.firebase

// FirebaseDatabaseHelper.kt

object FirebaseDatabaseHelper {
    /*private val database = FirebaseDatabase.getInstance()
    private val qaInfoRef:DatabaseReference? = database.getReference("techit_qainfo_android")
    fun initFirebaseRef(path:String?="techit_qainfo") {
        database.getReference(path?:"techit_qainfo")
    }

    fun addQAInfo(qaInfo: QAInfo) {
        val key = qaInfoRef?.push()?.key ?: return
        qaInfoRef.child(key).setValue(qaInfo.copy(id = key))
    }

    fun getQAInfoList(callback: (List<QAInfo>) -> Unit) {
        qaInfoRef?.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val qaInfoList = mutableListOf<QAInfo>()
                for (qaInfoSnapshot in snapshot.children) {
                   try {

                       val qaInfo = qaInfoSnapshot.getValue(QAInfo::class.java)
                       if (qaInfo != null) {
                           qaInfoList.add(qaInfo)
                       }//QAInfo(id = qaInfoSnapshot.key,title = qaInfoSnapshot?.getValue("title")?:"", type = qaInfoSnapshot?.getValue("type")?:"", detailed = qaInfoSnapshot?.getValue("detailed"))
                   }finally {
                       Log.e("Finally Data:","${qaInfoSnapshot.toString()}")
                   }
                }
                callback(qaInfoList)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors.
                Log.e("Database Error",error.message)
            }
        })
    }

    fun getQAInfoById(qaInfoId: String, callback: (QAInfo?) -> Unit) {
        qaInfoRef?.child(qaInfoId)?.get()?.addOnSuccessListener { snapshot ->
            val qaInfoData = snapshot.getValue(QAInfo::class.java)
            callback(qaInfoData)
        }?.addOnFailureListener {
            it.printStackTrace()
            callback(null)
        }
    }*/
}