package com.example.proguardrules.RepoImpl

import com.example.proguardrules.Domain.FakeRepository

class RepoImpl2: FakeRepository {
    override fun fakeFunction(): String {
        return "Repo2"
    }
}