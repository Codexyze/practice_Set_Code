package com.nutrino.worddocstools.domain.model

data class ExcelRow(
    val rowIndex: Int,
    val cells: List<String>
)

data class ExcelSheet(
    val name: String,
    val rows: List<ExcelRow>,
    val maxColumns: Int
)

data class ExcelWorkbook(
    val fileName: String,
    val sheets: List<ExcelSheet>
)
