package com.jinxian.wenshi.di

import com.jinxian.wenshi.data.http.MainService
import com.jinxian.wenshi.data.http.UserService
import com.jinxian.wenshi.module_main.api.MainApi
import com.jinxian.wenshi.module_main.repository.MainRepository
import com.jinxian.wenshi.module_main.viewmodel.HomeViewModel
import com.jinxian.wenshi.module_user.api.UserApi
import com.jinxian.wenshi.module_user.repository.UserRepository
import com.jinxian.wenshi.module_user.viewmodel.UserViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { UserViewModel(get()) }

    viewModel { HomeViewModel(get()) }
}

val reposModule = module {
    factory { UserRepository(get()) }

    factory { MainRepository(get()) }
}

val remoteModule = module {
    // single 单例注入

    single<UserApi> { UserService }

    single<MainApi> { MainService }

}

val localModule = module {

}

val appModule = listOf(viewModelModule, reposModule, remoteModule, localModule)