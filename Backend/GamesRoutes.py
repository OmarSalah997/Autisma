import base64
import os
from TokenGenerator import *
from flask import request
from flask_restful import Resource
import mysql.connector as mysql
import random


DIRECTORY = "Games/"
my_folders = ['happy/','sad/','angry/','numbers/']


def decoding_file(base64_img, file_name):
    base64_img_bytes = base64_img.encode('utf-8')
    with open(file_name, 'wb') as file_to_save:
        decoded_image_data = base64.decodebytes(base64_img_bytes)
        file_to_save.write(decoded_image_data)


def encoding_file(file_name):
    with open(file_name, 'rb') as binary_file:
        binary_file_data = binary_file.read()
        base64_encoded_data = base64.b64encode(binary_file_data)
        return base64_encoded_data.decode('utf-8')

def get_random_image(folder_name):
    images = os.listdir(folder_name)
    return images[random.randint(0,len(images)-1)]


class MatchingGame(Resource):
    def get(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation': 'fail', "error_code": "2001"}
        stoken = str(token)
        token = token.replace("'", "")
        token = bytes(token[1:], 'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', "error_code": "2005"}
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("SELECT premium FROM  users where token= %s and ID = %s",(stoken, str(user_id)))
            rows = cursor.fetchall()
            if cursor.rowcount !=1:
                return {'operation': 'fail', 'error_code': "2005"}
            elif not bool(rows[0][0]):
                return {'operation': 'fail', 'error_code': "3001"}
            else:
                i = random.randint(0,2)
                img1= encoding_file(DIRECTORY+my_folders[i]+get_random_image(DIRECTORY+my_folders[i]))
                img2 = encoding_file(DIRECTORY + my_folders[i] +get_random_image(DIRECTORY+my_folders[i]))
                img3 = encoding_file(DIRECTORY+my_folders[abs(i-1)] +get_random_image(DIRECTORY+my_folders[abs(i-1)]))
                img4 = encoding_file(DIRECTORY+my_folders[abs(i-1)] +get_random_image(DIRECTORY+my_folders[abs(i-1)]))
                return {'operation': 'success',"image1":img1 ,"image2":img2 ,"image3":img3 ,"image4":img4}
        except Exception as e:
            return {'operation': 'fail', "error_code": "1001"}


class ChooseFeeling(Resource):
    def get(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation': 'fail', "error_code": "2001"}
        stoken = str(token)
        token = token.replace("'", "")
        token = bytes(token[1:], 'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', "error_code": "2005"}
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("SELECT premium FROM  users where token= %s and ID = %s", (stoken, str(user_id)))
            rows = cursor.fetchall()
            if cursor.rowcount != 1:
                return {'operation': 'fail', 'error_code': "2005"}
            elif not bool(rows[0][0]):
                return {'operation': 'fail', 'error_code': "3001"}
            else:
                i = random.randint(0, 2)
                img1 = encoding_file(DIRECTORY + my_folders[i] + get_random_image(DIRECTORY + my_folders[i]))
                return {'operation': 'success', "image": img1, "solution":my_folders[i][0:len(my_folders[i])-2]}
        except Exception as e:
            return {'operation': 'fail', "error_code": "1001"}


class WriteNumber(Resource):
    def get(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation': 'fail', "error_code": "2001"}
        stoken = str(token)
        token = token.replace("'", "")
        token = bytes(token[1:], 'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', "error_code": "2005"}
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("SELECT premium FROM  users where token= %s and ID = %s", (stoken, str(user_id)))
            rows = cursor.fetchall()
            r = cursor.rowcount
            cursor.close()
            db.close()
            if r != 1:
                return {'operation': 'fail', 'error_code': "2005"}
            elif not bool(rows[0][0]):
                return {'operation': 'fail', 'error_code': "3001"}
            else:
                img_name= get_random_image(DIRECTORY + my_folders[3])
                print(DIRECTORY + my_folders[3] + img_name)
                img1 = encoding_file(DIRECTORY + my_folders[3] + img_name)
                db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
                cursor = db.cursor()
                cursor.execute('SELECT number FROM image_numbers where name= "'+str(img_name)+'"')
                number = cursor.fetchone()[0]
                return {'operation': 'success', "image1": img1,"solution":str(number)}
        except Exception as e:

            return {'operation': 'fail', "error_code": "1001"}
