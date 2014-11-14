eecs4413c
=========

Setting up the project:

A few things need to be hooked up before your project can be used in your respective IDE. In particular, ensure that
the following statements are true for you:
    - /src/ should be marked as the root for Java source files
    - /build/ should be marked as the root for compiled data -- if not, it is crucial that you do not commit your IDE's
       equivalent of the build folder.
    - /WebContent/ should be marked as the root for all web files (IntelliJ defaults to /web/ and this will cause issues
       with displaying files as various components will be missing from the server on startup).


The project has files configured such that projects should be reading and writing to the same folder regardless of
whether or not you are using IntelliJ or eclipse. As such there are project configuration files for both IDEs. The
configuration files when not read by your IDE serve no purpose and act as plaintext files that are never read.

However! Do not remove these files as you may accidentally push those changes to everyone else on subsequent commits!