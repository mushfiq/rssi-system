import setup_django
from  mongoengine import connect
from restApp.documents import watchRecords

connect('rssiSystem')

mPhoto = open('sampleMap2.jpg', 'r')

def start():
	print "starting..."
	w = watchRecords()
	w.x = 3.2
	w.y = 4.2
	w.mapImage.put(mPhoto, content_type= 'image/jpg')
	try:
		w.save()
		print "wuploaded!!"
	except Exception,e:
		print e
		
def listData():
	allWatchRecords = watchRecords.objects.all()[:4]
	
	for record in allWatchRecords:
		print record.id

		
if __name__=='__main__':
	listData()
	# start()