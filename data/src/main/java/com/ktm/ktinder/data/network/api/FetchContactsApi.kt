package com.ktm.ktinder.data.network.api

import com.ktm.ktinder.data.network.ContactService
import com.ktm.ktinder.domain.entity.Contact
import com.ktm.ktinder.domain.repository.result.Error
import com.ktm.ktinder.domain.repository.result.Result
import javax.inject.Inject

class FetchContactsApi @Inject constructor(
    private val contactService: ContactService
) {

    suspend fun fetchContacts(): Result<List<Contact>, Error> {
        return Result.Success(contactService.fetchContacts())
    }
}