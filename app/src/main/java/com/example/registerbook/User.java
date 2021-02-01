package com.example.registerbook;
//this is a user class object to save the user info in the java
//object so we ca n send the object to firebase
public class User {

        //create string variables for name, age, email
        public String fullName, age, email;
        // add 2 constructors that match name of class, 1 empty public constructor and the other
        //should accept 3 argument.
        public User(){

        }
        public User(String fullName, String age, String email){
            //now initialize these variables
            this.fullName = fullName;
            this.age = age;
            this.email = email;
        }

    }
