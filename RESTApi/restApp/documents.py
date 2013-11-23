from mongoengine import Document, EmbeddedDocument, StringField, FloatField, DateTimeField,\
                    IntField, FileField, ReferenceField, ListField, EmbeddedDocumentField

class InheritableDocument(Document):
    meta = {
        'abstract': True,
        'allow_inheritance': True,
    }

class InheritableEmbeddedDocument(EmbeddedDocument):
    meta = {
        'abstract': True,
        'allow_inheritance': True,
    }

class rawData(InheritableDocument):
    signalStrenth = StringField(max_length=200, required=True)
    insertAt = StringField(max_length=40, required=True)
    
    
class watchRecords(InheritableDocument):
	x = FloatField()
	y = FloatField()
	insertedAt = DateTimeField()
	mapId = IntField()
	watchId = StringField()
    # wMap =  EmbeddedDocumentField('map')
    

class maps(InheritableDocument):
	mapId = IntField()
	image = FileField()
	width = IntField()
	height = IntField()
	sclaing = FloatField()
	offsetX = IntField()
	offsetY = IntField()
	updateTime = DateTimeField()
	

