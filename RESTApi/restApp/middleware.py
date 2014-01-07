class DisableCSRF(object):
    def process_request(self, request):
        print "disabling csrf",request
        setattr(request, '_dont_enforce_csrf_checks', True)