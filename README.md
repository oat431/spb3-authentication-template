# 2023 version of JWT authentication using Spring Boot and Spring Security
- This will be used as a backbone of my future projects
- It will be role based authentication

## Features

### Sign up

```ruby
POST /api/v1/auth/signup
body: {
    "profilepicture": "profilepicture",
    "platfromname": "platfromname",
    "username": "username",
    "email": "email",
    "firstname": "firstname",
    "lastname": "lastname",
    "tel": "tel",
    "password": "password",
    "birthday": "birthday",
    "address": "address",
    "city": "city",
    "state": "state",
    "country": "country",
    "zip": "zip"
}
```

### Sign in

```ruby
POST /api/v1/auth/
body: {
    "loginname": "loginname",
    "password": "password"
}
or
body: {
    "email": "email",
    "password": "password"
}
```

### Update Account

#### Update Account Details

```ruby
PUT /api/v1/auth/
body: {
    "profilepicture": "profilepicture",
    "platfromname": "platfromname",
    "firstname": "firstname",
    "lastname": "lastname",
    "tel": "tel",
    "birthday": "birthday",
    "address": "address",
    "city": "city",
    "state": "state",
    "country": "country",
    "zip": "zip"
}
```

#### Update Account Password

```ruby
PATCH /api/v1/auth/password
body: {
    "newPassword": "newPassword"
}
```

#### Update Account Email

```ruby
PATCH /api/v1/auth/email
body: {
    "newEmail": "newEmail"
}
```

#### Update Account username

```ruby
PATCH /api/v1/auth/username
body: {
    "newUsername": "newUsername"
}
```

### Delete Account
```ruby
DELETE /api/v1/auth/
```

### Refresh token
```ruby
POST /api/v1/auth/refresh
body: {
    token: "token"
}
```

### Forgot password
```ruby
POST /api/v1/auth/forgotpassword
body: {
    email: "email"
}
```

## To Enhance in the future
- [ ] Add email verification
- [ ] Add OTP verification
- [ ] Add 2FA

## Todo Next
- [x] Add Graalvm Building tools to the project
- [x] Add unit test
- [ ] Add integration test
- [x] Containerized using Docker
- [ ] Create Auto CI
- [x] Add documentation
