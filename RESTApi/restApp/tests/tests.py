import datetime
import setup_django
from django.test import TestCase
from documents import watchRecords

import unittest
import os

os.environ['DJANGO_SETTINGS_MODULE'] = 'settings'


class TestMongoDB(unittest):
    '''
    TODO:Complete Test case.
    '''
    def setUp(slef):
        total_documents = len(rawData.objects.all())
        dObj = rawData()
        dObj.signalStrenth = "DUMMY SIGNAL"
        dObj.insertAt = datetime.datetime.now().strftime("%Y-%m-%d %H:%M:%S")
        dObj.save()
    
    def testInsertion(self):
        new_total_documents = len(rawData.objects.all())
        self.assertEqual(total_documents+1, new_total_documents)

    def tearDown(self):
        dummy = rawData.objects.get(id=dObj.id)
        dummy.delete()
        print "deleted test data"
        