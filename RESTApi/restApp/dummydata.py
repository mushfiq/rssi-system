import time
import datetime
from random import shuffle, random, uniform, randint
from documents import rawData, watchRecords
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
		# print dObj.watchId
		# k 
		# print dObj
		# print dObj.insertedAt
		dObj.save()
		# print "Saved"
	except Exception, e:
		print e
		
def delete_dummy_data():
	allObjs = watchRecords.objects.all()
	for obj in allObjs:
		obj.delete()
	
	print "All deleted!"


def insertedSilviosData():
	for dataDict in  silvioDataWatch1:
		# print i
		time.sleep(3)
		updatedDummyData(dataDict)
		# x = list(i)[0]
		# y = list(i)[1]
		# print x, y

def readAndInsetrfromJSON():
	file = open('sampleData.json', 'r+')
	lines = file.xreadlines()

	for l in lines:
		dict = json.loads(l)
		# print dict.get('insertedAt')
		updatedDummyData(dict)
		# print json.loads(l).get('x'), json.loads(l).get('y')
	
if __name__ == '__main__':
	readAndInsetrfromJSON()
	# insertedSilviosData()
	# for i in range(0, 100):
	# 	updatedDummyData()
	# delete_dummy_data()
	# insertDummyData()
	# for i in range(0, 3):
	#     addRadomData()