from django.http import HttpResponse
from django.http import Http404
from mongoengine.django.auth import User
from django.core.exceptions import ObjectDoesNotExist

from django.views.decorators.csrf import csrf_exempt

from  mongoengine import connect
from documents import mapRecords

import json

connect('rssiSystem')


def index(request):
	html = '<h1>Shironambd coming soon!</h1>'
	return HttpResponse(html)
	
def getImage(request):
    user_id = request.GET.get('access_key')
    try:
        user = User.objects.get(id=user_id)
    except Exception,e:
        print e
        raise Http404("User Not Authenticated!")
        
    image_id = request.GET.get('id')
    file_obj = mapRecords.objects.get(id=image_id)
    return HttpResponse(file_obj.image.read(), mimetype="image/jpeg")

@csrf_exempt
def authenticate(request):
    
    if request.method != 'POST':
        raise Http404('Error')
        
    user_name =  request.POST.get('username')
    pass_word = request.POST.get('password')
    try:
        user = User.objects.get(username=user_name, password=pass_word)
        user_obj = str({'access_key':user.id.__str__(), 'name':user.username})
        response = HttpResponse(json.dumps(user_obj), mimetype='application/json')
        response['Access-Control-Allow-Origin'] = "*"
        return response
        
    except ObjectDoesNotExist:
        raise Http404("User Not Authenticated!")
        
        
    
    
    
    
    
    
    