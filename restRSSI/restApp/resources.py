from tastypie import authorization as tastypie_authorization, fields as tastypie_fields

from tastypie_mongoengine import fields, paginator, resources

from restApp import documents

class RssiResource(resources.MongoEngineResource):
    class Meta:
        # Ordering by id so that pagination is predictable
        queryset = documents.rawData.objects.all().order_by('id')
        # print queryset
        allowed_methods = ('get')
        # authorization = tastypie_authorization.Authorization()
        paginator_class = paginator.Paginator
        # polymorphic = {
        #      'person': 'self',
        #      'strangeperson': StrangePersonResource,
        #  }
