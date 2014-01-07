import requests
import unittest
import json    

from configuration import BASE_URL, OPTIONS, HEADERS

'''
    This test suit test receiver resource 
    change data values to check with custom data.
    And test suit covers create, update, delete (teardown) for new resource.
'''

class watchResourceTest(unittest.TestCase):
    
    def setUp(self):
        self.base_url = BASE_URL+'watch/'
        data = {'mapId': 2, 'watchId': '12', 'x': 12.3, 'y': 3.4}
        self.response = requests.post(self.base_url, data=json.dumps(data), headers=HEADERS, params=OPTIONS)
        self.testUrl = self.response.headers.get('location')
   
    def testInsertWatch(self):
        self.assertTrue(201, self.response.status_code)
    
    def testUpdateWatch(self):
        data = {'mapId': 20, 'watchId': '52', 'x': 12.3, 'y': 3.4}
        response = requests.put(self.testUrl, data=json.dumps(data), headers=HEADERS, params=OPTIONS )
        self.assertTrue(204, response.status_code)
        
    def tearDown(self):
        response = requests.delete(self.testUrl, params=OPTIONS)
        self.assertTrue(204, response.status_code)
        
        
        
if __name__=='__main__':
    unittest.main()
        
    