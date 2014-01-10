import requests
import unittest
import json      

from configuration import OPTIONS, BASE_URL, HEADERS

'''
    This test suit test receiver resource 
    change data values to check with custom data.
    And test suit covers create, update, delete (teardown) for new resource.
'''

class ReceiverResourceTest(unittest.TestCase):
    
    def setUp(self):
        self.base_url = BASE_URL+'receiver/'
        data = {"mapId": 0, "receiverId": "0", "x": 0.3, "y": 0.3}
        self.response = requests.post(self.base_url, data=json.dumps(data), headers=HEADERS, params=OPTIONS)
        self.testUrl = self.response.headers.get('location')
        
    def testInsertReceiver(self):
        self.assertTrue(201, self.response.status_code)

    def testUpdateReceiver(self):
        data = {"mapId": 0, "receiverId": "0", "x": 9.3, "y": 9.3}
        response = requests.put(self.testUrl, data=json.dumps(data), headers={'content-type': 'application/json'}, params=OPTIONS )
        self.assertTrue(204, response.status_code)

    def tearDown(self):
        response = requests.delete(self.testUrl, params=OPTIONS)
        self.assertTrue(204, response.status_code)
        

if __name__=='__main__':
    unittest.main()
