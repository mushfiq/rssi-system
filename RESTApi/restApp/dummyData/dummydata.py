import time
import datetime
import setup_django
from random import shuffle, random, uniform, randint
from restApp.documents import rawData, watchRecords, maps
import json
from  mongoengine import connect, Document

#datetime.datetime.strptime(d, '%Y-%m-%dT%H:%M:%S')

connect('rssiSystem')

randomStrength = ["AAAA", "BBBB", "CCCCC", "DDDDD"]

dthandler = lambda obj: obj.isoformat() if isinstance(obj, datetime.datetime)  or isinstance(obj, datetime.date) else None

silvioDataWatch1 = [{'x':0,'y':145}, {'x':10,'y':145}, {'x':20,'y':145},
{'x':35,'y':145}, {'x':50,'y':160}, {'x':70,'y':180}, {'x':90,'y':200},
{'x':90,'y':220}, {'x':110,'y':230}, {'x':130,'y':235}, {'x':150,'y':238},
{'x':180,'y':238}, {'x':210,'y':238}]

silvioDataWatch2 = [{'x':0,'y':145}, {'x':10,'y':145}, {'x':30,'y':130},
{'x':60,'y':130}, {'x':90,'y':120}, {'x':120,'y':120}, {'x':140,'y':90}, {'x':160,'y':85}, 
{'x':190,'y':85},{'x':210,'y':85}, {'x':230,'y':85}, {'x':250,'y':85}, {'x':210,'y':75}, {'x':140,'y':75}]

''' this script is for inserting dummy data into the mongo'''	
watchIdList = ['watch1', 'watch2', 'watch3']

def getAll():
	allData = rawData.objects.all()
	print allData
	
def insertTest(signalString):
	dObj = rawData()
	dObj.signalStrenth = signalString
	dObj.insertAt = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
	dObj.save()
	print "saved"
	
def insertDummyData():
	fileName = 'dummyData/testdata.txt'
	fileObject = open(fileName, 'r')
	for line in fileObject:
		insertTest(line)

def addRadomData():
	time.sleep(3)
	dObj = rawData()
	shuffledSignals	= shuffle(randomStrength, random)
	dObj.signalStrenth = randomStrength[0]
	dObj.insertAt = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
	dObj.save()
	print "saved"
	
def updatedDummyData(dataDict):
	dObj = watchRecords()

	dObj.x = dataDict.get('x')
	dObj.y = dataDict.get('y')
	dateString = dataDict.get('insertedAt')
	# print "DATE STRING", dateString
	# print "CONVERTED", datetime.datetime.strptime(dateString, '%Y-%m-%dT%H:%M:%S')
	# dObj.insertedAt = dataDict.get('insertedAt')
	# dObj.insertedAt = datetime.datetime.fromtimestamp(dataDict.get('insertedAt'))
	dObj.insertedAt = datetime.datetime.strptime(dateString, '%Y-%m-%dT%H:%M:%S')
	dObj.watchId = dataDict.get("watchId")

	dObj.mapId = dataDict.get('mapId')

	try:
		print dObj
		# print dObj.watchId
		# k 
		# print dObj
		# print dObj.insertedAt
		# dObj.save()
		# print "Saved"
	except Exception, e:
		print e
		
def delete_dummy_data():
	allObjs = watchRecords.objects.all()
	count = 0
	for obj in allObjs:
		obj.delete()
		count+=1
	
	print "All deleted!"


def insertedSilviosData():
	for dataDict in  silvioDataWatch1:
		# print i
		time.sleep(3)
		updatedDummyData(dataDict)
		# x = list(i)[0]
		# y = list(i)[1]
		# print x, y

def generateAndSaveMap():
	#set image number
	# imageNumber = ''
    m = maps()
    m.mapId = randint(0, 12)
    m.width = randint(0, 200)
    m.height = randint(0, 200)
    m.scaling = random()*10
    m.offsetX = randint(0, 12)
    m.offsetY = randint(0, 12)
    m.updateTime = datetime.datetime.now()
    m.save()
    print "saved"
	
def generateMapWatch():
    randomMapId = randint(0, 5)
    m = maps.objects.get(mapId=1)
    watch = watchRecords()
    watch.x = random()*10
    watch.y = random()*10
    watch.watchId = str(randint(0, 12))
    watch.insertedAt = datetime.datetime.now()
    watch.mapId = m.mapId
    watch.save()
    
    print "saved!"
	# print watch
	
def deleteMaps():
	all_map = maps.objects.all()
	all_map.delete()
	print "Total %d map has been deleted!" % len(maps)
	
def readAndInsetrfromJSON():
	file = open('sampleData.json', 'r+')
	lines = file.xreadlines()

	for l in lines:
		dict = json.loads(l)
		# print dict.get('insertedAt')
		updatedDummyData(dict)
		# print json.loads(l).get('x'), json.loads(l).get('y')
	
if __name__ == '__main__':
    # delete_dummy_data()
    # for i in range(0, 5):
    #     generateAndSaveMap()
    for i in range(0, 30):
        generateMapWatch()
	# readAndInsetrfromJSON()
	# insertedSilviosData()
	# for i in range(0, 12):
	# 	generateAndSaveMap()
    # deleteMaps()

