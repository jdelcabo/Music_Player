# MusicPlayer

Android music discovery app built around the iTunes Search API. It lets users explore songs and albums, open detailed views, play preview audio, and save favourites locally for later access.

## Overview

MusicPlayer is a bottom-navigation Android app with three main areas:

- **Search**: discover songs and albums by keyword or browse random results.
- **Guess**: a song-guessing mini game based on audio previews.
- **Favourites**: a local collection of saved songs and albums.

The app is designed for fast browsing, media preview playback, and lightweight offline persistence for user favourites.

## Main features

- Search songs and albums through the iTunes Search API
- Browse random song and album suggestions
- Infinite loading when browsing random results
- Dedicated detail screens for songs and albums
- In-app audio preview playback with seek bar, play/pause, and loop controls
- Save and remove favourite songs and albums
- Local persistence using `SharedPreferences`
- Song sorting by:
    - alphabetical order
    - release date
    - duration
- Guess-the-song mode with lives, progress tracking, and score feedback

## How it works

### Search
The search screen shows song and album cards backed by API data. When no search term is entered, the app loads random results. When a keyword is entered, the app fetches matching songs and albums from the iTunes API.

### Song and album browsing
Each card opens a full detail screen with cover art and metadata. Song cards show the track name, artist, duration, and playback actions. Album cards show the album title, artist, and price.

### Audio playback
Preview audio is downloaded on demand and stored in the app’s private external files directory. The audio player provides a seek bar, current/end time labels, play/pause, and looping support.

### Favourites
Songs and albums can be added or removed from favourites directly from their detail views. The saved items are stored locally using `SharedPreferences`, so they remain available across app launches.

### Guess mode
The guess game plays preview clips and asks the user to identify the song title. The game tracks lives, progress, and correct answers, and it can be restarted once finished.

## Tech stack

- **Language**: Java
- **Platform**: Android SDK
- **UI**: Fragments, RecyclerView, Material Components, Bottom Navigation
- **Networking**: Volley
- **JSON parsing**: Gson
- **Image loading**: Picasso
- **Media**: MediaPlayer
- **Local storage**: SharedPreferences

## Requirements

- Android 8.0+ (`minSdk 26`)
- Internet access for API requests and preview downloads

## Notes

- The app uses the iTunes Search API for both songs and albums.
- Preview audio is cached locally in the app storage.
- Favourites are stored on-device and are not synced to a remote backend.