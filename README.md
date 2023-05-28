![](https://s8.uupload.ir/files/untitled_6r4l.png)





			# 						EIGHTH ASSIGNMENT REPORT

			# 			Mohammad Hossein Basouli    401222020

















## Introduction:

- ### A brief description of the program:

  - This program will simulate an popular, widely-used application for playing and downloading games called **Steam**.

- ### Objectives of the program:

  1. Let clients to **login**, **signup**.
  2. Providing a list of games that are available in the app.
  3. Let clients to **download game files**. (A **.png** file for simplicity)
  4. Provide a table for each one of the users to **manage their downloads**.

- ### A high-level overview of approach:

  - When a client connects to the server we simply assign an independent thread to provide service to it. Such as **listening**, **sending png file** and so on.
  - Each time user wants to do something we send a **Json** over the socket to tell our request to server.
  - These **Json's** will be handled by each of the client's service providers on server side.
  - In return server will send a **Json** over the socket or for example a sequence of bytes that can build up an image.



## Design and implementation:

- ### Description on design of the solution:

  - **ClientHandler**: When an client connects to the server main method of **ClientHandler** will get executed. After that user is prompted to enter some information to walk through the menu option and at the end of each one of them some data will be putted in a **Json Object** to be sent over the socket. After that clients waits a while and then receives the desired answer.
  - **Request**: Provides some static methods to send a well-formatted request to server.
  - **ServerMain**: The first thing that has to get started is **ServerMain**. After that the server establishes connection on the specified PORT (8888), any client can connect to that and will be assign a thread to provide service to it.
  - **SteamService**: This class obligates to listen to it's client and do service for it based on the it's request. After taking request from client it may want to query to **Database** to access some data and then will return back the response using a **Json**.
  - **Response**: Provides some static methods to send a well-formatted response to client.

- ### Libraries:

  - **UUID**: To uniquely identify some objects like Account.
  - **Socket**: To be able to communicate with client and also the server.
  - **Gson**: Is used to create well-formatted and well-structured request and answers between client and server.
  - **SQL**: Used to connect to databases and write and execute queries to them.

