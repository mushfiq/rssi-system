# from tastypie import authorization as tastypie_authorization, fields as tastypie_fields
from tastypie.authorization import Authorization

from django.views.decorators.csrf import csrf_exempt

from tastypie_mongoengine import paginator, resources

from tastypie.resources import ModelResource, ALL, ALL_WITH_RELATIONS

from restApp import documents


class WatchResource(resources.MongoEngineResource):
    class Meta:
        queryset = documents.watchRecords.objects.order_by('-insertedAt')

        allowed_methods = ('get', 'post', 'update')
        
        paginator_class = paginator.Paginator
        
        filtering = {
            "insertedAt" : ['gte','lte',],
            "mapId"         : ['exact',],
            "watchId"     : ['exact',],
        }

        excludes = ['id', 'resource_uri']

        authorization = Authorization()
        resource_name = 'watch'

class MapResource(resources.MongoEngineResource):
    class Meta:
        queryset = documents.mapRecords.objects.order_by('mapId')

        excludes = ['resource_uri']
        
        filtering = {
        	"mapId"		 : ['exact',],
        }

        allowed_methods = ('get')
        
        
class ReceiverResource(resources.MongoEngineResource):
    class Meta:
        queryset = documents.receiverRecords.objects.all()
        
        # resource_name = 'receiver'
        
        all_methods = ('get')
        
        filtering = {
        	"mapId"		 : ['exact',],
        }
        
        paginator_class = paginator.Paginator
        excludes = ['id', 'resource_uri']
    
		
		