# Watchr - TDA367 Objektorienterat Programmeringsprojekt

[![Build Status](https://travis-ci.org/ITJohan/tda367-objektorienterat-programmeringsprojekt.svg?branch=development)](https://travis-ci.org/ITJohan/tda367-objektorienterat-programmeringsprojekt)

## Introduction

This is an movie browser android application developed during a 36 day period for the course TDA367. There were in total three people working on this, with only one having previous experience with android development.  
It was developed with the use of a scrum workflow and according to the [Jetpack](https://developer.android.com/jetpack) guidelines (might have changed since it was developed).

### Video preview

[![Watchr preview](https://img.youtube.com/vi/lGk2oZdj1MQ/0.jpg)](http://www.youtube.com/watch?v=lGk2oZdj1MQ)

## Features

- Information about movies are fetched from [themoviedb](https://www.themoviedb.org/), so it's always up to date
- Comments and Profile pics are updated in real time. (If someone comments on another device it'll be shown directly without needing to refresh)
- Infinite scroll. (movie entries are loaded as you scroll)
- All the movie information is cached in a local SQLite database using [Room](https://developer.android.com/topic/libraries/architecture/room). (prevents excessive data usage)
- Registration email & password resets are available. (see video for registration demo)
- Everything related to your account such as comments, movie lists, and movie stats is synced to our own database in [Firebase](https://firebase.google.com/).

## Technical Details

- [Software Design Description document](SDD.pdf)
- [Requirements and Analysis documents](RAD.pdf)
