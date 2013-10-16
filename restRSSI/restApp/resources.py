from tastypie import authorization as tastypie_authorization, fields as tastypie_fields

from tastypie_mongoengine import paginator, resources

from restApp import documents

class RssiResource(resources.MongoEngineResource):
    class Meta:
        queryset = documents.rawData.objects.all().order_by('id')
        allowed_methods = ('get')
        paginator_class = paginator.Paginator

