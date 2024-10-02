package com.ktm.ktinder.presentation.ui.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import com.ktm.ktinder.presentation.util.Logger

abstract class BaseActivity : AppCompatActivity(), DefaultLifecycleObserver {

    protected val logger = Logger.init(this::class.java.simpleName)

    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        lifecycle.addObserver(this)
    }

    protected fun ensureActivityActive(block: () -> Unit) {
        if (lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)) {
            kotlin.runCatching {
                block()
            }.onFailure {
                logger.log("Error happens!", it)
            }
        }
    }
}