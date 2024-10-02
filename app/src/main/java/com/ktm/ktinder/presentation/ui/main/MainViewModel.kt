package com.ktm.ktinder.presentation.ui.main

import androidx.lifecycle.viewModelScope
import com.ktm.ktinder.domain.repository.ContactRepository
import com.ktm.ktinder.domain.repository.result.Result
import com.ktm.ktinder.domain.usecase.ContactUseCase
import com.ktm.ktinder.presentation.ui.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    contactRepository: ContactRepository
) : BaseViewModel() {

    private val contactUseCase = ContactUseCase(contactRepository)

    private val _mainUiState = MutableStateFlow<MainUiState>(MainUiState.Default)
    val mainUiState: StateFlow<MainUiState> = _mainUiState

    private val cards = arrayListOf<CardUiModel>()

    private var cardIndex = 0

    fun fetchContacts() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.Default) {
                contactUseCase.fetchContacts()
            }
            when (result) {
                is Result.Success -> {
                    cards.clear()
                    cardIndex = 0
                    result.data.forEach { contact ->
                        cards.add(
                            CardUiModel(
                                name = contact.name,
                                age = contact.age,
                                desc = contact.desc,
                                imageUrl = contact.imageUrl,
                                bgColorHexCode = "#D3D3D3"
                            )
                        )
                    }
                    notifyUpdateContact()
                }

                is Result.Error -> {
                    _mainUiState.value = MainUiState.Error()
                }
            }
        }
    }

    fun moveToNextContact() {
        cardIndex++
        notifyUpdateContact()
    }

    private fun notifyUpdateContact() {
        _mainUiState.value = MainUiState.Success(
            ContactUiModel(
                cardOne = cards[cardIndex % cards.size],
                cardTwo = cards[(cardIndex + 1) % cards.size]
            )
        )
    }
}

sealed class MainUiState {
    data object Default : MainUiState()

    data class Success(val contact: ContactUiModel) : MainUiState()

    data class Error(
        val errorCode: Int? = null, val throwable: Throwable? = null
    ) : MainUiState()
}