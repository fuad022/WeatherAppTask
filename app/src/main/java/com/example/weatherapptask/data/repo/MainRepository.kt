package com.example.weatherapptask.data.repo

class MainRepository(
    repository: Repository,
    localRepository: LocalRepository
) {
    val remote = repository
    val local = localRepository
}