import datetime
from django.test import TestCase
from documents import watchRecords

import os

os.environ['DJANGO_SETTINGS_MODULE'] = 'settings'

from tastypie.test import ResourceTestCase
    

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


class WatchResourceTest(ResourceTestCase):
    
    def setUp(slef):
        super(EntryResourceTest, self).setUp()
        
    def test_get_list_json(self):
        resp = self.api_client.get('/api/v1/watch/', format=json)
        self.assertValidJSONResponse(resp)
        # print "true!!"
        
        

        
        
