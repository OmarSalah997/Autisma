import jwt
import datetime
import os


def encode_auth_token(user_id):
    try:
        # exp: expiration date of the token
        # iat: the time the token is generated
        # sub: the subject of the token (the user whom it identifies)
        payload = {
            'exp': datetime.datetime.utcnow() + datetime.timedelta(days=7),
            'iat': datetime.datetime.utcnow(),
            'sub': user_id
        }
        return jwt.encode(payload, os.environ['MY_SECRET'], algorithm='HS256')
    except Exception as e:
        return "error: " + str(e)

# return int user_id in normal situation.
# in case of error it returns string with the error.
def decode_auth_token(auth_token):
    try:
        payload = jwt.decode(auth_token, os.environ['MY_SECRET'])
        return int(payload['sub'])
    except jwt.InvalidTokenError:
        return 'Invalid token.'
    except jwt.ExpiredSignatureError:
        return 'Token expired. Please log in again.'


#print(type(b'eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJleHAiOjE2MDE5MDEzOTMsImlhdCI6MTYwMTI5NjU5Mywic3ViIjozfQ.OPajjgzhrjqxfj_YAyRj8R9FJS98uR-S69GsSqdPOxc'))