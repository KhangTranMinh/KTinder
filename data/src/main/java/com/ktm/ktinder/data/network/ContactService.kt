package com.ktm.ktinder.data.network

import com.ktm.ktinder.domain.entity.Contact

interface ContactService {

    suspend fun fetchContacts(): List<Contact>
}