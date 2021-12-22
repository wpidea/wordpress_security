#!/usr/bin/python3                     
import pymysql
import linecache

db = pymysql.connect(host='173.255.211.73',
                     port=3306,
                     user='pymysql',
                     password='6lO/]u(zWpBQUa5A',
                     database='projectdata')

cursor = db.cursor()

#fetch the ip address from blacklist_ips table in MySQL
sql = "SELECT DISTINCT ip_add FROM blacklist_ips"
try:
    cursor.execute(sql)
    results = cursor.fetchall()
#    print(results)
except:
    print("Error: unable to fetch data")



#write in denylist_block.txt, one entry per line
f = open('denylist_block.txt','w')
for ip in results:
#    print(str(ip[0]))
    f.write(str(ip[0])+'\n')
f.close()


#count the amount of the de-duplicated blocked ip addresses, and print the last 5 blocked ip addresses.
f = open('denylist_block.txt','r')
linecount = len(f.readlines())
print('\nTotal blocked: ' + str(linecount))


#write the amount of the de-duplicated blocked ip addresses to the action_blocked table
sql = "INSERT INTO action_blocked(counter_blocked) VALUES(%s);" %linecount
#sql_test = "INSERT INTO action_blocked(counter_blocked) VALUES ('6.6.6.6');"
try:
    cursor.execute(sql)
#    cursor.execute(sql_test)
    db.commit()
except:
    db.rollback()

f.close()


fname = 'denylist_block.txt'
with open(fname, 'r') as f:
    lines = f.readlines()
    first_line = lines[0]
    last_line = lines[-1]
    line1 = lines[-5]
    line2 = lines[-4]
    line3 = lines[-3]
    line4 = lines[-2]
    print('\nThe last 5 blocked ip addresses are:\n' + line1 + line2 + line3 + line4 + last_line )


db.close()

