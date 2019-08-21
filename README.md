# Activity Tracker App

## Overview
The purpose of this app is to efficiently and precisely determine when the user is walking or running, find the borders of these activities and assign corresponding labels.

## The Idea
Using the step detector sensor, this app records the timestamps of each step. Depending on the difference between the timestamps of the new and the previous steps, the new step would either be added to the list and classified as "Walking", "Running", or "None". Consecutive steps having the same label will create an activity of that type.

## Features
* Capability to train the app for personal running / walking step intervals or use pre-set defaults
* Observe newly added and previous steps (Step Number, Label, Timestamp) under "By Steps" fragment
* Observe current and previous activities (Label, From Time, To Time, Number of Steps) under "Overview" fragment
* Save the data for steps and activities in .CSV files

## TODO
* Change all hard coded strings to "string" resources
* Improve the color scheme :-)

## Credits
Many elements of this app were build by adopting the approaches from the [Coding In Flow](https://codinginflow.com/) tutorials.

## Screenshots
![GitHub Logo](/screenshots/training_in_progress.png) ![GitHub Logo](/screenshots/training_results.png) ![GitHub Logo](/screenshots/by_steps.png) ![GitHub Logo](/screenshots/activities_overview.png) ![GitHub Logo](/screenshots/menu.png)
