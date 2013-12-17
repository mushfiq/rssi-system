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
    ''' shows access log of remote server'''
    
	run('sudo tail -n 10 /var/log/apache2/shironambd-custom.log')
	
def show_error_log():
    ''' shows error log of remote server'''
    
	run('sudo tail -n 10 /var/log/apache2/shironambd-error.log')
	
def restart_apache():
    ''' restrart server apache '''
    
	run('sudo service apache2 restart')

def rssi_deploy():
    '''
    	this task deploy the code to server 
    	paths need to fixed before real prod use
        rename site_name, site_path accordingly to server
    '''
    
    env.site_name = 'RESTApi' 
    env.site_path    = '/var/www/rssi'
    run('sudo rm -rf %s/%s' % (env.site_path,env.site_name))
    local('tar --exclude="*.pyc" -czf %s.tgz %s/' % (env.site_name, env.site_name))
    put('%s.tgz' % env.site_name, env.site_root)
    run('cd  %s && tar -xzf %s.tgz' % (env.site_root, env.site_name))
    run('rm -rf %s/%s.tgz' % (env.site_path,env.site_name))
    local('rm -rf %s.tgz' % (env.site_name))

def install_dependecies():
    '''
        this method install required depencies
    '''
    
    env.site_name = 'RESTApi' 
    env.site_path    = '/var/www/rssi'
    run('pip install -r requirements.txt')