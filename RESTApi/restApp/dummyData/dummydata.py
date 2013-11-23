import time
import datetime
import setup_django
from random import shuffle, random, uniform, randint
from restApp.documents import watchRecords, maps
import json
from  mongoengine import connect, Document

#datetime.datetime.strptime(d, '%Y-%m-%dT%H:%M:%S')

connect('rssiSystem')

randomStrength = ["AAAA", "BBBB", "CCCCC", "DDDDD"]

dthandler = lambda obj: obj.isoformat() if isinstance(obj, datetime.datetime)  or isinstance(obj, datetime.date) else None

	
class GenerateData(object):
    
    def insert_maps(self):
        return 
        
    def delete_maps(self):
        all_maps = maps.objects.all()
        all_maps.delete()
        print "All maps deleted!"
        return 
        
    def delete_watches(self):
        all_watches = watchRecords.objects.all()
        all_watches.delete()
        print "All watches deleted!"
        return
        
    def get_maps(self):
        return maps.objects.all()
    
    def generate_save_map(self, total):
        for i in range(0, total):
        
            m = maps()
            m.mapId = randint(0, total)
            m.width = randint(0, 200)
            m.height = randint(0, 200)
            m.scaling = random()*10
            m.offsetX = randint(0, total)
            m.offsetY = randint(0, total)
            m.updateTime = datetime.datetime.now()
            try:
                m.save()
                pass
            except Exception, e:
                print e
        
        print "Total %d object created!" %total
        
        return 
    
    def generate_save_watch(self, total):
        for i in range(0, total):
            
            randomMapId = randint(0, 5)
            # print randomMapId
            m = maps.objects.all()
            # print m[randomMapId].mapId
            watch = watchRecords()
            watch.x = random()*10
            watch.y = random()*10
            watch.watchId = str(randint(0, 12))
            watch.insertedAt = datetime.datetime.now()
            watch.receiverId = randint(0, 5)
            watch.mapId = m[randomMapId].mapId
        
            try:
                watch.save()
                pass
            except Exception, e:
                print e
        
        print "Total %d objects created!" %total
            
        return 
        

        
if __name__ == '__main__':
    dataGen = GenerateData()
    # dataGen.generate_save_map(6)
    dataGen.generate_save_watch(30)
    # dataGen.delete_maps()
    # dataGen.delete_watches()

