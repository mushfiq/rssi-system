from  mongoengine import connect
from documents import watchRecords

connect('rssiSystem')

# mPhoto = open('icon.png', 'r')

def start():
	print "starting..."
	w = watchRecords()
	w.x = 1.2
	w.y = 3.2
	w.mapImage.put(mPhoto, content_type= 'image/png')
	try:
		w.save()
		print "wuploaded!!"
	except Exception,e:
		print e
		
def listData():
	allWatchRecords = watchRecords.objects.all()[:2]
	for record in allWatchRecords:
		print record.mapImage.read()
		
if __name__=='__main__':
	listData()