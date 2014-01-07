import time
import datetime
import setup_django
from random import shuffle, random, uniform, randint
from restApp.documents import watchRecords, mapRecords, receiverRecords
import json
# from  mongoengine import connect, Document

#datetime.datetime.strptime(d, '%Y-%m-%dT%H:%M:%S')


randomStrength = ["AAAA", "BBBB", "CCCCC", "DDDDD"]

dthandler = lambda obj: obj.isoformat() if isinstance(obj, datetime.datetime)  or isinstance(obj, datetime.date) else None

	
reciversData = [(None, 1, 1, 0.00, 6.00), (None, 1, 2, 2.00, 6.00), (None, 1, 3, 6.00, 6.00), 
    (None, 1, 4, 0.00, 2.45), (None, 1, 5, 6.00, 2.45), (None, 1, 6, 6.00, 0.00),
    (None, 1, 7, 3.70, 2.00), (None, 1, 8, 0.00, 0.00), (None, 1, 9, 4.00, 0.00),
    (None, 1, 10, 4.00, 6.00),(None, 1, 11, 0.00, 6.00)]


class GenerateData(object):
    
    def insert_maps(self):
        return 
        
    def delete_maps(self):
        all_maps = mapRecords.objects.all()
        all_maps.delete()
        print "All maps deleted!"
        return 
        
    def delete_watches(self):
        all_watches = watchRecords.objects.all()
        all_watches.delete()
        print "All watches deleted!"
        return
        
    def get_maps(self):
        return mapRecords.objects.all()
    
    def generate_save_map(self, total):
        for i in range(0, total):
        
            m = mapRecords()
            m.mapId = randint(0, total)
            m.receiverId = randint(0, 5)
            m.width = randint(0, 200)
            m.height = randint(0, 200)
            m.scalingX = random()*10
            m.scalingY = random()*10
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
    
    def get_random_mapId(self):
        all_maps = mapRecords.objects.all()
        randomMapId = randint(0, len(all_maps))
        return all_maps[randomMapId].mapId
        
    def generate_save_watch(self, total):
        for i in range(0, total):
            
            randomMapId = randint(0, 5)
            # print randomMapId
            m = mapRecords.objects.all()
            # print m[randomMapId].mapId
            watch = watchRecords()
            watch.x = random()*10
            watch.y = random()*10
            watch.watchId = str(randint(0, 12))
            watch.insertedAt = datetime.datetime.now()
            watch.mapId = m[randomMapId].mapId
        
            try:
                watch.save()
                pass
            except Exception, e:
                print e
        
        print "Total %d objects created!" %total
            
        return 
    
        
    def generate_save_receiver(self, total):
        for i in range(0, total):
            r = receiverRecords()
            r.receiverId = randint(0, 5)
            r.mapId = self.get_random_mapId()
            r.x = random()*10
            r.y = random()*10
            
            try:
                r.save()
                pass
            except Exception, e:
                print e
            
    def delete_receivers(self):
        all_recievers = receiverRecords.objects.all()
        all_recievers.delete()
        print "All receivers deleted!"
        return 
        
    def update_old_maps(self):
        maps = mapRecords.objects.all()
        for m in maps:
            m.scaling = None
            m.scalingX = random()*10
            m.scalingY = random()*10
            m.save()
            
        print "Total %d maps updated!", len(maps)
        
    def update_receiver_data(self):
        all_receivers = receiverRecords.objects.all()
        for index,receiver in enumerate(all_receivers[:4]):
            # print index, receiver
            receiver.x = reciversData[index][0]
            receiver.y = reciversData[index][1]
            receiver.save()
            print "reciverid", receiver.id
        print "updated!!"
            
    def delete_extra_receivers(self):
        all_receivers = receiverRecords.objects.all()
        total_receivers = len(all_receivers)
        # print "taoal receivers"
        print "deletetng ...", len(all_receivers)-4 , "receivers"
        delete_total = len(all_receivers)-4
        for i in range(delete_total):
            all_receivers[i].delete()
            
        print "deleted successfully!"
            
    def add_receivers_from_calculated_values(self):
        for i in range(0, len(reciversData)):
            # print reciversData[i][0], reciversData[i][1]
            # print reciversData[i][2]
            r = receiverRecords()
            r.angle = reciversData[i][0]
            r.mapId = reciversData[i][1]
            r.receiverId = reciversData[i][2]
            r.x = reciversData[i][3]
            r.y = reciversData[i][4]
            r.save()
            
        print "Done!"
        return
        
        
if __name__ == '__main__':
    dataGen = GenerateData()
    # dataGen.update_receiver_data()
    # dataGen.delete_extra_receivers()
    dataGen.delete_receivers()
    # dataGen.update_receiver_data()
    dataGen.add_receivers_from_calculated_values()
    # dataGen.delete_maps()
    # dataGen.generate_save_map(6)
    # dataGen.update_old_maps()
    # dataGen.generate_save_receiver(7)
    # dataGen.generate_save_watch(30)
    # dataGen.delete_watches()
    # dataGen.delete_receivers()
