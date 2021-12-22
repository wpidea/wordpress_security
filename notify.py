# using SendGrid's Python Library
# https://github.com/sendgrid/sendgrid-python
import os
from sendgrid import SendGridAPIClient
from sendgrid.helpers.mail import Mail
from enum import unique
import pymysql
import datetime

project_db = pymysql.connect( host="localhost", port=3306, \
    user="pymysql", passwd="xxxx", db="projectdata")

pj_cursor = project_db.cursor()
pj_cursor.execute("SELECT notified FROM notified_time ORDER BY id DESC limit 1;")
current_time = pj_cursor.fetchone()

pj_cursor.execute("SELECT * FROM File_Monitor_history ORDER BY Query_time DESC limit 1;")
new_time = pj_cursor.fetchone()

pj_cursor.execute("SELECT email_add FROM email_receiver;")
mail_adds=pj_cursor.fetchall()

if current_time[0] != new_time[0]:
    file_num=new_time[2]
    file_name=new_time[3]
    detect_time=new_time[0]

    if int(file_num) > 0:
        for mail_add in mail_adds:
            message = Mail(
            from_email='notify@idealightup.com',
            to_emails= mail_add[0],
            subject='Tampered File Alert',
            html_content='Alert! At '+detect_time+ ', <strong>'+file_num+'</strong> files were found tampered, they are: '+file_name+'.')
            sg = SendGridAPIClient(os.environ.get('SENDGRID_API_KEY'))
            response = sg.send(message)

        


    pj_cursor.execute("INSERT INTO notified_time(notified) SELECT Query_time FROM File_Monitor_history ORDER BY Query_time DESC limit 1;")
    project_db.commit()


project_db.close()



