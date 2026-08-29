package com.nutrino.worddocstools.domain.repository

import android.net.Uri
import com.nutrino.worddocstools.domain.model.ExcelWorkbook
import com.nutrino.worddocstools.domain.stateHandeling.ResultState
import kotlinx.coroutines.flow.Flow

interface ExcelRepository {
    fun readExcelFile(inputUri: Uri): Flow<ResultState<ExcelWorkbook>>
}
