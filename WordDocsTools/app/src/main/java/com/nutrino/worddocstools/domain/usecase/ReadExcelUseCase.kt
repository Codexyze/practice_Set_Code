package com.nutrino.worddocstools.domain.usecase

import android.net.Uri
import com.nutrino.worddocstools.domain.model.ExcelWorkbook
import com.nutrino.worddocstools.domain.repository.ExcelRepository
import com.nutrino.worddocstools.domain.stateHandeling.ResultState
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ReadExcelUseCase @Inject constructor(
    private val repository: ExcelRepository
) {
    operator fun invoke(inputUri: Uri): Flow<ResultState<ExcelWorkbook>> {
        return repository.readExcelFile(inputUri)
    }
}
