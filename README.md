# Pre-work - Just Do It!

Just Do It! is an android app that allows building a todo list and basic todo items management functionality including adding new items, editing and deleting an existing item.

Submitted by: John Paul Fernandez

Time spent: 15 hours spent in total

## User Stories

The following **required** functionality is completed:

* [x] User can **successfully add and remove items** from the todo list
* [x] User can **tap a todo item in the list and bring up an edit screen for the todo item** and then have any changes to the text reflected in the todo list.
* [x] User can **persist todo items** and retrieve them properly on app restart

The following **optional** features are implemented:

* [x] Persist the todo items [into SQLite](http://guides.codepath.com/android/Persisting-Data-to-the-Device#sqlite) instead of a text file
* [x] Improve style of the todo items in the list [using a custom adapter](http://guides.codepath.com/android/Using-an-ArrayAdapter-with-ListView)
* [x] Add support for completion due dates for todo items (To follow: display within listview item)
* [x] Use a [DialogFragment](http://guides.codepath.com/android/Using-DialogFragment) instead of new Activity for editing items
* [x] Add support for selecting the priority of each todo item (and display in listview item)
* [x] Tweak the style improving the UI / UX, play with colors, images or backgrounds

The following **additional** features are implemented:

* [x] Add support for selecting the status of each todo item (and display in listview item)
      [To-follow] Update item status in the database via checkbox in View

## Video Walkthrough 

Here's a walkthrough of implemented user stories:

<img src='http://i.imgur.com/By8MRBp.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).


## Project Analysis

As part of your pre-work submission, please reflect on the app and answer the following questions below:

**Question 1:** "What are your reactions to the Android app development platform so far? Compare and contrast Android's approach to layouts and user interfaces in past platforms you've used."

**Answer:** The Android platform is so interesting, not only for app development for smartphones, tablets and other consumer electronic devices like TV and car entertainment systems, but for more and more embedded segments and connected devices that are coming out. I've worked with JavaCard applets that run on SIM Card OS in the past, and I think that Android's approach to layouts and user interfaces is far more superior. While Android provides rich options for layout and user interface, the SIM platform can only support rudimentary UI to show up on the phone such as displaying text or reading input from the user. However, UI elements in JavaCard applets may be declared in XML or by instantiating them at runtime, which is somehow similar to Android's approach.

Also, in working with basic React web apps, so far I've seen how the framework brings easy and clean UI development, and how it can be useful for multiple mobile platforms as well with React Native. The Android platform works by using its native layout and user interface components, but I think React Native uses the same fundamental UI building blocks in Android or other platforms, though using JavaScript and React.

**Question 2:** "Take a moment to reflect on the `ArrayAdapter` used in your pre-work. How would you describe an adapter in this context and what is its function in Android? Why do you think the adapter is important? Explain the purpose of the `convertView` in the `getView` method of the `ArrayAdapter`."

**Answer:** `ArrayAdapter` is used in Android to convert data into View items, and it is important because it describes how to represent or visualize the data source into a corresponding View object. The adapter allocates the memory for each row, and it can function for View recycling as well. In this project, I used a custom `ArrayAdapter` to use a custom XML layout that represents the View template for each item. In `getView` method of the `ArrayAdapter`, `convertView` is the result of converting a custom XML layout to a Java object in memory and it serves as a template that can be reused or recycled by updating with new values.

## Notes

Describe any challenges encountered while building the app.
- Updating items using SQLite; retrieving item ID of the item to be updated
- Storing and retrieving dates from SQLite
- Updating item status in the database via checkbox in View (To-follow)

## License

    Copyright 2017 Fernandez, John Paul

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
