package com.tushe.wallapoor.network.managers.userFirestore

import com.tushe.wallapoor.common.ExceptionClosure
import com.tushe.wallapoor.network.models.User

interface UserAuthorizationManager {
    fun register(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?)
    fun login(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?)
    fun recoverPassword(user: User, onSuccess: (User) -> Unit, onError: ExceptionClosure?)
    fun isLogged(onSuccess: (User?) -> Unit)
    fun logout(onSuccess: () -> Unit)
}