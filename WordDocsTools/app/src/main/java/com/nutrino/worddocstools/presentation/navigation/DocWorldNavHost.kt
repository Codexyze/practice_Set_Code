package com.nutrino.worddocstools.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.nutrino.worddocstools.presentation.screens.excelViewer.ExcelViewerScreen
import com.nutrino.worddocstools.presentation.screens.featureSelection.FeatureSelectionScreen

@Composable
fun DocWorldNavHost(
    navController: NavHostController = rememberNavController(),
    startDestination: Any = FEATURE_SELECTION_SCREEN
) {
    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        composable<FEATURE_SELECTION_SCREEN> {
            FeatureSelectionScreen(
                onExcelSelected = { uri ->
                    navController.navigate(EXCEL_VIEWER_SCREEN(uri.toString()))
                }
            )
        }

        composable<EXCEL_VIEWER_SCREEN> { backStackEntry ->
            val route: EXCEL_VIEWER_SCREEN = backStackEntry.toRoute()
            ExcelViewerScreen(
                uriString = route.uriString,
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
