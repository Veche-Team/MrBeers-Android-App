package com.example.neverpidor.util

sealed class PasswordException(message: String): Exception(message) {
    class OldPasswordException(message: String): PasswordException(message)
    class NewPasswordException(message: String): PasswordException(message)
    class RepeatPasswordException(message: String): PasswordException(message)
}

class UserDoesntExistException(message: String): Exception(message)
class NumberAlreadyExistsException(message: String): Exception(message)

class EmptyFieldException(message: String): Exception(message)
class InvalidPriceException(message: String): Exception(message)
class InvalidAlcPercentageException(message: String): Exception(message)
class InvalidVolumeException(message: String): Exception(message)