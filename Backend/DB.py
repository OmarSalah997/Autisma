import mysql.connector as mysql

def DB_intialize():
    # it takes 3 required parameters 'host', 'user', 'passwd'
    db = mysql.connect(host="localhost", user="root", passwd="1234")
    cursor = db.cursor()
    # it returns a list of all databases present
    cursor.execute("SHOW DATABASES")
    databases = cursor.fetchall()
    if databases[1][0] != "mydb":
        # creating Database
        cursor.execute("CREATE DATABASE MyDB")
        db = mysql.connect(host="localhost", user="root", passwd="1234", database="mydb")
        cursor = db.cursor()
        cursor.execute(
            "CREATE TABLE users (ID INT AUTO_INCREMENT PRIMARY KEY , email VARCHAR(255) UNIQUE NOT NULL , type BOOLEAN NOT NULL , user_name VARCHAR(255) NOT NULL , password VARCHAR(32) NOT NULL , concode VARCHAR(6), valid BOOLEAN NOT NULL , premium BOOLEAN NOT NULL , alarms TEXT , answers VARCHAR(30), token TEXT  )")
        cursor.execute(
            "CREATE TABLE doctors (name VARCHAR(255) NOT NULL PRIMARY KEY , specialization TEXT , gover VARCHAR(30) , address VARCHAR(255), phone VARCHAR(11) , booking TEXT)")
        cursor.execute(
            "CREATE TABLE institution (name VARCHAR(255) NOT NULL PRIMARY KEY , description TEXT, gover VARCHAR(30) , address VARCHAR(255) , phone VARCHAR(11))")
        cursor.execute("CREATE TABLE image_numbers(name VARCHAR(255) NOT NULL PRIMARY KEY , number INT NOT NULL)")
        db.commit()
        cursor.close()
        db.close()
