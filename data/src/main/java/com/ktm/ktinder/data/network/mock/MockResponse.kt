package com.ktm.ktinder.data.network.mock

import com.ktm.ktinder.domain.entity.Contact

object MockResponse {

    fun getContacts(): List<Contact> {
        return arrayListOf(
            Contact(
                name = "Iron Man",
                age = 35,
                desc = "The church was white and brown and looked very old.",
                imageUrl = "https://fastly.picsum.photos/id/839/270/424.jpg?hmac=cyjDUhIctuH9QipDfOpZhvJpG_ynh7NS6WZKPllLmtc",
            ),
            Contact(
                name = "Captain America",
                age = 38,
                desc = "I wished that father could be here to see this.",
                imageUrl = "https://fastly.picsum.photos/id/1049/270/424.jpg?hmac=TPCCtx7c1gH4dTt3BvDHBP90Wb6h83xAkWCq5hwBXzU",
            ),
            Contact(
                name = "Spider-Man",
                age = 28,
                desc = "My pet Roger is white and fluffy and he loves to eat carrots.",
                imageUrl = "https://fastly.picsum.photos/id/977/270/424.jpg?hmac=K6D5gzVvEyGIweI1pBUjWn3Jhtb9KeLY1KIyERRJdKU",
            ),
            Contact(
                name = "Deadpool",
                age = 45,
                desc = "She was starting to run out of things to say.",
                imageUrl = "https://fastly.picsum.photos/id/480/270/424.jpg?hmac=Ccj6msZzhwSnhEUPzdyH1JJCAOb53GCa2hfJz3Sips0",
            ),
            Contact(
                name = "Black Panther",
                age = 34,
                desc = "We've been making this recipe for generations.",
                imageUrl = "https://fastly.picsum.photos/id/212/270/424.jpg?hmac=RKmb3T_lRAE7ySF7MYqdoC_S5_uql1ewuQRgpkYSAs8",
            ),
            Contact(
                name = "Jessica Jones",
                age = 26,
                desc = "You would somehow manage to make yourself understood.",
                imageUrl = "https://loremflickr.com/cache/resized/65535_5036373427_d18d839491_270_424_nofilter.jpg",
            ),
            Contact(
                name = "Ant-Man",
                age = 33,
                desc = "My pen broke and leaked blue ink all over my new dress.",
                imageUrl = "https://loremflickr.com/cache/resized/65535_53320330330_911483bfae_270_424_nofilter.jpg",
            ),
            Contact(
                name = "Captain Marvel",
                age = 37,
                desc = "Hit me with your pet shark!",
                imageUrl = "https://loremflickr.com/cache/resized/65535_53446123919_bdbe79957b_z_270_424_nofilter.jpg",
            ),
            Contact(
                name = "Wolverine",
                age = 28,
                desc = "How long did you think you could get away with this?",
                imageUrl = "https://loremflickr.com/cache/resized/65535_53659058616_d8feb20e2f_c_270_424_nofilter.jpg",
            ),
            Contact(
                name = "Luke Cage",
                age = 49,
                desc = "My German vocabulary list is up to two thousand words now.",
                imageUrl = "https://loremflickr.com/cache/resized/65535_53890577972_e0145f0882_c_270_424_nofilter.jpg",
            ),
        )
    }
}