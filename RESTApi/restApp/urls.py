from django.conf.urls import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

# urlpatterns = patterns('',
#     # Examples:
#     # url(r'^$', 'restApp.views.home', name='home'),
#     # url(r'^restApp/', include('restApp.foo.urls')),
# 
#     # Uncomment the admin/doc line below to enable admin documentation:
#     # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),
# 
#     # Uncomment the next line to enable the admin:
#     # url(r'^admin/', include(admin.site.urls)),
# )
from tastypie import api

from restApp.resources import WatchResource, MapResource, ReceiverResource

v1_api = api.Api(api_name='v1')
v1_api.register(WatchResource())
v1_api.register(MapResource())
v1_api.register(ReceiverResource())

urlpatterns = patterns('',
	url(r'^index$', 'restApp.views.index'),
	url(r'^api/v1/image/', 'restApp.views.getImage'),
    url(r'^api/', include(v1_api.urls)),
)