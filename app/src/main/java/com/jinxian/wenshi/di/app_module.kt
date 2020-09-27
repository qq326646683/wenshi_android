package com.jinxian.wenshi.di

import com.jinxian.wenshi.data.http.UserService
import com.jinxian.wenshi.module_user.api.UserApi
import com.jinxian.wenshi.module_user.repository.UserRepository
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { UserViewModel(get()) }
}

val reposModule = module {
    factory { UserRepository(get()) }
}

val remoteModule = module {
    single<UserApi> { UserService }
}

val localModule = module {

}

val appModule = listOf(viewModelModule, reposModule, remoteModule, localModule)