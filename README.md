# ToDoMap

An map-based app with to do list to manage your life easily.

Group Member: Jiapan Wang, Zihan Liu

## 1. Introduction

Have you ever experienced that when you have a lot of tasks to do but you don’t know which one to start and how to plan your daily journey? Have you ever experienced that when you want to search the location of your next work place but you have to switch different apps and copy paste many times? Here is why our **ToDoMap** comes out. This application aims to help users manage their work and life easily by visualizing todo tasks on a map.

This app is the course project of the **Mobile Cartography** course within the framework of [Cartography M.Sc.](https://cartographymaster.eu/) Programme.

## 2. Target Groups

**ToDoMap** is an app for all user groups. We provide seven types of task (all :clipboard:, work :briefcase:, study :book:, life :house:, travel :airplane:, other :globe_with_meridians: and done :heavy_check_mark:) which can fulfill most of demands of users.

Not only a todo management app **ToDoMap** is, but also a life record app it could be.

- If you are studying or working, you can use our app to manage your daily study, work, life, and other stuff.

- If you are doing a travel plan, you can use our app to mark the places you want to go beforehand or record the places you have been to afterwards.

- If you are a life record lover, you can use our app to record your daily life and post it at anyplace you want!

Give full play to your imagination to use **ToDoMap**, combine your daily life with maps, and make your life better!

## 3. Main Features

#### 3.1 Task List

- Add a new task 
- Update your task
- Mark your task as done
- Delete your task

#### 3.2 Task Map

- Display all of tasks on the map
- Create a new task from map (by clicking or searching)
- Navigate to your next task

#### 3.3 Custom Style

- Change app mode (Day or Night)
- Change basemap style
- Change navigation mode

#### 3.4 Auxiliary

- App tutorial
- About us


## 4. Development

#### 4.1 Task SQLite Database

Here is the structure and datatype of SQLite database, which will be created locally on the device when user firstly install **ToDoMap**.

| _id                               | title         | type | time | address | latitude | longitude | description |
|-----------------------------------|---------------|------|------|---------|----------|-----------|-------------|
| INTEGER PRIMARY KEY AUTOINCREMENT | TEXT NOT NULL | TEXT | TEXT | TEXT    | DOUBLE   | DOUBLE    | TEXT        |

After tasks created, the data in database looks like:

![database](./img/database_local.png)

#### 4.2 Fragment-based Bottom Navigation

#### 4.3 Google Map Integration

#### 4.3 Custom Design

#### 4.4 Further Development

## 5. Contribution

| Assignment      | Contributor   |
|-----------------|---------------|
| Database        | Jiapan        |
| App framework   | Jiapan, Zihan |
| Add/Modify task | Jiapan, Zihan |
| Map view        | Zihan         |
| Task view       | Jiapan        |
| Settings view   | Jiapan, Zihan |
| Icon design     | Zihan         |
| Map style       | Zihan         |
| App style       | Jiapan        |

<!-- 
### The requirements to be implemented

- [ ] use of different Views and ViewGroups including a ListView or RecyclerView
- [ ] use of Intents
- [ ] integration of a map with an own map style
- [ ] use of GPS
- [ ] use of a local SQLite database
- [ ] design and integration of an own launcher icon

### Further additions to be implemented

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
  - todo
    - address -> latlon (Geocoder)
    - database type limitation (Id Int, title String, type String, time String, address String, lat Double, lon Double, desc String, status Int) -->