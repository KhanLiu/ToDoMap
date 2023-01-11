# ToDoMap
An map-based app with to do list to manage your life easily.

### Introduction
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
MAPS_API_KEY=AIzaSyCCxIZOTd3MpxLhMQAVU4RJjcXyX8Iztcg

#### Development Log

- branch dev_sql
  - SQLite 
    - DatabaseHelper // Create, Update table (only one table now)
    - DBManager // CRUD
    - Todo: table scheme
  - TaskView
    - TaskViewActivity // main task view
    - AddTaskActivity // add task btn from top-right menu, and pop up add task window (should be popup)
    - ModifyActivity // update or delete task window (should be popup)
    - Todo: implement popup window, modify interface