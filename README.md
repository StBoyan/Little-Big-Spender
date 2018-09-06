# Little Big Spender
Personal money management Android application developed in part fulfillment of the requirements of the Degree of Master of Science at the University of Glasgow.

## Features
* Supports Android 5.0 (API 21) and above
* Track your accounts
* Record income and expense transactions
* Categorise transactions
* Choose home currency
* Add recurring transactions
* View charts of your finances
* Integrates cryptocurrencies

## Screenshots
![Overview](/screenshots/overview.png "Overview") ![Transactions](/screenshots/transactions.png "Transactions screen")![Accounts](/screenshots/accounts.png "Accounts")

![Navigation Drawer](/screenshots/navdrawer.png "Navigation Drawer") ![Categories](/screenshots/categories.png "Categories") ![Statistics](/screenshots/stat4.png "Statistics")

## How to build and run project on an emulator from Android Studio 3.0+
1. Clone this repository on your computer
2. Open Android Studio and click "Open an existing Android Studio project"
3. Navigate and select the folder of the repository
4. From the menu go to Tools -> AVD Manager and click "Create Virtual Device"
5. Select a device/screen size and make sure to select API level 21 at minimum 
6. From the menu go to Build and click "Make Project"
7. From the menu go to Run -> Run 'app' and select the virtual device you created earlier

## Documentation
Javadoc documentation of the whole project, including private methods can be found [**here**](https://stboyan.github.io/Little-Big-Spender/).

## Libraries 
- [**AppIntro**](https://github.com/apl-devs/AppIntro)
- [**AboutLibraries**](https://github.com/mikepenz/AboutLibraries)
- [**Butter Knife**](https://github.com/JakeWharton/butterknife)
- [**Currency Picker**](https://github.com/midorikocak/currency-picker-android)
- [**Material Dialogs**](https://github.com/afollestad/material-dialogs)
- [**MPAndroidChart**](https://github.com/PhilJay/MPAndroidChart)
- [**OkHttp**](https://github.com/square/okhttp)
- [**Realm**](https://github.com/realm/realm-java)

## Statistic
The table below summarises the lines of code that make up the project, including Java and XML.

| Language      | Number of files | Blank lines | Comment lines | Lines of code |
| ------------- | --------------- | ----------- | ------------- | ------------- |
| **Java**      |               52|         1013|           1217|           4489|
| **XML**       |               48|          282|             23|           2363|
| **Total**     |              100|         1295|           1240|           6852|

## License
    MIT License
     
    Copyright (c) 2018 Boyan Stoynov
     
    Permission is hereby granted, free of charge, to any person obtaining a copy
    of this software and associated documentation files (the "Software"), to deal
    in the Software without restriction, including without limitation the rights
    to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
    copies of the Software, and to permit persons to whom the Software is
    furnished to do so, subject to the following conditions:
     
    The above copyright notice and this permission notice shall be included in all
    copies or substantial portions of the Software.
     
    THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
    IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
    FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
    AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
    LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
    OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
    SOFTWARE.