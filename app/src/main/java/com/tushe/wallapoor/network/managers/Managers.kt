package com.tushe.wallapoor.network.managers

import com.tushe.wallapoor.network.managers.productFirestore.ProductFirestore
import com.tushe.wallapoor.network.managers.userFirestore.UserAuthoritation
import com.tushe.wallapoor.network.managers.userFirestore.UserFirestore

class Managers {
    companion object {
        // Manager de autentificacion de usuario
        var managerUserAuthoritation: UserAuthoritation? = null
        // Manager de tabla de Usuarios en Firestore
        var managerUserFirestore: UserFirestore? = null
        // Manager de tabla de Productos en Firestore
        var managerProductFirestore: ProductFirestore? = null
    }
}