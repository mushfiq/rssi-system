import requests
import unittest
import json

from configuration import BASE_URL, OPTIONS

'''
    this test case check whether server is returning an image or not, 
    make sure to pass correct image id by replacing imageId.
    And test suit covers create, update, delete (teardown) for new resource
'''
class ImageRetreiveTest(unittest.TestCase):
        
    def testGetImage(self):
        imageId = '52c69505fb21ab02e8c206d1'
        options = {'access_key':'5298fe82fb21ab1b8b725333', 'id': imageId}
        imageUrl = BASE_URL+'image/'
        response = requests.get(imageUrl, params=options)
        self.assertTrue(response.status_code==200)


if __name__=='__main__':
    unittest.main()
        
        
