package com.example.exoplayernotification.domain.UseCases

import com.example.exoplayernotification.data.Local.Song
import com.example.exoplayernotification.domain.Repository.AudioRepository
import com.example.exoplayernotification.domain.SateHandeling.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllSongUseCase @Inject constructor(private val getAllSongRepository: AudioRepository) {
    suspend operator fun invoke(): Flow<ResultState<List<Song>>>{
        return getAllSongRepository.getAllSongs()
    }
}