import os
import json

filename = "/var/log/nginx/idealightup_access.log"


record_files = []
with open(filename) as f_obj:
        records = f_obj.readlines()
        for record in records:
            record = record.split('HTTP')[0]
            record = record.split(' /')[1]
            record = record.split('?')[0]
            record_files.append(record)

print(str(len(record_files)) + " records have been analyzed, results are as follow:")

statistics = {}
for record_file in record_files:
    if record_files.count(record_file) > 1:
        statistics[record_file] = record_files.count(record_file)

for key, value in statistics.items():
    print(key + ": " + str(value))

with open('analyzed_logs.json', 'w') as f_obj:
    json.dump(statistics, f_obj)
