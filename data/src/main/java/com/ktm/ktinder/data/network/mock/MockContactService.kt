package com.ktm.ktinder.data.network.mock

import com.ktm.ktinder.data.network.ContactService
import com.ktm.ktinder.domain.entity.Contact
import javax.inject.Inject

class MockContactService @Inject constructor() : ContactService {

    override suspend fun fetchContacts(): List<Contact> {
        return MockResponse.getContacts()
    }
}