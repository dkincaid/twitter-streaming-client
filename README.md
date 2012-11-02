twitter-client
==========
This is a very simple client for the Twitter streaming api. It will connect to the streaming api
filter call and save id, created_at and text for all of the received tweets to a given directory.

Building
------------
This project uses Maven as its build tool. In order to build the client the easiest thing to do is
to run
    mvn assembly:assembly
This will build an uber jar with all the dependencies in the `target` directory.

Setup
--------
You will need to create a twitter4j.properties file in the directory that you're going to launch the
client from with your Twitter API credentials. This client uses [Twitter4J](http://twitter4j.org)
library, so see the [configuration page](http://twitter4j.org/en/configuration.html) there for more details.

Usage
---------
Launch the client using the command
    java -jar twitter-client-0.1-with-dependencies.jar <search string> <directory>
it will launch the client and return tweets that match the search string. It will put JSON formatted
strings into files in the given directory. The client currently just saves the id, created_at and
text of the tweets.
