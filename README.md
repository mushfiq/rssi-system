rssi-system
===========
**RSSI based indoor localization project.**

This  project contains following applications.
 - Java Application consists of Algortihm and (comReadeer) 
 - Python, Django based REST API (RESTApi)
 - An android application that display latest position of the   
   (AndroidApp)

**Summary**: To localize objects or people inside a building (Indoor Localization) several devices are needed. In this project an approach with RSSI Receivers is used. There are several RSSI receivers (Texas Instruments RFCC1110-868) placed inside a room. A watch (Texas Instruments ez430), when inside the room, emits signal of constant strength. Receivers register the signal strength from the watch (the stronger the signal, the closer the watch is) and send this information to the main hub (Chipcon). The java application extract the RSSI information and feeds value to Algorithm, where Algoritms returns x,y value from RSSI values. Then the data is sent to the server via REST API, with some additional data.
And the REST API saves the data into MongoDB and serves data to the clients.
