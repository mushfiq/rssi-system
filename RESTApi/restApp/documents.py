from mongoengine import Document, EmbeddedDocument, StringField

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
    
