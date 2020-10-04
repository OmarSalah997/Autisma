from flask import Flask
from flask_restful import Api
from DB import *
from authorizationRoutes import *


app = Flask(__name__)
api = Api(app)
DB_intialize()


class Doctors(Resource):
    def get(self):
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("select * from doctors")
            return {'result': [list(i) for i in cursor.fetchall()], 'operation': 'success'}
        except Exception as e:
            return {'operation':'fail' , "error_code" : "1001"}


class Institution(Resource):
    def get(self):
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("select * from institution")
            return {'result': [list(i) for i in cursor.fetchall()], 'operation': 'success'}
        except Exception as e:
            return {'operation':'fail', "error_code" : "1001"}


class Alarm(Resource):
    def get(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation':'fail' , "error_code" : "2001"}
        stoken = str(token)
        token=token.replace("'","")
        token=bytes(token[1:],'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id ,int):
            return {'operation': 'fail' ,"error_code" : "2005" }
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("select alarms from users where ID =%s and token=%s",(str(user_id),stoken))
            rows=cursor.fetchall()
            if cursor.rowcount > 0 :
                return {'alarms': str(rows[0][0]) , 'operation':'success'}
            else:
                return {'operation': 'fail', "error_code": "2005"}
        except Exception as e:
            return {'operation':'fail' , "error_code" : "1001"}

    def post(self):
        auth_header = request.headers.get('Authorization')
        if auth_header:
            token = auth_header.split(" ")[1]
        else:
            return {'operation':'fail' ,  "error_code" : "2001"}
        stoken=str(token)
        token=token.replace("'","")
        token=bytes(token[1:],'utf-8')
        user_id = decode_auth_token(token)
        if not isinstance(user_id ,int):
            return {'operation': 'fail' ,"error_code" : "2005" }

        alarm = request.get_json()['alarms']
        try:
            db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
            cursor = db.cursor()
            cursor.execute("UPDATE users SET alarms = %s where ID =%s and token=%s",(str(alarm) ,str(user_id),stoken))
            db.commit()
            cursor.close()
            db.close()
            return {'operation': 'success'}
        except Exception as e:
            return {'operation':'fail', "error_code" : "1001"}




# assign Routes
api.add_resource(Doctors, '/doctors')
api.add_resource(Institution, '/institution')
api.add_resource(Alarm, '/alarm')
api.add_resource(Login, '/login')
api.add_resource(Signup, '/signup')
api.add_resource(ForgetPass, '/forgetpass')
api.add_resource(SetNewPass, '/setnewpass')
api.add_resource(Logout, '/logout')
api.add_resource(Confirm, '/confirm')
api.add_resource(ChangePass, '/changepass')
api.add_resource(ChangeName, '/changename')
api.add_resource(Profile, '/profile')
api.add_resource(ProfileImg, '/profileImg')
api.add_resource(MCQ, '/mcq')
api.add_resource(ImgTest, '/imgtest')
api.add_resource(VidTest, '/vidtest')


if __name__ == "__main__":
    app.run(port='5002',debug=True)

