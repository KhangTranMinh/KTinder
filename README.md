# KTinder

## Architecture
This project follow **CLean Architecture**.

The project contains 3 modules:

1. Domain

    - This module contains business logic models of the application
    - This module define the Repository interfaces, then the higher modules can implement these interfaces
    - It also define Use Cases that handle business logic of the application using Repository interfaces

2. Data

    - This module contains Repository implementations
    - This module contains Android components (such as Network, Data Soures..) that can be used in Repository implementation

3. Presentation

    - This module handles UI parts of the application
    - This module follows MVVM pattern

The project use **Hilt** for Dependency Injection


## Requirements

The initialized requirement is to develop an application like Tinder (focus on the main page).

After analyzing the requirement, I split the requirement into 2 parts:

1. Implement **Main page** like Tinder's one

    - To fulfill this requirement, I use `MotionLayout` to implement action `Swipe left`, `Swipe right`. Using `MotionLayout`, we can define **constraints** and **transition** to support "Swipe" actions.
    - Beside `MotionLayout`, I also use `MaterialCardView` to implement the card UI
    - For the `Contact` data, I return mock objects from class `MockContactService`

2. The **ImageLoader** (I call it `KTinderImageLoader`)

    - To display `Contact`'s image in the card, I need to implement an **ImageLoader** to display image from URL
    - `KTinderImageLoader` will do following steps:

      Step 1: check if the bitmap exists in `MemCache` and `DiskCache`. If Yes, display the bitmap into `ImageView`

      Step 2: download the bitmap, save it into `MemCache` and `DiskCache`

      Step 3: apply the transformation if needed

      Step 4: display the bitmap into `ImageView`

## Screen record

![](screenshot/screen-record.gif)