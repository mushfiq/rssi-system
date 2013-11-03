import time
import datetime
from random import shuffle, random
from documents import rawData
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
	
if __name__ == '__main__':
	# insertDummyData()
	for i in range(0, 3):
	    addRadomData()