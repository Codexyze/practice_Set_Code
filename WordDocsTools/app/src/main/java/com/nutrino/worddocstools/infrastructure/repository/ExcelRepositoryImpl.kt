package com.nutrino.worddocstools.infrastructure.repository

import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import com.nutrino.worddocstools.domain.model.ExcelRow
import com.nutrino.worddocstools.domain.model.ExcelSheet
import com.nutrino.worddocstools.domain.model.ExcelWorkbook
import com.nutrino.worddocstools.domain.repository.ExcelRepository
import com.nutrino.worddocstools.domain.stateHandeling.ResultState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.CellType
import org.apache.poi.ss.usermodel.DateUtil
import org.apache.poi.ss.usermodel.FormulaEvaluator
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.text.SimpleDateFormat
import java.util.Locale
import javax.inject.Inject

class ExcelRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val ioDispatcher: CoroutineDispatcher
) : ExcelRepository {

    override fun readExcelFile(inputUri: Uri): Flow<ResultState<ExcelWorkbook>> = flow {
        emit(ResultState.Loading)
        try {
            val fileName = getFileNameFromUri(inputUri) ?: "Spreadsheet.xlsx"
            context.contentResolver.openInputStream(inputUri)?.use { inputStream ->
                val workbook: Workbook = WorkbookFactory.create(inputStream)
                try {
                    val formulaEvaluator = workbook.creationHelper.createFormulaEvaluator()
                    val sheetsList = mutableListOf<ExcelSheet>()

                    for (sheetIndex in 0 until workbook.numberOfSheets) {
                        val sheet = workbook.getSheetAt(sheetIndex)
                        val sheetName = sheet.sheetName ?: "Sheet ${sheetIndex + 1}"

                        var maxCols = 0
                        val rowsList = mutableListOf<ExcelRow>()

                        for (r in 0..sheet.lastRowNum) {
                            val row = sheet.getRow(r)
                            if (row == null) {
                                rowsList.add(ExcelRow(rowIndex = r + 1, cells = emptyList()))
                                continue
                            }

                            val colsInRow = row.lastCellNum.toInt()
                            if (colsInRow > maxCols) {
                                maxCols = colsInRow
                            }

                            val cellValues = mutableListOf<String>()
                            for (c in 0 until colsInRow) {
                                val cell = row.getCell(c)
                                cellValues.add(getCellValueAsString(cell, formulaEvaluator))
                            }
                            rowsList.add(ExcelRow(rowIndex = r + 1, cells = cellValues))
                        }

                        sheetsList.add(
                            ExcelSheet(
                                name = sheetName,
                                rows = rowsList,
                                maxColumns = maxCols
                            )
                        )
                    }

                    val excelWorkbook = ExcelWorkbook(
                        fileName = fileName,
                        sheets = sheetsList
                    )
                    emit(ResultState.Success(excelWorkbook))
                } finally {
                    workbook.close()
                }
            } ?: throw Exception("Could not open input stream from file.")
        } catch (e: Exception) {
            e.printStackTrace()
            emit(ResultState.Error(e.localizedMessage ?: "Failed to read Excel file"))
        }
    }.flowOn(ioDispatcher)

    private fun getFileNameFromUri(uri: Uri): String? {
        var name: String? = null
        if (uri.scheme == "content") {
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                if (cursor.moveToFirst()) {
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    if (nameIndex != -1) {
                        name = cursor.getString(nameIndex)
                    }
                }
            }
        }
        if (name == null) {
            name = uri.path?.let {
                val cut = it.lastIndexOf('/')
                if (cut != -1) it.substring(cut + 1) else it
            }
        }
        return name
    }

    private fun getCellValueAsString(cell: Cell?, evaluator: FormulaEvaluator?): String {
        if (cell == null) return ""

        return when (cell.cellType) {
            CellType.STRING -> cell.stringCellValue ?: ""
            CellType.NUMERIC -> {
                if (DateUtil.isCellDateFormatted(cell)) {
                    val date = cell.dateCellValue
                    val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())
                    sdf.format(date)
                } else {
                    val num = cell.numericCellValue
                    if (num % 1 == 0.0) {
                        num.toLong().toString()
                    } else {
                        num.toString()
                    }
                }
            }
            CellType.BOOLEAN -> cell.booleanCellValue.toString()
            CellType.FORMULA -> {
                try {
                    val evalResult = evaluator?.evaluate(cell)
                    when (evalResult?.cellType) {
                        CellType.STRING -> evalResult.stringValue ?: ""
                        CellType.NUMERIC -> {
                            val num = evalResult.numberValue
                            if (num % 1 == 0.0) num.toLong().toString() else num.toString()
                        }
                        CellType.BOOLEAN -> evalResult.booleanValue.toString()
                        else -> cell.cellFormula ?: ""
                    }
                } catch (e: Exception) {
                    try {
                        cell.stringCellValue ?: cell.cellFormula ?: ""
                    } catch (ex: Exception) {
                        ""
                    }
                }
            }
            CellType.BLANK -> ""
            CellType.ERROR -> "#ERROR!"
            else -> ""
        }
    }
}
