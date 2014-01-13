import time
import requests
import urllib

def do_request_by_limit(type, limit):
    url = 'http://shironambd.com/api/v1/'+str(type)+'/?access_key=529a2d308333d14178f5c54d&limit='+str(limit)+'&format=json'
    resp = requests.get(url)
    return resp.elapsed.total_seconds()
    
def do():
    print '\n\n'
    print "Starting Benchmarking of RSSI REST API"
    print "======================================="
    time.sleep(2)
    print "| 1 watch    |", do_request_by_limit("watch", 1) , ' seconds|'
    print "__________________________________"
    print "| 1 map      |",  do_request_by_limit("map", 1), ' seconds|'
    print "__________________________________"
    print "| 1 receiver |",  do_request_by_limit("receiver", 1), ' seconds|'
    print "__________________________________"
    print "| 10 watch   |", do_request_by_limit("watch", 10), ' seconds|'
    print "__________________________________"
    print "| 10 map     |",  do_request_by_limit("map", 10), ' seconds|'
    print "__________________________________"
    print "| 10 receiver|",  do_request_by_limit("receiver", 10), ' seconds|'
    print "__________________________________"
    print "| 100 watch  |", do_request_by_limit("watch", 100), ' seconds|'
    print "__________________________________"
    print "| 100 map    |",  do_request_by_limit("map", 100), ' seconds|'
    print "__________________________________"
    print "|100 receiver|",  do_request_by_limit("receiver", 100), ' seconds|'
    print "__________________________________"
    print '\n\n'
    
    return 
    
    
    
    
    
if __name__=='__main__':
    do()