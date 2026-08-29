package com.nutrino.worddocstools.presentation.screens.excelViewer

import com.nutrino.worddocstools.domain.model.ExcelWorkbook

sealed class ExcelViewerUIState {
    object Idle : ExcelViewerUIState()
    object Loading : ExcelViewerUIState()
    data class Success(
        val workbook: ExcelWorkbook,
        val selectedSheetIndex: Int = 0,
        val searchQuery: String = ""
    ) : ExcelViewerUIState()
    data class Error(val message: String) : ExcelViewerUIState()
}

sealed class ExcelViewerAction {
    data class ShowToast(val message: String) : ExcelViewerAction()
}
