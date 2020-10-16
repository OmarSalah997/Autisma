from GamesRoutes import *


PROFILE_DIRC = "Profile/"


class Profile (Resource):
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
            cursor.execute("SELECT USER_NAME , EMAIL FROM  users where token= %s and ID = %s",(stoken, str(user_id)))
            rows = cursor.fetchall()
            if cursor.rowcount !=1:
                return {'operation': 'fail', 'error_code': "2005"}
            else:
                if os.path.exists(PROFILE_DIRC+str(user_id)+".jpg") :
                    img = encoding_file(PROFILE_DIRC+str(user_id)+".jpg")
                else:
                    img = "null"
                return {'operation': 'success' , "name":str(rows[0][0]) , "email" : str(rows[0][1]) , "image":img}
        except Exception as e:
            return {'operation': 'fail', "error_code": "1001"}



class ProfileImg (Resource):
    def post(self):
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
            cursor.execute("select ID from users where token=%s and ID = %s", (stoken, str(user_id)))
            r = cursor.fetchall()
            rows = cursor.rowcount
            cursor.close()
            db.close()
            if rows == 0:
                return {'operation': 'fail', 'error_code': "2005"}

            img = request.get_json()['image']
            decoding_file(img,PROFILE_DIRC+str(user_id)+".jpg")
            return {'operation': 'success'}
        except Exception as e:
            return {'operation': 'fail', "error_code":"1001"}


class ChangePass (Resource):
    def post(self):
        password = request.get_json()['password']
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation':'fail' , "error_code": "2001"}
        stoken = str(token)
        token = token.replace("'", "")
        token = bytes(token[1:], 'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', "error_code": "2005"}
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("UPDATE users SET password = MD5(%s) where token= %s and ID = %s" , (str(password), stoken,str(user_id)))
            db.commit()
            cursor.close()
            db.close()
            return {'operation': 'success'}
        except Exception as e:
            return {'operation': 'fail', "error_code":"1001"}


class ChangeName (Resource):
    def post(self):
        name = request.get_json()['name']
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
            cursor.execute("UPDATE users SET user_name = %s where token= %s and ID= %s" , (str(name), stoken , str(user_id)))
            db.commit()
            cursor.close()
            db.close()
            return {'operation': 'success'}
        except Exception as e:
            return {'operation': 'fail', "error_code":"1001"}


class Premium (Resource):
    def post(self):
        pass