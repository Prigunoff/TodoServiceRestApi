# TodoServiceRestApi
This pet is just to secure my knowlage from cources.
ToDo App to set todo's lists and tasks for users, also create users. Users can be registered and has their own roles.
Build with Spring Security, Jwt,DB. 

### To run:

1.Open the project in IntelliJIDEA Ultimate Edition.
-----
2.Install  postgresSQL.
-----
3.Set up app.properties: spring.datasource.url=jdbc:postgresql://localhost:(yourPort)/(yourDbName)
-----
## Go to Postman and login as Admin\User or create account(user)
- Admin has all the permissions. User can only create\read\update\delete\read his own info about account and his own todos.
----
4.To login send: 
POST:localhost:8080/api/auth/login
----
- JSON:{
"username" : "mike@mail.com"
"password" : 1111
}

- Admin: mike@mail.com \ 1111
- User: nick@mail.com \ 2222
----
 ## To create USER send POST to:
- api/users/
----
- JSON:{
    "first_name":"Stepan",
    "last_name":"Bandera",
    "email":"bandera@mail.com",
    "password":"uRp4$$w0rD"
}
----
- Go to login (api/auth/login)and take your token and put him into Headers > KEY:Authorization, VALUE: Bearer_YourToken
----
User endpoints:
1. GET:api/users/{id}, GET:api/users/all
2. POST:api/users/
3. PUT:api/users/ ( to update user just add to JSON from 5th point "id":userId
4. DELETE:api/users/{id}
----

### TODO
Todo endpoits:
1. GET:/api/todos{id} , GET:/api/todos/ , GET:/api/todos/{userId}/all
2. POST:/api/todos/{id}
    - JSON { "title": "Write hello World" }
3. PUT:/api/todos
    - JSON {"id" : 12,"title" : "Updated title"}
4. DELETE:/api/todos/{id}
----
### TASK
Task lives inside todo.

Task endpoits:

1.GET:/api/tasks/, GET:/api/tasks/{id}

2.POST:/api/tasks/todo/{id}
   - JSON {"task_id" : 12,"task" : "Test","priority" : "LOW","state_id" : 5,"todo_id" : 12}

4.DELETE:/api/tasks/{id}
