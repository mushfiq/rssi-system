from django.http import HttpResponse

def index(request):
	html = '<h1>Hello From RSSI</h1>'
	return HttpResponse(html)