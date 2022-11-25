# TodoServiceRestApi
This pet is just to secure my knowlage from cources.
ToDo App to set todo's lists and tasks for users, also create users. Users can be registered and has their own roles.
Build with Spring Security, Jwt Token and DB. 

To run:
1.Open the project in IntelliJIDEA Ultimate Edition.
-----
2.Install  postgresSQL.
-----
3.Set up app.properties: spring.datasource.url=jdbc:postgresql://localhost:(yourPort)/(yourDbName)
-----
Go to Postman and login as Admin\User or create account(user)
POST:localhost:8080/api/auth/login
JSON:{
"username" : mike@mail.com
"password" : 1111
}
Admin: mike@mail.com \ 1111
User: nick@mail.com \ 2222
----
