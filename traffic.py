import pymysql

project_db = pymysql.connect( host="localhost", port=3306, \
    user="pymysql", passwd="xxxx", db="projectdata")

pj_cursor = project_db.cursor()

pj_cursor.execute("INSERT INTO traffic_records(traffic,ips,cips,sips,bips) VALUES(0,0,0,0,0);")
project_db.commit()
project_db.close()




