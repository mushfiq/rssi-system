import setup_django
import datetime
from random import randint, random
from  mongoengine import connect
from restApp.documents import mapRecords


connect('rssiSystem')

mPhoto = open('sampleMap2.jpg', 'r')

#curl -i -H "Content-Type: application/json" -X POST -d '{"mapId": 2, "watchId": "42", "x": 12.3, "y": 3.4}'  http://localhost:8000/api/v1/watch/

def saveMap():
    print "starting..."
    m = mapRecords()
    m.mapId = randint(0,5)
    m.width = randint(0, 200)
    m.height = randint(0, 200)
    m.scaling = random()*10
    m.offsetX = randint(0, 12)
    m.offsetY = randint(0, 12)
    m.updateTime = datetime.datetime.now()
    m.image.put(mPhoto, content_type= 'image/jpg')
    try:
        m.save()
        print "wuploaded!!"
    except Exception,e:
		print e
		
def listData():
	allWatchRecords = watchRecords.objects.all()[:4]
	
	for record in allWatchRecords:
		print record.id

		
if __name__=='__main__':
	saveMap()
	# start()