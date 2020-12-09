package com.tushe.wallapoor.network.managers.userFirestore

import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.network.models.User

interface UserFirestoreManager {
    fun selectUsers(onSuccess: (List<User>) -> Unit, onError: ExceptionClosure)
    fun selectUser(userId: String, onSuccess: (User?) -> Unit, onError: ExceptionClosure)
    fun insertUser(user: User, onSuccess: () -> Unit, onError: ExceptionClosure)
    fun updateUser(user: User, onSuccess: () -> Unit, onError: ExceptionClosure)
}