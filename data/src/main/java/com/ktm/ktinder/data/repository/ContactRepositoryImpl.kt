package com.ktm.ktinder.data.repository

import com.ktm.ktinder.data.network.api.FetchContactsApi
import com.ktm.ktinder.domain.entity.Contact
import com.ktm.ktinder.domain.repository.ContactRepository
import com.ktm.ktinder.domain.repository.result.Error
import com.ktm.ktinder.domain.repository.result.Result
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val fetchContactsApi: FetchContactsApi
) : ContactRepository {

    override suspend fun fetchContacts(): Result<List<Contact>, Error> {
        return fetchContactsApi.fetchContacts()
    }
}