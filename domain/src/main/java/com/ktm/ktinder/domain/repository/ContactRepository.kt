package com.ktm.ktinder.domain.repository

import com.ktm.ktinder.domain.entity.Contact
import com.ktm.ktinder.domain.repository.result.Error
import com.ktm.ktinder.domain.repository.result.Result

interface ContactRepository {

    suspend fun fetchContacts(): Result<List<Contact>, Error>
}