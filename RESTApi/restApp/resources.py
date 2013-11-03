from tastypie import authorization as tastypie_authorization, fields as tastypie_fields

from tastypie_mongoengine import paginator, resources

from restApp import documents

class RssiResource(resources.MongoEngineResource):
    class Meta:
        queryset = documents.rawData.objects.order_by('-insertAt')
        allowed_methods = ('get')

        paginator_class = paginator.Paginator

