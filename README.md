# Android Card Deck App

Card Deck application for android that allows users to choose from a variety of decks to use however they please. This includes conversation starters to have fun with friends and loved ones or get to know strangers, flip card packs to practice language skills, trivia, and more. Users are able to download additional decks as well.

## About
Cards are versitile; they can be simple text, double-sided text, or include pictures on one or more side. They are stored locally in an SQL database on your device so you can play offline. The app can also connect to an library online allowing the user to look through new decks (with images and descriptions) and download them to their devices.

Application built in Android Studio in Java. Uses Room SQL database and Volley for internet request. 

## Uses
* Playing card deck
* Conversation starters
* Flip cards (practice foreign languages, vocabulary, et cetera)
* Trivia
* Other card based games

## Existing features
* SQL database
  * Deck table
  * Card table
  * Categories
* Default decks installed with application
* Deck UI
  * UI changes to handle cards with different combinations of photo and text
  * Decks shuffle on start
* Start activity remembers last deck used
* Choose deck recyclerview activity shows photos and titles of downloaded decks
* Floating button allows user to add new decks
* New deck activity, also recylclerview, pulls information from internet JSON and displays
* New deck activity allows downloading additional decks
  * Updates database
  * Stores images on internal storage for later retrieval

## Features under development
* Create new decks and edit existing decks
* Add additional decks to online library
* Add search and filter feature (categories already stored in database)
* Option to choose language (language already stored in database)
* Option to maintain deck state (currently does not save place in deck if application exited and resuffles)

## Github Repo
[card_deck_app](https://github.com/dale-wahl/card_deck_app)
