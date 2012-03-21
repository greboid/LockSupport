Windows session lock listener
=============================

Simple library to add support for listening to windows session lock/unlock events.

Dependencies
------------
- [JNA](https://github.com/twall/jna "JNA")
- [Lombok-PG](https://github.com/peichhorn/lombok-pg "Lombok-PG")


Usage
-----

1. Create an instance of LockAdapter passing in your Java window.
2. Add a LockListener to the LockAdapter instance.
3. Handle lock events.
