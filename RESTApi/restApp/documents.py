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

    
class watchRecords(InheritableDocument):
	x = FloatField()
	y = FloatField()
	insertedAt = DateTimeField()
	mapId = IntField()
	watchId = StringField()
            

class maps(InheritableDocument):
	mapId = IntField()
	image = FileField()
	width = IntField()
	height = IntField()
	sclaing = FloatField()
	offsetX = IntField()
	offsetY = IntField()
	updateTime = DateTimeField()
	

