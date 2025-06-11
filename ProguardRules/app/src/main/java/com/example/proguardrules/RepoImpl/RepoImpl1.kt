package com.example.proguardrules.RepoImpl

import com.example.proguardrules.Domain.FakeRepository

class RepoImpl1: FakeRepository {
    override fun fakeFunction(): String {
        return "Xyzzzzz"
    }
}