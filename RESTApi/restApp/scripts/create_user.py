import argparse
import getpass
import setup_django
from mongoengine.django.auth import User


def create_user(user_name, password, email):
    user = User()
    try:
        # print user_name, password, email
        user.create_user(user_name, password, email)
        print "User Created!"
        new_user = User.objects.get(username=user_name)
        print "User Access Key", new_user.id
    except Exception,e:
        print "Error", e
      
          
def parse_arguments():
    parser = argparse.ArgumentParser()
    parser.add_argument('-u', '--user_name', required=True, help='Set Username please')
    parser.add_argument('-e', '--email', required=True, help='Set Email please')
    password = getpass.getpass()
    repeated_password = getpass.getpass('Repeat Password:')
    if password != repeated_password:
        print "Password does not match..."
        return 

    args = parser.parse_args()
    # print args.email
    create_user(args.user_name, password, args.email)
    
    
if __name__=='__main__':
    # create_user()
    parse_arguments()
    # parse_arguments()
    # test()
    
