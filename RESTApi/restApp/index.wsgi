import os
import sys
import site

# Add the site-packages of the chosen virtualenv to work with
site.addsitedir('~/.virtualenvs/rssi/lib/python2.6/site-packages/')

# Add the app's directory to the PYTHONPATH
sys.path.append('/var/www/rssi/RESTApi')
sys.path.append('/var/www/rssi/RESTApi/restApp')

os.environ['DJANGO_SETTINGS_MODULE'] = 'restApp.settings'

# Activate your virtual env
activate_env=os.path.expanduser("/home/mushfiq/.virtualenvs/rssi/bin/activate_this.py")
execfile(activate_env, dict(__file__=activate_env))

import django.core.handlers.wsgi
application = django.core.handlers.wsgi.WSGIHandler()