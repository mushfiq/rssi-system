from tastypie.test import ResourceTestCase
    
class ImageRetreiveTest(ResourceTestCase):
    
        def setUp(slef):
            super(EntryResourceTest, self).setUp()
        
        def test_get_list_json(self):
            resp = self.api_client.get('/api/image/', format=json)
            self.assertValidJSONResponse(resp)
            
        def tearDown(self):
            return
        