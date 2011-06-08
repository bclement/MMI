
MainGui is the entry point for the program that's used during the studies.

CrudFrame is the entry point for the CRUD program Brian asked for.

5/25/11 Payne@MattPayne.org changed example session file so it has paths that are absolute paths.   
The paths originally started out 
with paths that were relative to the base directory of the maven project.


TODO: 
1) DONE: MainGui should size to the biggest image
   a) Logic about writing CSV file when ending the session early.
   b) Always append to the CSV file.
   c) Use a file chooser to save the CSV file.
   d) Think about using config files in the home directory to remember things
2) CrudFrame should populate the images and audio files in a session.  And 
   a) save back to the session.txt and 
   b) create new session.txt and 
   c) create a new directory tree...
   d) JSplitPane in CrudFrame
   e) Examplar group and name inputs
3) The menus should have shortcuts where needed
4) Clean all TODOs from code
5) JNLP the whole thing and make it so we can load session.zip files from HTTP
6) Build some sample datasets while testing on Windows boxes in the UNOmaha.edu user room
