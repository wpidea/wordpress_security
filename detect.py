import pymysql

from visitor import Visitor
from extract_target import extract_target

project_db = pymysql.connect( host="localhost", port=3306, \
    user="pymsql", passwd="6lO/]u(zWpBQUa5A", db="projectdata")

nginx_db = pymysql.connect( host="localhost", port=3306, \
    user="pymsql", passwd="6lO/]u(zWpBQUa5A", db="nginxdata")

pj_cursor = project_db.cursor()
nx_cursor = nginx_db.cursor()

pj_cursor.execute("SELECT url_name FROM suspicious_urls;")
results = pj_cursor.fetchall()

suspicious_targets = []
for  record in results:
    suspicious_targets.append(record[0])

pj_cursor.execute("SELECT nginx_id FROM nginx_record_index;")
record_index = pj_cursor.fetchone()[0]

#Selet nginx records that after last time checking
nx_cursor.execute("SELECT Message FROM SystemEvents WHERE ID >"+ str(record_index) +";")
messages = nx_cursor.fetchall()
visitor_records =[]
for message in messages:
    visitor_records.append(message[0])

record_index = record_index + len(visitor_records)
pj_cursor.execute("UPDATE nginx_record_index SET nginx_id =" + str(record_index) + " WHERE id=1 ;")

visited_suspicious_targets_ips = []
for visitor_record in visitor_records:
    visitor = Visitor(visitor_record)
    if visitor.url in suspicious_targets:
        visited_suspicious_targets_ips.append(visitor.ip)

pj_cursor.execute("SELECT ip_add FROM suspicious_ips;")
susp_ips = pj_cursor.fetchall()
suspicious_ips = []
for ip in susp_ips:
    suspicious_ips.append(ip[0])

pj_cursor.execute("SELECT ip_add FROM cautious_ips;")
caus_ips = pj_cursor.fetchall()
cautious_ips = []
for ip in caus_ips:
    cautious_ips.append(ip[0])

at_least_cautious = []

#check if the visitor that visited susp_urls in susp_ips table
for ip in visited_suspicious_targets_ips:
    for susp_ip in suspicious_ips:
        if ip == susp_ip:
            pj_cursor.execute("UPDATE suspicious_ips SET counter= counter + 1 WHERE ip_add='"+susp_ip+"';")
            
        else:
            if ip not in at_least_cautious:
                at_least_cautious.append(ip)


#check if the visitor that visited susp_urls in caut_ips table
for ip in at_least_cautious:
    for caut_ip in cautious_ips:
        if ip == caut_ip:
            pj_cursor.execute("UPDATE cautious_ips SET counter= counter + 1 WHERE ip_add='"+caut_ip+"';")
            
        else:
            pj_cursor.execute("INSERT IGNORE INTO cautious_ips(ip_add, counter) VALUES('"+ ip +"', 1);")


#Move the cautious ips that their counters over 3 times to suspicious_ips table
pj_cursor.execute("SELECT ip_add FROM cautious_ips WHERE counter > 3;")
three_results = pj_cursor.fetchall()
counter_over_three_ips = []
for ip in three_results:
    counter_over_three_ips.append(ip[0])

susp_ip_visited_urls = []
for ip in  counter_over_three_ips:
    pj_cursor.execute("INSERT INTO suspicious_ips(ip_add, counter) VALUES('"+ ip +"', 1);")
    pj_cursor.execute("DELETE FROM cautious_ips WHERE ip_add='"+ ip +"';")
    nx_cursor.execute("SELECT Message FROM SystemEvents WHERE Message LIKE '%"+ ip +"%';")
    urls_results = nx_cursor.fetchall()
    new_susp_urls = []
    for urls_result in urls_results:
        susp_url = extract_target(urls_result[0])
        if susp_url not in new_susp_urls:
            new_susp_urls.append(susp_url)
        
    susp_urls_in_one = ", ".join(new_susp_urls)
    pj_cursor.execute("UPDATE suspicious_ips SET visited_urls='"+ susp_urls_in_one + "' WHERE ip_add='"+ ip +"';")


#Move the suspicious ips that their counters over 5 times to blaclist_ips table
pj_cursor.execute("SELECT ip_add FROM suspicious_ips WHERE counter > 5;")
five_results = pj_cursor.fetchall()
counter_over_five_ips = []
for ip in five_results:
    counter_over_five_ips.append(ip[0])

pj_cursor.execute("SELECT url_name FROM suspicious_urls;")
recorded_susp_urls_results = pj_cursor.fetchall()
recorded_susp_urls = []
for url in recorded_susp_urls_results:
    recorded_susp_urls.append(url[0])

for ip in counter_over_five_ips:
    pj_cursor.execute("INSERT INTO blacklist_ips(ip_add) VALUES('"+ ip +"');")
    pj_cursor.execute("SELECT visited_urls FROM suspicious_ips WHERE ip_add='"+ ip +"';")
    susp_urls_result = pj_cursor.fetchone()[0]
    seperated_susp_urls = susp_urls_result.split(", ")


    for seperated_susp_url in  seperated_susp_urls:
        if seperated_susp_url not in recorded_susp_urls:
            pj_cursor.execute("INSERT INTO suspicious_urls(url_name) VALUES('" + seperated_susp_url +"');")

    pj_cursor.execute("DELETE FROM suspicious_ips WHERE ip_add='" + ip +"';")



project_db.commit()
project_db.close()
nginx_db.close()