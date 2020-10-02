from TestsRoutes import *
from random import randint
from emails import *

class Login(Resource):
    def post(self):
        # receiving json
        data = request.get_json()
        email = data['email']
        password = data['password']
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute('SELECT Valid FROM USERS WHERE EMAIL="'+str(email)+'"')
            rows=cursor.fetchall()
            rows_number= int(cursor.rowcount)
            if rows_number!=1:
                return {'operation': 'fail', 'error_code':"2004"}
            if rows_number==1 and not bool(rows[0][0]):
                return {'operation': 'fail', 'error_status': "2002"}
        except:
            return {'operation': 'fail','error_status': "1001"}

        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute('SELECT ID FROM USERS WHERE EMAIL=%s and password=MD5(%s)', (str(email) , str(password)))
            rows = cursor.fetchall()
            rows_number = int(cursor.rowcount)
            if rows_number != 1:
                return {'operation': 'fail', 'error_code': "2004"}
            else:
                token = encode_auth_token(int(rows[0][0]))
                db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
                cursor = db.cursor()
                cursor.execute('UPDATE users SET token = %s where email = %s',(str(token), str(email)))
                db.commit()
                cursor.close()
                db.close()
                return {'operation': 'success', 'token': str(token)}
        except:
            return {'operation': 'fail','error_status': "1001"}


class Logout (Resource):
    def post(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation': 'fail' , 'error_code': "2005"}
        token=token.replace("'","")
        token=bytes(token[1:],'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id, int):
            return {'operation': 'fail', 'error_code': "2005"}
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute('UPDATE users SET token = "" where ID='+str(user_id))
            db.commit()
            cursor.close()
            db.close()
            return {'operation': 'success'}
        except Exception as e:
            return {'operation':'fail' , "error_code":"1001"}


class Signup(Resource):
    def post(self):
        # receiving json
        data = request.get_json()
        name=data['user_name']
        email=data['email']
        password=data['password']
        # check email is already in database then add it if not in database
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute('SELECT ID FROM USERS WHERE EMAIL="'+str(email)+'"')
            cursor.fetchall()
            rows= cursor.rowcount
            cursor.close()
            if rows > 0 :
                return {'operation': 'fail' , 'error_code': "2006"}
            else:
                conf = ''.join(["{}".format(randint(0, 9)) for num in range(6)])
                cursor = db.cursor()
                cursor.execute("INSERT INTO users (email, user_name , password , ConCode  )VALUES(%s, %s , MD5(%s) , %s)",(str(email), str(name), str(password),str(conf) ))
                db.commit()
                cursor.close()
                # send email to verify the email
                send_email(name, email , "Autizma app account confirmation" , conf)
                return {'operation': 'success'}
        except Exception as e:
            return {'operation': 'fail', "error_code":"1001"}


class ForgetPass(Resource):
    def post(self):
        email = request.get_json()['email']

        return {'operation': 'success'}


class Confirm (Resource):
    def post(self):
        # receiving json
        data = request.get_json()
        code=data['code']
        email=data['email']
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute('SELECT ID FROM USERS WHERE EMAIL="' + str(email) + '"')
            user_id = int(cursor.fetchone()[0])
            rows = cursor.rowcount
            cursor.close()
            if rows == 0:
                return {'operation': 'fail', 'error_code': "2001"}
            else:
                cursor = db.cursor()
                cursor.execute('SELECT ConCode FROM USERS WHERE EMAIL="' + str(email) + '"')
                saved_code = str(cursor.fetchone()[0])
                print(saved_code)
                if saved_code == str(code):
                    token = encode_auth_token(user_id)
                    cursor.execute('UPDATE users SET ConCode = "" , token = %s , valid = 1 where email = %s',(str(token) , str(email)))
                    db.commit()
                    cursor.close()
                    db.close()
                    return {'operation': 'success' , 'token': str(token)}
        except Exception as e:
            return {'operation': 'fail', "error_code" : "1001"}