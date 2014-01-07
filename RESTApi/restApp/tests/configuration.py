''' this file containts base configuration for 
    running the test cases for the backend
    make sure backend rest api is running and mongodb is running
    if you want to test with production checnge BASE_URL with http://shironambd.com/api/v1/
'''

OPTIONS = {'access_key':'5298fe82fb21ab1b8b725333', 'format':'json'}
BASE_URL = 'http://localhost:8000/api/v1/'
HEADERS = {'content-type': 'application/json'} 


