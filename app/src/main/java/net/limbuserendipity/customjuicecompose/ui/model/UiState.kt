package net.limbuserendipity.customjuicecompose.ui.model

sealed class UiState{

    object Quietly : UiState()
    object Progress : UiState()
    object Complete : UiState()
    object Completed : UiState()

}