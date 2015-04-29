from vertica_python import connect

class walgreen_conn(object):
    
    def db_conn(self):
       
        connection = connect({
                              'host': 'bigonc.sdsc.edu',
                              'port': 5433,
                              'user': 'dbadmin',
                              'password': '3Nathan$',
                              'database': 'walgreen'

                              })
        
        return connection