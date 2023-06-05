package com.example.beers_app.domain.use_cases.users

import android.content.Context
import com.example.beers.R
import com.example.beers_app.data.database.entities.UserEntity
import com.example.beers_app.domain.repositories.UserRepository
import com.example.beers_app.presentation.fragments.register.util.RegisterInputFields
import com.example.beers_app.util.NumberAlreadyExistsException
import com.example.beers_app.util.PasswordException
import com.example.beers_app.util.security.SecurityUtils

class RegisterUserUseCase(
    private val repository: UserRepository,
    private val securityUtils: SecurityUtils,
    private val context: Context
) {
    suspend operator fun invoke(
        registerInputFields: RegisterInputFields
    ) {
        if (!registerInputFields.password.contentEquals(registerInputFields.repeatPassword)) {
            throw PasswordException.RepeatPasswordException(context.getString(R.string.passwords_should_match_exception))
        }
        registerInputFields.repeatPassword.fill('*')
        val userEntity = repository.findUserByNumber(registerInputFields.number)
        userEntity?.let {
            throw NumberAlreadyExistsException(context.getString(R.string.user_already_exists_exception))
        }
        val salt = securityUtils.generateSalt()
        val hash = securityUtils.passwordToHash(registerInputFields.password, salt)
        registerInputFields.password.fill('*')
        val user = UserEntity(
            phoneNumber = registerInputFields.number,
            name = registerInputFields.name,
            hash = securityUtils.bytesToString(hash),
            salt = securityUtils.bytesToString(salt)
        )
        repository.addUser(user)
    }
}