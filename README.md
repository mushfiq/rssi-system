rssi-system
===========
**RSSI based indoor localization project.**

[![GitHub version](https://badge.fury.io/gh/mushfiq%2Frssi-system.png)](http://badge.fury.io/gh/mushfiq%2Frssi-system)

This  project contains following applications.
 - Java Application consists of Algortihm and (comReadeer) 
 - Python, Django based REST API (RESTApi)
 - An android application that display latest position of the   
   (AndroidApp)


![Watchm receivers and transmitters][1]

**Summary**: To localize objects or people inside a building (Indoor Localization) several devices are needed. In this project an approach with RSSI Receivers is used. There are several RSSI receivers (Texas Instruments RFCC1110-868) placed inside a room. A watch (Texas Instruments ez430), when inside the room, emits signal of constant strength. Receivers register the signal strength from the watch (the stronger the signal, the closer the watch is) and send this information to the main hub (Chipcon). The java application extract the RSSI information and feeds value to Algorithm, where Algoritms returns x,y value from RSSI values. Then the data is sent to the server via REST API, with some additional data.
And the REST API saves the data into MongoDB and serves data to the clients.


  [1]: https://lh4.googleusercontent.com/-cLSu1_u8qb8/UvQBVbciJ3I/AAAAAAAAAHw/sXxCT16xvUw/s0/Screen+Shot+2014-02-06+at+10.39.58+PM.png "Screen Shot 2014-02-06 at 10.39.58 PM.png"