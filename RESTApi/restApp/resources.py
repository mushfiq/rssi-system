from tastypie import authorization as tastypie_authorization, fields as tastypie_fields

from tastypie_mongoengine import paginator, resources

from restApp import documents

class WatchResource(resources.MongoEngineResource):
	class Meta:
		queryset = documents.watchRecords.objects.order_by('-insertedAt')
        
        resource_name = 'watch'
        
        allowed_methods = ('get')
		
        paginator_class = paginator.Paginator

        filtering = {
        	"insertedAt" : ['gte','lte',],
        	"mapId"		 : ['exact',],
        	"watchId"	 : ['exact',],
        }

        excludes = ['id', 'resource_uri']

class MapResource(resources.MongoEngineResource):
	class Meta:
		queryset = documents.maps.objects.all()
        
        resource_name = 'map'
		
        allowed_methods = ('get')

        paginator_class = paginator.Paginator

        filtering = {
        	"mapId"		 : ['exact',],
        }

        excludes = ['id', 'resource_uri']
        
class ReceiverResource(resources.MongoEngineResource):
    class Meta:
        queryset = documents.receivers.objects.all()
        
        resource_name = 'receiver'
        
        all_methods = ('get')
        
        paginator_class = paginator.Paginator
        
        exclude = ['id']
    
		
		