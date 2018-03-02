## OOD Final Project

#### Members:
 - Danny Burer
 - Deon Sanchez
 - Brett Mitchell
 - "Champ"
 
#### Task list:

Basically a grab bag of stuff I put together so we can split up the workload. Pick anything you like, but try and coordinate with the others so that nobody's working on the same thing. When done, add your name to the entry and mark it as complete.

**Example:**

- Before:
  - [x] Do a thing, and make it snappy. (Deon)
- After:
  - [x] Do a thing, and make it snappy. (Danny)
  
**The list:**

- [x] Implement a `Message` class. (Danny)
  - Needs to have various decorator classes
  - Serializable such that it can be passed from client to server to client
  - After received by the client, can be instantiated with the appropriate decorator and displayed.
- [x] Implement a "room" system. (Deon)
  - When clients join, have them either put in a code or select from a list of open rooms to join
- [x] Implement ability for a client to enter a name upon connection (this will likely be easy to do incorporate with the `Message` class task) (Danny)
  - Display name in chat when message is broadcast
- [x] GUI upgrades (Deon)
  - Make the interface in general more robust
  - Improve responsiveness, add interface components for to-be-implemented features
- [x] Documentation (Deon)
  - Document classes and methods, with parameter descriptions
- [x] General clean-up (Deon)
  - Clean up unneeded or unused code, take `TODO` comments and create an entry in this list for them instead.
- [x] Broadcast a notification when a user joins a room (Danny)
- [x] Show a list of currently connected users (Deon)
  - Periodically reconcile or "sync" with server
- [x] Style the client's message differently, to distinguish the user's messages from the rest of the room (Deon)
 
