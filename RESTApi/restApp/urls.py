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
from restApp.resources import RssiResource
from restApp.resources import WatchResource

v1_api = api.Api(api_name='v1')
v1_api.register(RssiResource())
v1_api.register(WatchResource())
urlpatterns = patterns('',
	url(r'^$', 'restApp.views.index'),
    url(r'^api/', include(v1_api.urls)),
)