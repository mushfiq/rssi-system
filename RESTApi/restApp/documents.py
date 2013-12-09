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
            

class mapRecords(InheritableDocument):
    mapId = IntField()
    receiverId = IntField()
    image = FileField()
    width = IntField()
    height = IntField()
    scaling = FloatField()
    offsetX = IntField()
    offsetY = IntField()
    updateTime = DateTimeField()
    
    
class receiverRecords(InheritableDocument):
    receiverId = IntField()
    mapId = IntField()
    x = FloatField()
    y = FloatField()
    
    
	

