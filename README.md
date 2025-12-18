## Welcome to Event World

Event world is a school project Spring Boot application that runs on a microservice architecture. It is built from a learning perspective and is not to be used as a working or secure application. There are surely many bugs to be encountered.

#### Setup

In order to get started you need to have both Maven and Docker installed. In the _scripts_ folder you can find scripts to install and run all parts of the application. It is a tedious process requiring many terminal windows as the application isnt dockerized, we apologize for that.

1. Make sure Docker is running!
   &nbsp;
2. Alter the executable permissions for all shell (_.sh_) files in the _scripts_ folder:

```
chmod u+x <--filename.sh-->
```

3. Open 4 terminals and navigate to the _scripts_ folder in all of them.
   &nbsp;
4. Run each of the following scripts in a separate terminal window in the exact order given below!

   1. setup.sh then immediatly runuser.sh in the same terminal!
   2. runevent.sh
   3. runbooking.sh
   4. frontend.sh

   _If the setup script complains that a docker image already exists, either delete it or start the container._
