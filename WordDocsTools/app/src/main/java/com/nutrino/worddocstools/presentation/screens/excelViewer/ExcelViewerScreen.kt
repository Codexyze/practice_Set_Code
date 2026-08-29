package com.nutrino.worddocstools.presentation.screens.excelViewer

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.GridOff
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.nutrino.worddocstools.domain.model.ExcelRow
import com.nutrino.worddocstools.domain.model.ExcelSheet

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExcelViewerScreen(
    uriString: String,
    onNavigateBack: () -> Unit,
    viewModel: ExcelViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    var isSearchVisible by remember { mutableStateOf(false) }

    LaunchedEffect(uriString) {
        viewModel.loadExcelFile(uriString)
    }

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            when (action) {
                is ExcelViewerAction.ShowToast -> {
                    Toast.makeText(context, action.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    val titleText = when (val state = uiState) {
                        is ExcelViewerUIState.Success -> state.workbook.fileName
                        else -> "Excel Document"
                    }
                    Text(
                        text = titleText,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
                actions = {
                    if (uiState is ExcelViewerUIState.Success) {
                        IconButton(onClick = { isSearchVisible = !isSearchVisible }) {
                            Icon(
                                imageVector = if (isSearchVisible) Icons.Default.Close else Icons.Default.Search,
                                contentDescription = "Search"
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1D6F42), // Excel Green
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (val state = uiState) {
                is ExcelViewerUIState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        CircularProgressIndicator(
                            color = Color(0xFF1D6F42),
                            strokeWidth = 4.dp
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Reading Excel spreadsheet...",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                is ExcelViewerUIState.Error -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.GridOff,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "Failed to open Excel file",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = state.message,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Button(
                            onClick = { viewModel.loadExcelFile(uriString) },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Retry")
                        }
                    }
                }

                is ExcelViewerUIState.Success -> {
                    val workbook = state.workbook
                    val currentSheet = workbook.sheets.getOrNull(state.selectedSheetIndex)

                    Column(modifier = Modifier.fillMaxSize()) {
                        // Search bar if toggled
                        if (isSearchVisible) {
                            OutlinedTextField(
                                value = state.searchQuery,
                                onValueChange = { viewModel.updateSearchQuery(it) },
                                placeholder = { Text("Search cell content...") },
                                leadingIcon = {
                                    Icon(imageVector = Icons.Default.Search, contentDescription = null)
                                },
                                trailingIcon = {
                                    if (state.searchQuery.isNotEmpty()) {
                                        IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                            Icon(imageVector = Icons.Default.Close, contentDescription = null)
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 12.dp, vertical = 8.dp),
                                singleLine = true,
                                shape = RoundedCornerShape(12.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = Color(0xFF1D6F42)
                                )
                            )
                        }

                        // Sheet Tab bar if multiple sheets
                        if (workbook.sheets.size > 1) {
                            ScrollableTabRow(
                                selectedTabIndex = state.selectedSheetIndex,
                                edgePadding = 12.dp,
                                containerColor = MaterialTheme.colorScheme.surfaceContainer
                            ) {
                                workbook.sheets.forEachIndexed { index, sheet ->
                                    Tab(
                                        selected = state.selectedSheetIndex == index,
                                        onClick = { viewModel.selectSheet(index) },
                                        text = {
                                            Text(
                                                text = sheet.name,
                                                fontWeight = if (state.selectedSheetIndex == index) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    )
                                }
                            }
                        }

                        if (currentSheet != null) {
                            ExcelSheetView(
                                sheet = currentSheet,
                                searchQuery = state.searchQuery,
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                                contentAlignment = Alignment.Center
                            ) {
                                Text("This spreadsheet is empty.")
                            }
                        }

                        // Bottom Status Bar
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            color = MaterialTheme.colorScheme.surfaceContainerHigh,
                            tonalElevation = 2.dp
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 16.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Sheet ${state.selectedSheetIndex + 1} of ${workbook.sheets.size}",
                                    style = MaterialTheme.typography.labelMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                if (currentSheet != null) {
                                    Text(
                                        text = "${currentSheet.rows.size} Rows • ${currentSheet.maxColumns} Columns",
                                        style = MaterialTheme.typography.labelMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }
                        }
                    }
                }

                else -> Unit
            }
        }
    }
}

@Composable
fun ExcelSheetView(
    sheet: ExcelSheet,
    searchQuery: String,
    modifier: Modifier = Modifier
) {
    val horizontalScrollState = rememberScrollState()

    // Filter rows if searching
    val filteredRows = remember(sheet, searchQuery) {
        if (searchQuery.isBlank()) {
            sheet.rows
        } else {
            sheet.rows.filter { row ->
                row.cells.any { cellText -> cellText.contains(searchQuery, ignoreCase = true) }
            }
        }
    }

    val maxCols = sheet.maxColumns.coerceAtLeast(1)
    val cellWidth = 120.dp
    val rowHeaderWidth = 48.dp

    if (filteredRows.isEmpty()) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (searchQuery.isNotBlank()) "No cells match '$searchQuery'" else "Empty sheet",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        return
    }

    Column(modifier = modifier.fillMaxSize()) {
        // Table Header (Row 0)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerHighest)
        ) {
            // Top-left corner box (Row index header)
            Box(
                modifier = Modifier
                    .width(rowHeaderWidth)
                    .height(36.dp)
                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "#",
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }

            // Column Header Letters (A, B, C...)
            Row(
                modifier = Modifier
                    .horizontalScroll(horizontalScrollState)
                    .height(36.dp)
            ) {
                for (colIndex in 0 until maxCols) {
                    Box(
                        modifier = Modifier
                            .width(cellWidth)
                            .height(36.dp)
                            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.7f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = getColumnLabel(colIndex),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    }
                }
            }
        }

        // Table Rows Body
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(filteredRows) { index, row ->
                val isEvenRow = index % 2 == 0
                val rowBgColor = if (isEvenRow) {
                    MaterialTheme.colorScheme.surface
                } else {
                    MaterialTheme.colorScheme.surfaceContainerLow
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(rowBgColor)
                ) {
                    // Left Row Number
                    Box(
                        modifier = Modifier
                            .width(rowHeaderWidth)
                            .height(44.dp)
                            .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                            .background(MaterialTheme.colorScheme.surfaceContainerHigh),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${row.rowIndex}",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    // Row Cells horizontally scrollable
                    Row(
                        modifier = Modifier
                            .horizontalScroll(horizontalScrollState)
                            .height(44.dp)
                    ) {
                        for (colIndex in 0 until maxCols) {
                            val cellValue = row.cells.getOrNull(colIndex) ?: ""
                            val isMatched = searchQuery.isNotBlank() && cellValue.contains(searchQuery, ignoreCase = true)

                            val cellBg = if (isMatched) {
                                Color(0xFFFFF59D) // Yellow highlight for matched search
                            } else {
                                Color.Unspecified
                            }

                            Box(
                                modifier = Modifier
                                    .width(cellWidth)
                                    .height(44.dp)
                                    .border(0.5.dp, MaterialTheme.colorScheme.outlineVariant)
                                    .background(cellBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.CenterStart
                            ) {
                                Text(
                                    text = cellValue,
                                    style = MaterialTheme.typography.bodySmall.copy(
                                        fontSize = 12.sp,
                                        fontWeight = if (isMatched) FontWeight.Bold else FontWeight.Normal
                                    ),
                                    color = if (isMatched) Color.Black else MaterialTheme.colorScheme.onSurface,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// Converts column index to Excel column name (0 -> A, 1 -> B, 25 -> Z, 26 -> AA)
private fun getColumnLabel(colIndex: Int): String {
    var temp = colIndex
    val label = StringBuilder()
    while (temp >= 0) {
        label.insert(0, ('A' + (temp % 26)))
        temp = (temp / 26) - 1
    }
    return label.toString()
}
