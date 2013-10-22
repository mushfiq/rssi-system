import bson
from mongoengine import connect, Document, EmbeddedDocument, StringField
import datetime

connect('rssiSystem')

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
    
