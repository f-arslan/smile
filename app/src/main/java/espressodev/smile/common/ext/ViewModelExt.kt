package espressodev.smile.common.ext

import kotlinx.coroutines.flow.MutableStateFlow

inline fun <reified T: Any> updateUiState(
    stateFlow: MutableStateFlow<T>,
    block: T.() -> T
) {
    stateFlow.value = stateFlow.value.block()
}