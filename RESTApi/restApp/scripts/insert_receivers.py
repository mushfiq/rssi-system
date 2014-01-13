import requests
import json      

HEADERS = {'content-type': 'application/json'} 
OPTIONS = {'access_key':'5298fe82fb21ab1b8b725333', 'format':'json'}
RECEIVER_LIST = [{"mapId": 3, "receiverId": "0", "x": 7.3, "y": 7.3, "angle": None}, {"mapId": 2, "receiverId": "0", "x": 9.3, "y": 7.3, "angle": None}]

def insert_test(receiver_record):
    url = 'http://localhost:8000/api/v1/receiver/'
    
    response = requests.post(url, data=json.dumps(receiver_record), headers=HEADERS, params=OPTIONS)
    print response.status_code, response.headers.get('location')
    
if __name__=='__main__':
    for receiver_record in RECEIVER_LIST:
        insert_test(receiver_record)
    
    