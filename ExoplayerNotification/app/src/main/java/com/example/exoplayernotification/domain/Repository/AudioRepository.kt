package com.example.exoplayernotification.domain.Repository

import com.example.exoplayernotification.data.Local.Song
import com.example.exoplayernotification.domain.SateHandeling.ResultState
import kotlinx.coroutines.flow.Flow

interface AudioRepository {
    fun getAllSongs(): Flow<ResultState<List<Song>>>
}