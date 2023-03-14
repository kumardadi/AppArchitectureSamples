package com.khudama.apparchitecturetraining.archs.mvc

import java.util.Observable

class UserModel: Observable() {
    var userName: String = ""
    var userEmail: String = ""
}