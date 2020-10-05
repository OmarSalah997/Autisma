from ProfileRoutes import *


class MCQ (Resource):
    def get(self):
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
            cursor.execute("select answers from users where token=%s and ID = %s",(stoken , str(user_id)))
            r=cursor.fetchall()
            rows = cursor.rowcount
            if rows == 0:
                return {'operation': 'fail', 'error_code': "2005"}
            else:
                return {'answers': str(r[0][0]) , 'operation':'success'}
        except Exception as e:
            return {'operation':'fail',"error_code": "1001"}


    def post(self):
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

        answers = request.get_json()['answers']
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("UPDATE users SET answers = %s where token= %s" , (str(answers), stoken))
            db.commit()
            cursor.close()
            db.close()
            return {'operation': 'success'}
        except Exception as e:
            return {'operation':'fail',"error_code": "1001"}


class ImgTest(Resource):
    def get(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation':'fail' , "error_code": "2001"}
        token = token.replace("'", "")
        token = bytes(token[1:], 'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', "error_code": "2005"}
        # read image and response


    def post(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation':'fail' , "error_code": "2001"}
        token = token.replace("'", "")
        token = bytes(token[1:], 'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', "error_code": "2005"}

        # extract the image and save it


class VidTest (Resource):
    def get(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation':'fail' , "error_code": "2001"}
        token = token.replace("'", "")
        token = bytes(token[1:], 'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', "error_code": "2005"}

    def post(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation':'fail' , "error_code": "2001"}
        token = token.replace("'", "")
        token = bytes(token[1:], 'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', "error_code": "2005"}
