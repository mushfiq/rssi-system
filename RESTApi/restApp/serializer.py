import csv
import ast
import StringIO
from tastypie.serializers import Serializer


class CSVSerializer(Serializer):
    print "inside serializers"
    formats = ['json', 'jsonp', 'xml', 'yaml', 'html', 'plist', 'csv', 'txt']
    content_types = {
        'json': 'application/json',
        'jsonp': 'text/javascript',
        'xml': 'application/xml',
        'yaml': 'text/yaml',
        'html': 'text/html',
        'plist': 'application/x-plist',
        'csv': 'text/csv',
        'txt': 'text/plain',
    }

    def to_txt(self, data, options=None):
        options = options or {}
        data = self.to_simple(data, options)
        raw_data = StringIO.StringIO()
        # Untested, so this might not work exactly right.
        for item in data:
            writer = csv.DictWriter(raw_data, item.keys(), extrasaction='ignore')
            writer.write(item)
        print "inside to txt", raw_data
        return raw_data

    def from_txt(self, content):
        # print "type content", type(content)
        raw_data = StringIO.StringIO(content)
        data = []
        print ""
        d =  ast.literal_eval(str(raw_data))
        print "d", type(d)
        # Untested, so this might not work exactly right.
        # for item in csv.DictReader(raw_data):
        for i in d.items():
            data.append(item)
        return data