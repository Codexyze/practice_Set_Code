package com.nutrino.worddocstools.presentation.screens.excelViewer

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nutrino.worddocstools.domain.stateHandeling.ResultState
import com.nutrino.worddocstools.domain.usecase.ReadExcelUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ExcelViewModel @Inject constructor(
    private val readExcelUseCase: ReadExcelUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<ExcelViewerUIState>(ExcelViewerUIState.Idle)
    val uiState = _uiState.asStateFlow()

    private val _actions = Channel<ExcelViewerAction>(Channel.BUFFERED)
    val actions = _actions.receiveAsFlow()

    fun loadExcelFile(uriString: String) {
        val uri = Uri.parse(uriString)
        viewModelScope.launch {
            readExcelUseCase(uri).collect { result ->
                when (result) {
                    is ResultState.Loading -> _uiState.value = ExcelViewerUIState.Loading
                    is ResultState.Success -> {
                        _uiState.value = ExcelViewerUIState.Success(
                            workbook = result.data,
                            selectedSheetIndex = 0,
                            searchQuery = ""
                        )
                    }
                    is ResultState.Error -> {
                        _uiState.value = ExcelViewerUIState.Error(result.message)
                        _actions.send(ExcelViewerAction.ShowToast("Error opening file: ${result.message}"))
                    }
                }
            }
        }
    }

    fun selectSheet(index: Int) {
        val currentState = _uiState.value
        if (currentState is ExcelViewerUIState.Success) {
            _uiState.value = currentState.copy(selectedSheetIndex = index)
        }
    }

    fun updateSearchQuery(query: String) {
        val currentState = _uiState.value
        if (currentState is ExcelViewerUIState.Success) {
            _uiState.value = currentState.copy(searchQuery = query)
        }
    }
}
