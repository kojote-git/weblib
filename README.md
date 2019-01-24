## Weblib

The third part of my coursework. It provides web interface for end user (readers) to interact with digital library.
The main features of this module are signing up new accounts for readers with the ability to sign in later, searching books
using certain criteria (by authors, title, subject) in library and also the ability of a reader to rate these books. 

As user interacts with the system by rating the books, the information about rating is recorded and then may be used for any
purpose. In this coursework the automatization task is in making up a recommendation list for a user based on his/her preferences
and the task is implemented so that the algorithm of making up lists uses this information about ratings made by the user 
to actually make the list.

It depends on the [core module](https://github.com/kojote-git/coursework) and uses API of the
[library-server](https://github.com/kojote-git/library-server) module.
