package com.ktm.ktinder.presentation.ui.main

import android.graphics.Color
import android.os.Bundle
import androidx.activity.viewModels
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.constraintlayout.motion.widget.TransitionAdapter
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.ktm.ktinder.R
import com.ktm.ktinder.databinding.ActivityMainBinding
import com.ktm.ktinder.imageloader.extension.load
import com.ktm.ktinder.presentation.ui.base.BaseActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.motionLayout.setTransitionListener(object : TransitionAdapter() {
            override fun onTransitionCompleted(motionLayout: MotionLayout, currentId: Int) {
                when (currentId) {
                    R.id.constraintSetOffScreenUnlike,
                    R.id.constraintSetOffScreenLike -> {
                        motionLayout.progress = 0f
                        motionLayout.setTransition(
                            R.id.constraintSetStart,
                            R.id.constraintSetDetails
                        )
                        viewModel.moveToNextContact()
                    }
                }
            }
        })

        binding.btnLike.setOnClickListener {
            binding.motionLayout.transitionToState(R.id.constraintSetLike)
        }

        binding.btnUnlike.setOnClickListener {
            binding.motionLayout.transitionToState(R.id.constraintSetUnlike)
        }

        updateUiByState()
    }

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        viewModel.fetchContacts()
    }

    private fun updateUiByState() {
        lifecycleScope.launch {
            viewModel.mainUiState.collect { uiState ->
                when (uiState) {
                    MainUiState.Default -> {
                    }

                    is MainUiState.Success -> {
                        updateContact(uiState.contact)
                    }

                    is MainUiState.Error -> {
                    }
                }
            }
        }
    }

    private fun updateContact(contact: ContactUiModel) {
        ensureActivityActive {
            binding.containerCardOne.setBackgroundColor(
                Color.parseColor(contact.cardOne.bgColorHexCode)
            )
            binding.tvName.text = contact.cardOne.toString()
            binding.tvDescription.text = contact.cardOne.desc
            binding.ivCardOne.load(
                url = contact.cardOne.imageUrl
            )

            binding.containerCardTwo.setBackgroundColor(
                Color.parseColor(contact.cardTwo.bgColorHexCode)
            )
            binding.ivCardOne.load(
                url = contact.cardTwo.imageUrl
            )
        }
    }
}