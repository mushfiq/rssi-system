#This file contains sample API call based on UNIX cURL
-------------------------------------------------------------------------------------------------------------------------------------------------------
#POST WATCH EXAMPLE:

curl -i -H "Content-Type: application/json" -X POST -d '{"mapId": 2, "watchId": "12", "x": 12.3, "y": 3.4}'  http://localhost:8000/api/v1/watch/
#POST MAP EXAMPLE:

curl -i -H "Content-Type: application/json" -X POST -d '{"mapId": 2, "receiverId": "12", "width": 12, "height": 3, "scaling": 1.5, "offsetX":23, "offsetY": 31}'  http://localhost:8000/api/v1/map/


curl  -H "Content-Type: image/png"  -d '{"mapId": 0, "receiverId": "0", "x": 0.3, "y": 0.3}' -F "image=@sample.png" http://localhost:8000/api/v1/map/?access_key=5298fe82fb21ab1b8b72533

#POST RECEIVER EXAMPLE:

curl -i -H "Content-Type: application/json" -X POST -d '{"receiverId": 2, "mapId": "13", "x": 12.3, "y": 3.4}'  http://localhost:8000/api/v1/receiver/


______________________________________________________________________________________________________________________________________________________

#PUT RECEIVER EXAMPLE:

curl -i -H "Content-Type: application/json" -X PUT -d '{"mapId": 0, "receiverId": "0", "x": 0.3, "y": 0.3}' http://localhost:8000/api/v1/receiver/52988ae6fb21ab0abf74409d/

#PUT WATCH EXAMPLE:

curl -i -H "Content-Type: application/json" -X PUT -d '{"mapId": 0, "watchId": "03", "x": 0.36, "y": 0.53}' http://localhost:8000/api/v1/watch/52910635fb21ab1342722e3a/

#PUT MAP EXAMPLE:

curl -i -H "Content-Type: application/json" -X PUT -d '{"mapId": 0,"receiverId":"13", "width": 12, "height": 420, "scaling": 2.53 , "offsetX":1, "offsetY": 11}' http://localhost:8000/api/v1/map/52988921fb21ab0a25d35bf0/


-------------------------------------------------------------------------------------------------------------------------------------------------------

#DELETE RECEIVER EXAMPLE:

curl -i -H "Content-Type: application/json" -X DELETE http://localhost:8000/api/v1/receiver/52988ae6fb21ab0abf74409f/

-------------------------------------------------------------------------------------------------------------------------------------------------------

#USER AUTH EXAMPLE:

 curl  -d "username=name&password=word"  http://localhost:8000/api/v1/authenticate/


