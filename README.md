# Jobsity Demo

![1](https://github.com/ispam/jobsity/blob/master/gif/jobsity.gif)

**Objective:** Build an app to display shows and its episodes, search and favorites

## Architectural Pattern

** Model View ViewModel (MVVM) **

The architectural pattern are presented by modules:
-   *Di Module:* This module is responsible for dealing with all the dependency injections
-   *Common Module:* This module has the responsibility to hold all common util classes and base components
-   *UI Module:* This module is for all the UI components like main screen, search and favorites
-   *Utils Module:* This module holds all utility classes and extensions

## Libraries
- [Dagger Hilt](https://dagger.dev/hilt/) by Google
- [Retrofit](https://github.com/square/retrofit) by Square
- [okHTTP](https://github.com/square/okhttp) by Square
- [Picasso](https://github.com/square/picasso) by Square

## License

Copyright [2022] [Diego Fernando Urrea Gutiérrez]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.