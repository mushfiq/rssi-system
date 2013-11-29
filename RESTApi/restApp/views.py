from django.http import HttpResponse


from  mongoengine import connect
from documents import mapRecords

connect('rssiSystem')


def index(request):
	html = '<h1>Hello From RSSI</h1>'
	return HttpResponse(html)
	
def getImage(request):
    image_id = request.GET.get('id')
    file_obj = mapRecords.objects.get(id=image_id)
    return HttpResponse(file_obj.image.read(), mimetype="image/jpeg")