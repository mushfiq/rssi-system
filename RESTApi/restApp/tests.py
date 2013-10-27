from django.test import TestCase
from documents import rawData
import datetime

class CheckMyDB(TestCase):
	
	def test_insertion(self):
		total_documents = len(rawData.objects.all())
		dObj = rawData()
		dObj.signalStrenth = "DUMMY SIGNAL"
		dObj.insertAt = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
		dObj.save()
		new_total_documents = len(rawData.objects.all())
		self.assertEqual(total_documents+1, new_total_documents)
		dummy = rawData.objects.get(id=dObj.id)
		dummy.delete()
		print "deleted test data"
