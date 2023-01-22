# ToDoMap
An map-based app with to do list to manage your life easily.

### Introduction
Have you ever experienced that when you have a lot of tasks to do but you don’t know which one to start and how to plan your daily journey? Have you ever 
experienced that when you want to search the location of your next work place but you have to switch different apps and copy paste many times? Here is why 
our ToDoMap comes out. This application aims to help users manage their work and life easily by visualizing todo tasks on a map.

This app is the course project of the <Mobile Cartography> course within the framework of Cartography M.Sc. Programme.

### The requirements to be implemented:
- [ ] use of different Views and ViewGroups including a ListView or RecyclerView
- [ ] use of Intents
- [ ] integration of a map with an own map style
- [ ] use of GPS
- [ ] use of a local SQLite database
- [ ] design and integration of an own launcher icon

### Further additions to be implemented:
- [ ] customized design
- [ ] integration of images
- [ ] multi-language

#### Google Map API

#### Development Log

- branch dev_sql
  - SQLite 
    - DatabaseHelper // Create, Update table (only one table now)
    - DBManager // CRUD
    - Task table scheme (Id, title, type, time, address, lat, lon, desc, status), type needs update
  - TaskView
    - TaskFragment // main task view
    - AddTaskActivity // add task btn from top-right menu, and pop up add task window
    - ModifyActivity // update or delete task window
    - Popup window
    - Todo: improve interface
  - Bottom Navigation Bar
    - Icon ./app/res/drawable/....
    - Task fragment and Map fragment
- branch dev_latlon
  - todo: 
    - address -> latlon (Geocoder)
    - database type limitation (Id Int, title String, type String, time String, address String, lat Double, lon Double, desc String, status Int)