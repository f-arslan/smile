package com.smile.model

import com.google.firebase.firestore.DocumentId


data class Contact(
    @DocumentId
    val contactId: String = "",
    val userId: String = "",
    val friendId: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val roomId: String = "",
)