Gradle Plugins Dev Playground
========================================

A project to play around with gradle __plugin development__. Shows a various tips and tricks on how to tackle 
obstacles for test and implementation best practices, due to the way gradle world works. 

> __EXAMPLE__: The dependency injection framework that gradle uses is __NOT__ public to the gradle plugin
developer. Therefore stubbing and mocking is next to impossible and crazy workarounds have to be applied.


Release notes
----------------------------------------
### Version 1.0.0
* A very simple wrapper around the `GradleRunner` test, used for Integration testing.
The wrapper aims at helping you create and configure a gradle project to test with.
* Example of a test in isolation of plugin that makes external HTTP calls. `JVM proxy settings` used.