package com.example.whattoeat.data

import dagger.hilt.android.scopes.ViewModelScoped
import javax.inject.Inject
import javax.inject.Named


@ViewModelScoped
class Repository @Inject constructor(
    remoteDataSource: RemoteDataSource,
    localDataSource: LocalDataSource
){
    val remote = remoteDataSource
    val local = localDataSource
}

interface DataSource<T>

class UseCase(
    private val remoteDataSource: DataSource<RemoteDataSource>,
    private val otherDataSource: DataSource<LocalDataSource>
){

}

