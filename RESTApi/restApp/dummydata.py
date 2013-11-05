import time
from datetime import datetime
from random import shuffle, random, uniform
from documents import rawData, watchRecords
from  mongoengine import connect, Document

connect('rssiSystem')

randomStrength = ["AAAA", "BBBB", "CCCCC", "DDDDD"]

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
	
def updatedDummyData():
	dObj = watchRecords()
	dObj.x = uniform(1.2,4.5)
	dObj.y = uniform(100.01, 302.5)
	dObj.insertedAt = datetime.now()
	try:
		dObj.save()
		print "Saved"
	except Exception, e:
		print e
	
if __name__ == '__main__':
	for i in range(0, 10):
		updatedDummyData()
	# insertDummyData()
	# for i in range(0, 3):
	#     addRadomData()