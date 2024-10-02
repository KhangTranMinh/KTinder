package com.ktm.ktinder.presentation.ui.base

import androidx.lifecycle.ViewModel
import com.ktm.ktinder.presentation.util.Logger

open class BaseViewModel : ViewModel() {

    protected val logger = Logger.init(this::class.java.simpleName)
}