![MacDown logo](https://upload.wikimedia.org/wikipedia/commons/thumb/e/ea/Onefootball.svg/2000px-Onefootball.svg.png)

# Android Code Challenge
The task of this code challenge is to refactor the structure of push settings in our sample project.  
The project consists of two packages `framework` and a `data` package. Your main focus should be on refactoring the structure of elements within the `data` package
according to the following specifications

## Specifications
The sample project consists of a screen and a dialog representing a team and
it's push settings. A push setting refers to an item for which the user wishes to receive a push notification. Therefore, each team provides a user configurable list of these options. Within this sample we have one team.

## Requirements
* Refactor the classes within the `data` package focusing on readability, maintainability and testability.
* The project is compileable.
* The project runs without bugs.
* A couple (2-3) of **meaningful** unit tests are provided.

## Nice to have
* Use Kotlin

## General Conditions
While completing the code challenge keep in mind:

* Do not spend too much time on details
* Leave some `TODO` to let us know in detail what you'd have done if there was more time
* Please send us the project as a zip file or as link to a repository


----------------------------------------------------------------------
----------------------------------------------------------------------

## Done Task
* converted and modified all files into Kotlin
* Reduced boilerplate code
* Isolated the the encode-decode functions in an `Object Declaration`
* Refactored the `data` packages
* Slightly modified Layouts
* Added strings in `string.xml` and `dimen.xml`
* Removed all the warnings and errors during building the project
* Added 6 unit testing using `Robolectric`
* Added the picture as the comparison between previous and current code
![Alt-Text](/codeLines.png)

## TODO Task
* modify the `Fragment` class
* implement the `Android Architecture Components`
* implement the `Espresso` for UI testing
