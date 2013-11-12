import os
from fabric.api import *
abspath = lambda filename: os.path.join(os.path.abspath(os.path.dirname(__file__)), filename)

#specify server user name
env.user = 'putUserName'
env.local_site_root = abspath('')
env.serverpath = '/'
env.site_root = '/var/www/rssi'

def server():
    env.hosts      = [
		'shironambd.com',
	]
    env.graceful = False

#next two tasks shows access log and error log
def show_access_log():
	run('sudo tail -n 10 /var/log/apache2/shironambd-custom.log')
	
def show_error_log():
	run('sudo tail -n 10 /var/log/apache2/shironambd-error.log')
	
def restart_apache():
	run('sudo service apache2 restart')

'''
	this task deploy the code to server 
	paths need to fixed before real prod use
'''
def rssi_deploy():
    env.site_name = 'RESTApi'
    env.site_path    = '/var/www/rssi'
    run('sudo rm -rf %s/%s' % (env.site_path,env.site_name))
    local('tar --exclude="*.pyc" -czf %s.tgz %s/' % (env.site_name, env.site_name))
    put('%s.tgz' % env.site_name, env.site_root)
    run('cd  %s && tar -xzf %s.tgz' % (env.site_root, env.site_name))
    run('rm -rf %s/%s.tgz' % (env.site_path,env.site_name))
    local('rm -rf %s.tgz' % (env.site_name))