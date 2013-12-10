from django.http import HttpResponse
from django.http import Http404
from mongoengine.django.auth import User


from  mongoengine import connect
from documents import mapRecords

connect('rssiSystem')


def index(request):
	html = '<h1>Hello From RSSI</h1>'
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