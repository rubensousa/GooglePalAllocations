package com.rubensousa.googlepalallocations

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.ads.interactivemedia.pal.ConsentSettings
import com.google.ads.interactivemedia.pal.NonceLoader
import com.google.ads.interactivemedia.pal.NonceRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.util.UUID

class MainViewModel(
    private val application: Application,
) : AndroidViewModel(application) {

    private val threads = MutableStateFlow<List<String>>(emptyList())
    private val nonces = MutableStateFlow(0)

    // Lazy to avoid immediate creation and check allocations before this gets created
    private var nonceLoader: NonceLoader? = null
    private val state = MainScreenState(
        allocatedThreads = threads.asStateFlow(),
        noncesGenerated = nonces.asStateFlow(),
        onGenerateClick = {
            generateNonce()
        },
        onReleaseClick = {
            nonceLoader?.release()
            nonceLoader = null
        }
    )

    init {
        observeThreadAllocations()
    }

    fun getState() = state

    private fun generateNonce() {
        nonces.update { count -> count + 1 }
        getOrCreateNonceLoader().loadNonceManager(createRandomNonceRequest())
            .addOnSuccessListener { manager ->
                Log.i("NONCE", "Nonce generated: ${manager.nonce}")
            }
    }

    private fun getOrCreateNonceLoader(): NonceLoader {
        nonceLoader?.let { return it }
        val newLoader = NonceLoader(
            application, ConsentSettings.builder()
                .allowStorage(true)
                .build()
        )
        nonceLoader = newLoader
        return newLoader
    }

    private fun createRandomNonceRequest(): NonceRequest {
        val supportedApiFrameworks = setOf(2, 7, 9)
        val nonceRequest = NonceRequest.builder()
            .descriptionURL(UUID.randomUUID().toString())
            .iconsSupported(true)
            .omidPartnerVersion("6.2.1")
            .omidPartnerName("Example Publisher")
            .playerType("ExamplePlayerType")
            .playerVersion("1.0.0")
            .ppid("testPpid")
            .sessionId(UUID.randomUUID().toString())
            .supportedApiFrameworks(supportedApiFrameworks)
            .videoPlayerHeight(480)
            .videoPlayerWidth(640)
            .willAdAutoPlay(true)
            .willAdPlayMuted(false)
            .build()
        return nonceRequest
    }

    private fun observeThreadAllocations() {
        viewModelScope.launch(Dispatchers.IO) {
            while (isActive) {
                threads.value = Thread.getAllStackTraces().keys
                    .filter { it.isAlive }
                    .sortedByDescending { it.name }
                    .map { it.name }
                delay(1_000L)
            }
        }
    }
}