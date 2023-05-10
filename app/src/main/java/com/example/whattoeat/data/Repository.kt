package com.example.whattoeat.data

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import javax.inject.Named

@ViewModelScoped
class Repository @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource
){
    val remote = remoteDataSource
    val local = localDataSource
}

interface DataSource<T>

class UseCase(
    private val remoteDataSource: DataSource<String>,
    @Named("local_data_source")
    private val otherDataSource: DataSource<Int>

)