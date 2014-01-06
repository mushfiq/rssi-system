import requests
import unittest
import json      

from configuration import BASE_URL, OPTIONS, HEADERS

class MapResourceTest(unittest.TestCase):
    
    def setUp(self):
        self.base_url = BASE_URL+'map/'
        data = {"mapId": 2, "receiverId": "12", "width": 12, "height": 3, "scalingX": 1.5, "scalingY": 2.6, "offsetX":23, "offsetY": 31}
        self.response = requests.post(self.base_url, data=json.dumps(data), headers=HEADERS, params=OPTIONS)
        self.testUrl = self.response.headers.get('location')
        
    def testInsertMap(self):
        self.assertTrue(201, self.response.status_code)

    def testUpdateMap(self):
        data = {"mapId": 2, "receiverId": "102", "width": 102, "height": 102, "scalingX": 125, "offsetX":23, "offsetY": 31}
        response = requests.put(self.testUrl, data=json.dumps(data), headers=HEADERS, params=OPTIONS )
        self.assertTrue(204, response.status_code)

    def tearDown(self):
        response = requests.delete(self.testUrl, params=OPTIONS)
        self.assertTrue(204, response.status_code)
        

if __name__=='__main__':
    unittest.main()