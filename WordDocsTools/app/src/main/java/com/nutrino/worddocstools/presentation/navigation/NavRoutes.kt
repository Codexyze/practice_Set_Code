package com.nutrino.worddocstools.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object FEATURE_SELECTION_SCREEN

@Serializable
data class EXCEL_VIEWER_SCREEN(val uriString: String)
