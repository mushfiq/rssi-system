from django.http import HttpResponse


from  mongoengine import connect
from documents import watchRecords

connect('rssiSystem')


def index(request):
	html = '<h1>Hello From RSSI</h1>'
	return HttpResponse(html)
	
def getImage(request):
	image_id = request.GET.get('id')
	image = watchRecords.objects.get(id=image_id)
	
	records = watchRecords.objects.all()[:2]
	for record in records:
		print record.id
	return HttpResponse(image.mapImage.read(), mimetype="image/jpeg")