import setup_django
import datetime
from random import randint, random
from  mongoengine import connect
from restApp.documents import mapRecords


connect('rssiSystem')

MAP_IMAGES = ['sampleMap1.jpg', 'sampleMap2.jpg']

def get_random_map():
    random_map = MAP_IMAGES[randint(0, len(MAP_IMAGES)-1)]
    mPhoto = open(random_map, 'rb')
    return mPhoto

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
    m.image.put(get_random_map(), content_type= 'image/jpg')
    try:
        m.save()
        print m.id
        print "wuploaded!!"
    except Exception,e:
		print e
		
def listData():
	allWatchRecords = watchRecords.objects.all()[:4]
	
	for record in allWatchRecords:
		print record.id

def update_maps():
    all_maps = mapRecords.objects.all()
    for m in all_maps:
        m.image = None
        m.image.put(get_random_map(), content_type='image/jpg')
        m.save()
    print "Total %d maps updated!" % len(all_maps)
    return
    
		
if __name__=='__main__':
    # get_random_map()
    saveMap()
	# start()
    # update_maps()