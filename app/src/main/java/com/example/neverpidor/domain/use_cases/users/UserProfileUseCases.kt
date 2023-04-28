package com.example.neverpidor.domain.use_cases.users

class UserProfileUseCases(
    val changeUserNameUseCase: ChangeUserNameUseCase,
    val changeUserPasswordUseCase: ChangeUserPasswordUseCase,
    val deleteUserUseCase: DeleteUserUseCase,
    val getUserUseCase: GetUserUseCase,
    val findUserByNumberUseCase: FindUserByNumberUseCase,
    val setCurrentUserUseCase: SetCurrentUserUseCase,
    val registerUserUseCase: RegisterUserUseCase,
    val addUserListenerUseCase: AddUserListenerUseCase
)