# An-Asynchronous-Language-Detection-System
4 Year Advanced Object Oriented 

I was required to implement a web-based services capable of identifying the language
classification of a submitted body of text.

## Application Overview.

This application covers the minimum requirements outlined in the project specification.
The Users should be able to specify some text in a web form that, when submitted, is placed in a message queue. The web clients should periodically poll
the web server with a Job# to ask if the language detection task has been completed.
The application is build with scalability and extensibility in mind. 
Each request is processed asynchronously and it is easy to create a new type of request.

Each request is handled by servlet AsyncContext ,the servlet allocates thread from container pool
and processes it separately allowing servlet accepts other requests this way we can use a few threads to handle busy traffic,because request threads don't 
have to wait for expensive work like database processing.And when it finished it dispatches response back to client or another servlet.

For the database initialization i used init method in servlet with thread pool.Around 50 threads parsed the words into kmers.

To handle encoding in UTF-8 i used filter ,which runs before servlet by setting up unicode format on request and response as well.

I used 2 BlockingQueues for processing requests and text from database,because they are blocking it is thread safe to use them on multithreaded environment.

Design Patterns.

1 Singleton.
The singleton pattern was used for the classes that manage the thread pool and database. The reason for this is that we do not want more than one thread pool or database to be created.

2 Facade pattern.
Facade pattern hides the complexities of the system and provides an interface to the user. This type of design pattern comes under structural pattern as this pattern adds an interface to existing system to hide its complexities.
This pattern involves a single class which provides simplified methods required by client and delegates calls to methods of existing system classes.


