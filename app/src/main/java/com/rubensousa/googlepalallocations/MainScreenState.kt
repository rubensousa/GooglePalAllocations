package com.rubensousa.googlepalallocations

import androidx.compose.runtime.Immutable
import kotlinx.coroutines.flow.StateFlow

@Immutable
data class MainScreenState(
    val allocatedThreads: StateFlow<List<String>>,
    val noncesGenerated: StateFlow<Int>,
    val onGenerateClick: () -> Unit,
    val onReleaseClick: () -> Unit,
)
