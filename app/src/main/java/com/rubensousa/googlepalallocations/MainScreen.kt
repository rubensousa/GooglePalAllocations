package com.rubensousa.googlepalallocations

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.rubensousa.googlepalallocations.ui.theme.GooglePalAllocationsTheme
import kotlinx.coroutines.flow.MutableStateFlow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    state: MainScreenState,
) {
    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(
                    "GooglePalAllocations",
                    style = MaterialTheme.typography.headlineSmall
                )
            })
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentAlignment = Alignment.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                val noncesGenerated = state.noncesGenerated.collectAsStateWithLifecycle().value
                val threadsAllocated = state.allocatedThreads.collectAsStateWithLifecycle().value
                Text(
                    text = "Nonces generated: $noncesGenerated"
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Button(
                        modifier = Modifier.weight(1.5f),
                        onClick = {
                            state.onGenerateClick()
                        }
                    ) {
                        Text("Create nonce")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            state.onGenerateClick()
                        }
                    ) {
                        Text("Release")
                    }
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = {
                            System.gc()
                        }
                    ) {
                        Text("Run GC")
                    }
                }

                Text(
                    text = "Threads allocated: ${threadsAllocated.size}"
                )
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxSize()
                        .background(Color.LightGray)
                        .padding(16.dp)

                ) {
                    items(threadsAllocated) { item ->
                        Text(item)
                    }
                }
            }

        }
    }
}

@Preview
@Composable
private fun PreviewMainScreen() {
    GooglePalAllocationsTheme {
        MainScreen(
            state = MainScreenState(
                allocatedThreads = MutableStateFlow(listOf("Sdsd")),
                noncesGenerated = MutableStateFlow(1),
                onGenerateClick = {},
                onReleaseClick = {}
            )
        )
    }
}