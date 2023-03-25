---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

--------------------------------------------------------------------------------------------------------------------

## **Acknowledgements**

* {list here sources of all reused/adapted ideas, code, documentation, and third-party libraries -- include links to the original source as well}

--------------------------------------------------------------------------------------------------------------------

## **Setting up, getting started**

Refer to the guide [_Setting up and getting started_](SettingUp.md).

--------------------------------------------------------------------------------------------------------------------

## **Design**

<div markdown="span" class="alert alert-primary">

:bulb: **Tip:** The `.puml` files used to create diagrams in this document can be found in the [diagrams](https://github.com/se-edu/addressbook-level3/tree/master/docs/diagrams/) folder. Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) to learn how to create and edit diagrams.
</div>

### Architecture

<img src="images/ArchitectureDiagram.png" width="280" />

The ***Architecture Diagram*** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`](https://github.com/AY2223S2-CS2103T-T15-2/tp/blob/master/src/main/java/seedu/fitbook/Main.java) and [`MainApp`](https://github.com/AY2223S2-CS2103T-T15-2/tp/blob/master/src/main/java/seedu/fitbook/MainApp.java). It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`FitBookModel`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.


**How the architecture components interact with each other**

The *Sequence Diagram* below shows how the components interact with each other for the scenario where the user issues the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its *API* in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding API `interface` mentioned in the previous point.

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component through its interface rather than the concrete class (reason: to prevent outside component's being coupled to the implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

The sections below give more details of each component.

### UI component

The **API** of this component is specified in [`Ui.java`](https://github.com/AY2223S2-CS2103T-T15-2/tp/blob/master/src/main/java/seedu/fitbook/ui/Ui.java)

![Structure of the UI Component](images/UiClassDiagram.png)

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `ClientListPanel`, `StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. For example, the layout of the [`MainWindow`](https://github.com/AY2223S2-CS2103T-T15-2/tp/blob/master/src/main/java/seedu/fitbook/ui/MainWindow.java) is specified in [`MainWindow.fxml`](https://github.com/AY2223S2-CS2103T-T15-2/tp/blob/master/src/main/resources/view/MainWindow.fxml)

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `FitBookModel` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `FitBookModel` component, as it displays `Client` object residing in the `FitBookModel`.

### Logic component

**API** : [`Logic.java`](https://github.com/AY2223S2-CS2103T-T15-2/tp/blob/master/src/main/java/seedu/fitbook/logic/Logic.java)

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `FitBookParser` class to parse the user command.
2. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
3. The command can communicate with the `FitBookModel` when it is executed (e.g. to add a client).
4. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600"/>

How the parsing works:
* When called upon to parse a user command, the `FitBookParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `FitBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

### FitBookModel component
**API** : [`FitBookModel.java`](https://github.com/AY2223S2-CS2103T-T15-2/tp/blob/master/src/main/java/seedu/fitbook/model/FitBookModel.java)

<img src="images/FitBookModelClassDiagram.png" width="450" />


The `FitBookModel` component,

* stores the FitBook data i.e., all `Client` objects (which are contained in a `UniqueClientList` object).
* stores the currently 'selected' `Client` objects (e.g., results of a search query) as a separate _filtered_ list which is exposed to outsiders as an unmodifiable `ObservableList<Client>` that can be 'observed' e.g. the UI can be bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `FitBookModel` represents data entities of the domain, they should make sense on their own without depending on other components)

<div markdown="span" class="alert alert-info">:information_source: **Note:** An alternative (arguably, a more OOP) model is given below. It has a `Tag` list in the `FitBook`, which `Client` references. This allows `FitBook` to only require one `Tag` object per unique tag, instead of each `Client` needing their own `Tag` objects.<br>

<img src="images/BetterFitBookModelClassDiagram.png" width="450" />

</div>


### Storage component

**API** : [`Storage.java`](https://github.com/AY2223S2-CS2103T-T15-2/tp/blob/master/src/main/java/seedu/fitbook/storage/FitBookStorage.java)

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both FitBook data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `FitBookStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the functionality of only one is needed).
* depends on some classes in the `FitBookModel` component (because the `Storage` component's job is to save/retrieve objects that belong to the `FitBookModel`)

### Common classes

Classes used by multiple components are in the `seedu.fitbook.commons` package.

--------------------------------------------------------------------------------------------------------------------

## **Implementation**

This section describes some noteworthy details on how certain features are implemented.

### Edit appointments feature
#### Implementation
The edit appointments feature allows users to view appointments in the upcoming dates.

This feature is implemented using a panel on the main window with a list of clients names 
that is updated with every command that may affect appointment set.

One situation that trigger the edit appointments feature is when a suer edits an appointment.
The following details explain how it works:

   *  What it does:
      * When an appointment is edited in the clients list, it is automatically added to the
      list of appointments.
      * The list is automatically sorted in increasing order of the appointment data time.
      

   * Details:
      * When the user enters the edit appointment command, it triggers the creation of an object the *Appointment* 
      class.
      * In the *EditCommand* class, the data entered by user is parsed.
      * If there is no error, the Appointment object is created which triggers the getAppointment() function in Model.
      * This function, in turn, calls editCommand() function in FitBook.
      * These functions call isValidDate() and isValidAppointment() functions in Appointment to confirm whether the 
      appointment date time are valid.
      * If the appointment date and time are valid, they are added to the appointment list, which is then sorted. 
      Otherwise, an error message is returned.


   * Example Usage Scenario

     Below is an example usage scenario of how the appointment list mechanism behaves at each step:
       * The user launches the application for the first time.
       * The user executes the Edit index app/ command to edit an appointment. The execution of the Edit index app/ command also 
       checks whether this appointment is valid in the appointment list. If it is, the appointment is added to the appointment list. Otherwise, an error is displayed.


  * Design Considerations

    One important design consideration is how to handle expired appointment dates and times. The current choice is to 
    automatically remove them after reopening the app and to display a gray card for the expired appointment date and time.
      * pros: Users can easily distinguish between expired and non-expired appointment dates and times. 
      * cons: expired date time cannot be updated immediately unless the user reopen the application.

### Find feature

#### Implementation

The proposed find mechanism is facilitated by `FitBook`. It implements the following operations:

* `FitBook#getFilteredClientList()` — Retrieves the client list.
* `FitBook#updateFilteredClientList(Predicate<Client> predicate)` — Filters the client list with the given predicate.

These operations are exposed in the  `FitBookModel` interface as `FitBookModel#getFilteredClientList()`, `FitBookModel#updateFilteredClientList(Predicate<Client> predicate)` respectively.

Given below is an example usage scenario and how the find mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `FitBook` will be initialized with the initial
FitBook state, and the `currentStatePointer` pointing to that single FitBook state.

![FindState0](images/FindState0.png)

Step 2. The user executes `find n/alex n/john` command to find all clients with "alex" or "john" in their name in the
FitBook. The `find` command calls `FitBookModel#updateFilteredClientList(Predicate<Client> predicate)`, causing the
modified state of the FitBook after the `find n/alex` command executes to be saved in the `fitBookStateList`, and the
`currentStatePointer` is shifted to the newly inserted FitBook state.

![FindState1](images/FindState1.png)

Step 3. The user now decides that he does not need to find the details of the client named "John". The user executes
`find n/alex`, causing another the current FitBook state to be deleted, and a new FitBook state added into the
`fitBookStateList`.

![FindState2](images/FindState2.png)

![FindState3](images/FindState3.png)

Step 4. The user now needs to view all of his clients' details again. The user executes `listClients` which will shift
the `currentStatePointer` to the first FitBook state, and restores the FitBook to that state.

![FindState3](images/FindState4.png)

The following sequence diagram shows how the find operation works:

![FindSequenceDiagram](images/FindSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `FindCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/FindActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How find executes more than once:**

* **Alternative 1 (current choice):** Finds from the entire FitBook.
    * Pros: Easy to implement.
    * Cons: Lower performance as every command will have to filter the entire FitBook.

* **Alternative 2:** Finds from an already filtered list.
    * Pros: Better performance.
    * Cons: May result in high memory usage as each new state has to be saved.


### \[Proposed\] Undo/redo feature

#### Proposed Implementation

The proposed undo/redo mechanism is facilitated by `VersionedFitBook`. It extends `FitBook` with an undo/redo history, stored internally as an `fitBookStateList` and `currentStatePointer`. Additionally, it implements the following operations:

* `VersionedFitBook#commit()` — Saves the current FitBook state in its history.
* `VersionedFitBook#undo()` — Restores the previous FitBook state from its history.
* `VersionedFitBook#redo()` — Restores a previously undone FitBook state from its history.

These operations are exposed in the `FitBookModel` interface as `FitBookModel#commitFitBook()`, `FitBookModel#undoFitBook()` and `FitBookModel#redoFitBook()` respectively.

Given below is an example usage scenario and how the undo/redo mechanism behaves at each step.

Step 1. The user launches the application for the first time. The `VersionedFitBook` will be initialized with the initial FitBook state, and the `currentStatePointer` pointing to that single FitBook state.

![UndoRedoState0](images/UndoRedoState0.png)

Step 2. The user executes `delete 5` command to delete the 5th client in the FitBook. The `delete` command calls `FitBookModel#commitFitBook()`, causing the modified state of the FitBook after the `delete 5` command executes to be saved in the `fitBookStateList`, and the `currentStatePointer` is shifted to the newly inserted FitBook state.

![UndoRedoState1](images/UndoRedoState1.png)

Step 3. The user executes `add n/David …​` to add a new client. The `add` command also calls `FitBookModel#commitFitBook()`, causing another modified FitBook state to be saved into the `fitBookStateList`.

![UndoRedoState2](images/UndoRedoState2.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If a command fails its execution, it will not call `FitBookModel#commitFitBook()`, so the FitBook state will not be saved into the `fitBookStateList`.

</div>

Step 4. The user now decides that adding the client was a mistake, and decides to undo that action by executing the `undo` command. The `undo` command will call `FitBookModel#undoFitBook()`, which will shift the `currentStatePointer` once to the left, pointing it to the previous FitBook state, and restores the FitBook to that state.

![UndoRedoState3](images/UndoRedoState3.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index 0, pointing to the initial FitBook state, then there are no previous FitBook states to restore. The `undo` command uses `FitBookModel#canUndoFitBook()` to check if this is the case. If so, it will return an error to the user rather
than attempting to perform the undo.

</div>

The following sequence diagram shows how the undo operation works:

![UndoSequenceDiagram](images/UndoSequenceDiagram.png)

<div markdown="span" class="alert alert-info">:information_source: **Note:** The lifeline for `UndoCommand` should end at the destroy marker (X) but due to a limitation of PlantUML, the lifeline reaches the end of diagram.

</div>

The `redo` command does the opposite — it calls `FitBookModel#redoFitBook()`, which shifts the `currentStatePointer` once to the right, pointing to the previously undone state, and restores the FitBook to that state.

<div markdown="span" class="alert alert-info">:information_source: **Note:** If the `currentStatePointer` is at index `fitBookStateList.size() - 1`, pointing to the latest FitBook state, then there are no undone FitBook states to restore. The `redo` command uses `FitBookModel#canRedoFitBook()` to check if this is the case. If so, it will return an error to the user rather than attempting to perform the redo.

</div>

Step 5. The user then decides to execute the command `list`. Commands that do not modify the FitBook, such as `list`, will usually not call `FitBookModel#commitFitBook()`, `FitBookModel#undoFitBook()` or `FitBookModel#redoFitBook()`. Thus, the `fitBookStateList` remains unchanged.

![UndoRedoState4](images/UndoRedoState4.png)

Step 6. The user executes `clear`, which calls `FitBookModel#commitFitBook()`. Since the `currentStatePointer` is not pointing at the end of the `fitBookStateList`, all FitBook states after the `currentStatePointer` will be purged. Reason: It no longer makes sense to redo the `add n/David …​` command. This is the behavior that most modern desktop applications follow.

![UndoRedoState5](images/UndoRedoState5.png)

The following activity diagram summarizes what happens when a user executes a new command:

<img src="images/CommitActivityDiagram.png" width="250" />

#### Design considerations:

**Aspect: How undo & redo executes:**

* **Alternative 1 (current choice):** Saves the entire FitBook.
  * Pros: Easy to implement.
  * Cons: May have performance issues in terms of memory usage.

* **Alternative 2:** Individual command knows how to undo/redo by
  itself.
  * Pros: Will use less memory (e.g. for `delete`, just save the client being deleted).
  * Cons: We must ensure that the implementation of each individual command are correct.

_{more aspects and alternatives to be added}_

### \[Proposed\] Data archiving

_{Explain here how the data archiving feature will be implemented}_


--------------------------------------------------------------------------------------------------------------------

## **Documentation, logging, testing, configuration, dev-ops**

* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Requirements**

### Product scope

**Target user profile**:
* has a need to manage a significant number of clients
* has a need to manage details of each client
* has a need to store exercises routines for each client
* has a need to mark done or not done for each exercises
* prefers desktop apps over other types
* can type fast
* prefers typing to mouse interactions
* is reasonably comfortable using CLI apps

**Value proposition**: Fitness trainers tend to write all their clients’ fitness checklist, weight and progress with
targets in a notebook. Keeping track of so many clients’ routines and progress on a notebook can be tough and
inefficient. So, our product aims to help solve this by combining the addressbook with additional features such as
adding fitness routines to each client with checkboxes for easy access and to check if they have completed their
routines during their individual sessions. The trainer can also find the clients’ progress such as weight and their
appointment dates and times they have with their clients.

---

### User stories

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`


=======
| Priority | As a …​           | I can …​                                                             | So that …​                                                     |
|----------|-------------------|----------------------------------------------------------------------|------------------------------------------------------------------------|
| `* * *`  | new user          | look at the list of clients                                          | I know who are my clients and their details                            |
| `* * *`  | new user          | edit my client's routine                                             | the client's routine data is accurate                                  |
| `* * *`  | new user          | edit my client's current routine                                     | so that the client's calorie intake is accurate                        |
| `* * *`  | new user          | find a person by name                                                | locate details of persons without having to go through the entire list |
| `* * *`  | new user          | hide private contact details                                         | minimize the chance of someone else seeing them by accident            |
| `* * *`  | new user          | edit my client's weight data                                         | the client's weight data is accurate                                   |
| `* * *`  | new user          | put my client's current weight                                       | I can see if they get closer to their targeted weight                  |
| `* * *`  | new user          | set goal for each of my clients                                      | I can help my clients to make the most suitable plan                   |
| `* * *`  | new user          | set a list of tasks for my client                                    | I can keep track of what i have asked my client to do                  |
| `* * *`  | new user          | sort persons by name                                                 | I can locate a person easily                                           |
| `* * *`  | new user          | search for clients through their names                               | I can find my client's data                                            |
| `* * *`  | new user          | save exercise routines in a list                                     | I can match them with each client                                      |
| `* * * ` | intermediate user | mark the exercise as incomplete                                      | I can manage my time properly next time                                |
| `* * *`  | intermediate user | mark the exercise as complete                                        | I can teach my clients new exercises                                   |
| `* * *`  | intermediate user | edit previous data                                                   | I can change my client's data                                          |
| `* * *`  | intermediate user | add time to a client's appointment                                   | I can easily view my appointments for the week                         |
| `* `     | intermediate user | search client's using their contact number                           | I can contact them when necessary                                      |
| `*`      | intermediate user | filter clients by gender or exercise level                           | I can find my client easily                                            |
| `*`      | Expert user       | link my clients under the same price package or classes              | I know who is under which class or price package                       |
| `* *`    | Expert user  | add clients into specific groups                                     | it will be easier to track if they are in the same session             |
| `* *`    | Expert user  | download data collected in FitBook                          | I can show results of my services to new potential clients             |
| `* *`    | Expert user  | view reminder messages                                               | I remember the session I have with my clients for the day              |
| `* *`    | Expert user  | add the time taken for each exercise for each session of the client  | I can achieve my target time for each exercise                         |



*{More to be added}*

---

### Use cases

(For all use cases below, the **System** is the `FitBook` and the **Actor** is the `user`, unless specified otherwise)

> **Use case: UC01 - Delete a client**

**MSS**

1.  User requests to list clients
2.  FitBook shows a list of clients
3.  User requests to delete a specific client in the list
4.  FitBook deletes the client

    Use case ends.

**Extensions**

* 2a. The list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. FitBook shows an error message.

      Use case resumes at step 2.

> **Use case: UC02 - Add a client**

**MSS**

1. User requests to add a client
2. FitBook adds the client into the list.
3. FitBook displays that the client has been added.

   Use case ends.

**Extensions**

* 1a. The client added has missing compulsory fields.

    * 1a1. FitBook shows an error message for missing fields.

      Use case ends.

* 1b. The client added has duplicate names.

    * 1b1. FitBook shows an error message for duplicate names.
       
       Use case ends.
    
>**Use case: UC03 - List clients**

**MSS**

1. User requests to list clients.
2. FitBook displays a list of clients.

   Use case ends.

**Extensions**

* 1a. Client list is empty use case.

  Use case ends.

> **Use case: UC04 - Edit a client**

**MSS**

1. User requests to edit a client's details.
2. FitBook edits the client's detail into the database.

   Use case ends.

**Extensions**

* 1a. The user enters the command incorrectly.

    * 1a1. FitBook shows an error message.

      Use case ends.

* 1b. The user enters a client that does not exist in the database.

    * 1b1. FitBook shows an error message that the client does not exist.

      Use case ends.

> **Use case: UC05 - Clear all clients**

**MSS**

1. User requests to clear the list of clients.
2. FitBook clears the list and database of clients.
3. FitBook displays that the list is cleared.

   Use case ends.

**Extensions**

* 1a. The list is empty in the database.

  * 1a1. FitBook displays that the list is cleared.

      Use case ends.

> **Use case: UC06 - Exit**

**MSS**

1. User requests to exit the application.
2. FitBook exits the program.

   Use case ends.

> **Use case: UC07 - Find**

**MSS**

1. User requests to find a client.
2. FitBook displays the list of matching clients.

   Use case ends.

**Extensions**

* 1a. The input does not match any client in the database.
    * 1a1. FitBook displays that there are no matches.

      Use case ends.

* 2a. The list is empty in the database.
    * 2a1. FitBook displays that there are no matches.

      Use case ends.

* 3a. The find command has incorrect format.
    * 3a1. FitBook displays an error that the find format is wrong.

      Use case ends.

> **Use case: UC08 - List Routines**

 **MSS**

1. User requests to list routines.
2. FitBook displays a list of routines.

   Use case ends.

**Extensions**

* 1a. Routine list is empty use case.

  Use case ends.

> **Use case: UC09 - Clear Routines**

**MSS**

1. User requests to clear the list of routines.
2. FitBook clears the list and database of routines.
3. FitBook displays that the routine list is cleared.

   Use case ends.

**Extensions**

* 1a. The routine list is empty in the database.

    * 1a1. FitBook displays that the routine list is cleared.

      Use case ends.

> **Use case: UC09 - Delete Routine**

**MSS**

1.  User requests to list routines
2.  FitBook shows a list of routines
3.  User requests to delete a specific routine in the list
4.  FitBook deletes the routine

    Use case ends.

**Extensions**

* 2a. The routine list is empty.

  Use case ends.

* 3a. The given index is invalid.

    * 3a1. FitBook shows an error message.

      Use case resumes at step 2.

> **Use case: UC10 - Delete Exercise**

**MSS**

1.  User requests to list routines
2.  FitBook shows a list of routines
3.  User requests to delete an exercise from a specific routine in the list
4.  FitBook deletes the exercise from the specific routine in the list

    Use case ends.

**Extensions**

* 2a. The routine list is empty.

  Use case ends.

* 3a. The given routine index is invalid.

    * 3a1. FitBook shows an error message.

      Use case resumes at step 2.
  
  3b. The given exercise index is invalid.

    * 3b1. FitBook shows an error message.
    
      Use case resumes at step 2.

> **Use case: UC11 - Find Routine**

**MSS**

1. User requests to find a routine by name.
2. FitBook displays the list of matching clients.

   Use case ends.

**Extensions**

* 1a. The list is empty in the database.
    * 1a1. FitBook displays that there are no matches.

      Use case ends.

> **Use case: UC13 - Export Client List**

**MSS**

1. User request to export Client List.
2. FitBook exports the Client List to csv format.
   
   Use case ends.

**Extensions**

* 1a. The Client csv file is opened in the background
    * 1a1. FitBook shows an error message.  
      
      Use case ends.

> **Use case: UC14 - Export Routine List**

**MSS**

1. User request to export Routine List.
2. FitBook exports the Routine List to csv format.

   Use case ends.

**Extensions**

* 1a. The Routine csv file is opened in the background
    * 1a1. FitBook shows an error message.

      Use case ends.

> **Use case: UC15 - Add routine**

**MSS**

1. User request to add a routine.
2. FitBook adds the routine to its routine list.

   Use case ends.

**Extensions**

* 1a. User request have missing routine name field.
    * 1a1. FitBook shows an error for missing routine name.
      
      Use case ends.

* 1b. User request have missing exercise field.
  * 1b1. FitBook adds a routine with no exercise. (Only routine itself)

    Use case ends.

* 1c. User request have all missing fields.
  * 1c1. FitBook shows an error for missing fields.

    Use case ends.

> **Use case: UC16 - Edit routine or exercise in routine**

**MSS**

1. User request to edit a routine.
2. User either edit by target routine's name or one of the target routine's exercise.
3. FitBook edits that routine in its routine list.

   Use case ends.

**Extensions**

* 2a. User request have mix fields which is not allowed.
    * 2a1. FitBook shows an error for incorrect command format.

      Use case ends.

* 2b. User request have all missing field.
    * 2b1. FitBook shows an error for missing fields

      Use case ends.

* 2c. User request have only target routine field.
    * 2c1. FitBook shows an error for missing field.

      Use case ends.

* 2d. User request for changing exercise only has one field. (Changing exercise requires two fields)
    * 2d1. FitBook shows an error for incorrect format.

      Use case ends.

*{More to be added}

---
### Non-Functional Requirements

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 clients without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. Should be usable by users even if they are not familiar with command lines.
5. Should start up fast when starting the application.
6. Should work on most screen resolutions.
7. Should not consume too much battery or data usage on the user's device.
8. Source code should be _open source_.
9. JAR file should be less than 2GB.

*{More to be added}*

---

### Glossary

* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **Private contact detail**: A contact detail that is not meant to be shared with others
* **Open source**: Programs that people can modify and share because its design is publicly accessible
* **Ui**: User interface of the program where the user interacts with

--------------------------------------------------------------------------------------------------------------------

## **Appendix: Instructions for manual testing**

Given below are instructions to test the app manually.

<div markdown="span" class="alert alert-info">:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.

</div>

### Launch and shutdown

1. Initial launch

   A. Download the jar file and copy into an empty folder

   B. Double-click the jar file Expected: Shows the GUI with a set of sample contacts. The window size may not be optimum.

2. Saving window preferences

   A. Resize the window to an optimum size. Move the window to a different location. Close the window.

   B. Re-launch the app by double-clicking the jar file.<br>
       Expected: The most recent window size and location is retained.

3. _{ more test cases …​ }_

### Deleting a client

1. Deleting a client while all clients are being shown

   A. Prerequisites: List all clients using the `list` command. Multiple clients in the list.

   B. Test case: `delete 1`<br>
      Expected: First contact is deleted from the list. Details of the deleted contact shown in the status message. Timestamp in the status bar is updated.

   C. Test case: `delete 0`<br>
      Expected: No client is deleted. Error details shown in the status message. Status bar remains the same.

   D. Other incorrect delete commands to try: `delete`, `delete x`, `...` (where x is larger than the list size)<br>
      Expected: Similar to previous.

### Deleting an exercise
1. Deleting an exercise while all routines are being shown

   A. Prerequisites: List all routines using the `listRoutines` command. Multiple routines with their respective set of exercises in the list.

   B. Test case: `deleteExercise 1 2`<br>
       Expected: Second exercise from the first routine is deleted from the list. Details of the deleted exercise shown in the status message.

   C. Test case: `deleteExercise 0 0`<br>
       Expected: No exercise is deleted. Error details shown in the status message.

   D. Other incorrect delete commands to try: `deleteExercise`, `delete x y`, (where x or y is larger than the list size and exercise list size respectively )<br>
       Expected: Similar to previous.
   
2. _{ more test cases …​ }_

### Saving data

1. Dealing with missing/corrupted data files

   A. _{explain how to simulate a missing/corrupted file, and the expected behavior}_

2. _{ more test cases …​ }_
