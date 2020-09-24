package com.jinxian.wenshi.di

import org.koin.dsl.module

val viewModelModule = module {

}

val reposModule = module {

}

val remoteModule = module {

}

val localModule = module {

}

val appModule = listOf(viewModelModule, reposModule, remoteModule, localModule)