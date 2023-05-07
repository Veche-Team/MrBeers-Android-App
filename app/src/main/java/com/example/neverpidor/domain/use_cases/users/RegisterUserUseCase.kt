package com.example.neverpidor.domain.use_cases.users

import android.content.Context
import com.example.neverpidor.R
import com.example.neverpidor.data.database.entities.UserEntity
import com.example.neverpidor.domain.repositories.UserRepository
import com.example.neverpidor.presentation.fragments.register.util.RegisterInputFields
import com.example.neverpidor.util.NumberAlreadyExistsException
import com.example.neverpidor.util.PasswordException
import com.example.neverpidor.util.security.SecurityUtils

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