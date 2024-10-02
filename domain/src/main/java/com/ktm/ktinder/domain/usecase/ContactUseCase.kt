package com.ktm.ktinder.domain.usecase

import com.ktm.ktinder.domain.entity.Contact
import com.ktm.ktinder.domain.repository.ContactRepository
import com.ktm.ktinder.domain.repository.result.Error
import com.ktm.ktinder.domain.repository.result.Result

class ContactUseCase(
    private val contactRepository: ContactRepository
) {

    suspend fun fetchContacts(): Result<List<Contact>, Error> {
        return contactRepository.fetchContacts()
    }
}