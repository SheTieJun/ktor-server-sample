package com.example.model

/**
 * User model
 * id，名称，年龄，性别，头像，地址，电话，邮箱，密码，创建时间，更新时间
 * User model attributes: id, name, age, gender, avatar, address, phone, email, password, creation and update times.
 */
data class UserModel(
    val id: Int,
    val name: String,
    val age: Int,
    val gender: String,
    val avatar: String,
    val address: String,
    val phone: String,
    val email: String,
    val password: String,
    val creationTime: Long,
    val updateTime: Long
) {

    companion object {

    }

}
