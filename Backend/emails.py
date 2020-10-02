import smtplib
from string import Template
from email.mime.multipart import MIMEMultipart
from email.mime.text import MIMEText

MY_ADDRESS = 'no.reply_autizma@outlook.com'
PASSWORD = 'A20212021a'


def send_email(name , email , sub , con_code):
    # read template email body
    with open('email_template.txt', 'r', encoding='utf-8') as template_file:
        template_file_content = template_file.read()
    message_template = Template(template_file_content)

    # set up the SMTP server
    s = smtplib.SMTP(host='smtp-mail.outlook.com', port=587)
    s.starttls()
    s.login(MY_ADDRESS, PASSWORD)

    # create a message
    msg = MIMEMultipart()

    # add in the actual person name and code to the message template
    message = message_template.substitute(PERSON_NAME=name.title() , CODE =con_code )

    # setup the parameters of the message
    msg['From'] = MY_ADDRESS
    msg['To'] = email
    msg['Subject'] = sub

    # add in the message body
    msg.attach(MIMEText(message, 'plain'))

    # send the message via the server set up earlier.
    s.send_message(msg)
    del msg

    # Terminate the SMTP session and close the connection
    s.quit()
